package yandex.market;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class Tests extends WebDriverSettings {


    /*
    Тест-кейс №1
    1.В яндекс маркете выбрать телефоны.
    2.Установить фильтр айфоны.
    3.Убедится что на всех страницах (если их несколько), присутствуют только айфоны.
     */

    @Test

    public void testCaseYandexSearchIphone() {
        String searchWord = "Телефоны";
        PageObjectYaMarket yaMarketWithSearch = new PageObjectYaMarket(chromeDriver, searchWord);
        List<Map<String,Object>> resultSearch = yaMarketWithSearch.getCollectResult();
        //resultSearch.stream().forEach(x-> System.out.println(x.get("namePhone").toString()));           //выражение проверки моделей телефонов, записанных в лист
        //Assertions.assertTrue(resultSearch.stream().allMatch(x->x.toString().contains("APPLE")), "Среди производителей обнаружена компания не APPLE"); //заготовка для проверки компании-производителя на странице
        Assertions.assertTrue(resultSearch.stream().allMatch(x->x.toString().contains("iPhone")), "Обнаружен телефон модели отличающейся от iPhone");     //проверка, что на странице все телефоны марки айфон

    }


}
