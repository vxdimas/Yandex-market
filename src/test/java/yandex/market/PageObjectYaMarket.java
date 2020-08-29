package yandex.market;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PageObjectYaMarket {

    private static WebDriver driver;

    private String selectorSearchItem = "//article";
    private String selectorManufacturer = ".//div[@class='_1NyIdwOZ6-']";
    private String selectorNamePhone = ".//h3[@class='_3dCGE8Y9v3 _25uOAS5e4A']//span";
    private String buttonShowMore = "//button[@data-tid='42371cd3']";
    private String selectorLoadScreen = "//div[@class='_2LvbieS_AO _1oZmP3Lbj2']";


    private List<WebElement> searchItem = new ArrayList<>();
    private List<Map<String,Object>> collectResult = new ArrayList<>();

    public PageObjectYaMarket(WebDriver driver, String search) {
        this.driver = driver;
        this.driver.get("https://market.yandex.ru/");
        WebElement searchField = driver.findElement(By.xpath("//input[@name=\"text\"]"));                               // поисковая строка в яндексе
        searchField.click();
        searchField.sendKeys(search);
        searchField.sendKeys(Keys.ENTER);
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(.,'Apple')]")));         //ожидание загрузки чекбокса
        WebElement abc = driver.findElement(By.xpath("//span[contains(.,'Apple')]"));
        abc.click();
        waitLoadAndCloseElement(selectorLoadScreen);                                                                    //ожидание появления и последующего закрытия экрана загрузки
        while (IsElementExists(By.xpath(buttonShowMore))) {                                                             //расширяем таблицу моделей телефонов, пока не исчезнет кнопка "Показать еще"
            driver.findElement(By.xpath(buttonShowMore)).click();
            waitLoad();                                                                                                 //работа с кнопкой "Показать еще"
        }
        searchItem = driver.findElements(By.xpath(selectorSearchItem));
    }

    public List<Map<String,Object>> getCollectResult() {
        for (WebElement result : searchItem) {
            collectResult.add(Map.of(
                    "WEB_ELEMENT", result,
                    "nameManufacturer", result.findElement(By.xpath(selectorManufacturer)).getText(),
                    "namePhone", result.findElement(By.xpath(selectorNamePhone)).getText()
            ));
        }
        return collectResult;
    }

    public boolean IsElementExists(By by)  //Проверка наличия элемента на странице, входные данные = адрес икспасс
    {
        try{
            driver.findElement(by);
            return true;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }

    public void waitLoadAndCloseElement(String selector) {
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(selector)));                              //ожидание появления элемента
        new WebDriverWait(driver, 5)
                .until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(selector))));                         //ожидание исчезновения элемента
    }



    /*
    Смысл метода waitLoad() заключается в том, что в Яндекс маркете после нажатия кнопки "показать еще" название меняется на символ загрузки.
    После подгрузки новых элементов, символ загрузки меняется опять на "Показать еще".
     */
    public void waitLoad() {
        pause(2000);
        if(IsElementExists(By.xpath(buttonShowMore))) {
            try {
                new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Показать ещё']")));
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    public void pause(Integer milliseconds){
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
