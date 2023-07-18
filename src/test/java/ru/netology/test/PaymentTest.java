package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class PaymentTest {
    String xpathFieldCardNumber="//*[@class='input__top'][text()='Номер карты']/..";
    String xpathFieldMonth="//*[@class='input__top'][text()='Месяц']/..";
    String xpathFieldYear="//*[@class='input__top'][text()='Год']/..";
    String xpathFieldCardHolder="//*[@class='input__top'][text()='Владелец']/..";
    String xpathFieldSecurityCode="//*[@class='input__top'][text()='CVC/CVV']/..";
    String xpathInputField="//*[@class='input__control']";
    String approvedCardNumber="4444 4444 4444 4441";
    String declinedCardNumber="4444 4444 4444 4442";
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.holdBrowserOpen=true;
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @Test
    void case1ShouldApprovedPayment() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(approvedCardNumber);
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Операция одобрена Банком.")).shouldBe(Condition.visible,Duration.ofSeconds(15));
    }

    @Test
    void case2ShouldDeclinedPayment() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(declinedCardNumber);
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка! Банк отказал в проведении операции.")).shouldBe(Condition.visible,Duration.ofSeconds(15));
    }

    @Test
    void case3ShouldApprovedCredit() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить в кредит")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(approvedCardNumber);
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Операция одобрена Банком.")).shouldBe(Condition.visible,Duration.ofSeconds(15));
    }

    @Test
    void case4ShouldDeclinedCredit() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить в кредит")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(declinedCardNumber);
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(byText("Ошибка! Банк отказал в проведении операции.")).shouldBe(Condition.visible,Duration.ofSeconds(15));
    }

    @Test
    void case5() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardNumber)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case6() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber()).sendKeys(Keys.chord(Keys.BACK_SPACE));
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardNumber)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case7() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(String.valueOf(randomNum));
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardNumber)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case8() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case9() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue("00");
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case10() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue("13");
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case11() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, LocalDate.now().getMonthValue()-1);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(LocalDate.now().minusMonths(randomNum).format(DateTimeFormatter.ofPattern("MM")));
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("YY")));
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case12() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldYear)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case13() {
        int randomNum = ThreadLocalRandom.current().nextInt(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY")))+6, 99);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(String.valueOf(randomNum));
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldYear)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case14() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY")))-1);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(LocalDate.now().minusYears(randomNum).format(DateTimeFormatter.ofPattern("YY")));
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldYear)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case15() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldSecurityCode)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case16() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode()).sendKeys(Keys.chord(Keys.BACK_SPACE));
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldSecurityCode)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case17() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode()).sendKeys(Keys.chord(Keys.BACK_SPACE,Keys.BACK_SPACE));
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldSecurityCode)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case18() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardHolder)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case19() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(card.getCardHolderCyrillic());
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardHolder)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }

    @Test
    void case20() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber+xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth+xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear+xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder+xpathInputField)).setValue(String.valueOf(randomNum));
        $(By.xpath(xpathFieldSecurityCode+xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardHolder)).find(".input__sub").shouldBe(Condition.visible,Duration.ofSeconds(1));
    }
}
