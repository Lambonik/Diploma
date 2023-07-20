package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.sql.DriverManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentTest {
    String xpathFieldCardNumber = "//*[@class='input__top'][text()='Номер карты']/..";
    String xpathFieldMonth = "//*[@class='input__top'][text()='Месяц']/..";
    String xpathFieldYear = "//*[@class='input__top'][text()='Год']/..";
    String xpathFieldCardHolder = "//*[@class='input__top'][text()='Владелец']/..";
    String xpathFieldSecurityCode = "//*[@class='input__top'][text()='CVC/CVV']/..";
    String xpathInputField = "//*[@class='input__control']";
    String approvedCardNumber = "4444 4444 4444 4441";
    String declinedCardNumber = "4444 4444 4444 4442";

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        //Configuration.holdBrowserOpen=true;
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
    @SneakyThrows
    void case1ShouldApprovedPayment() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(approvedCardNumber);
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(0));
        var statusSQL = "SELECT * FROM payment_entity LIMIT 1;";
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/app", "app", "pass");
                var statusStmt = connection.prepareStatement(statusSQL);
        ) {
            try (var rs = statusStmt.executeQuery()) {
                while (rs.next()) {
                    var status = rs.getString("status");
                    Assertions.assertEquals("APPROVED", status);
                }
            }
        }
    }

    @Test
    @SneakyThrows
    void case2ShouldDeclinedPayment() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(declinedCardNumber);
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(0));
        var statusSQL = "SELECT * FROM payment_entity LIMIT 1;";
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/app", "app", "pass");
                var statusStmt = connection.prepareStatement(statusSQL);
        ) {
            try (var rs = statusStmt.executeQuery()) {
                while (rs.next()) {
                    var status = rs.getString("status");
                    Assertions.assertEquals("DECLINED", status);
                }
            }
        }
    }

    @Test
    @SneakyThrows
    void case3ShouldApprovedCredit() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить в кредит")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(approvedCardNumber);
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(0));
        var statusSQL = "SELECT * FROM credit_request_entity LIMIT 1;";
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/app", "app", "pass");
                var statusStmt = connection.prepareStatement(statusSQL);
        ) {
            try (var rs = statusStmt.executeQuery()) {
                while (rs.next()) {
                    var status = rs.getString("status");
                    Assertions.assertEquals("APPROVED", status);
                }
            }
        }
    }

    @Test
    @SneakyThrows
    void case4ShouldDeclinedCredit() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить в кредит")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(declinedCardNumber);
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(0));
        var statusSQL = "SELECT * FROM credit_request_entity LIMIT 1;";
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://192.168.99.100:3306/app", "app", "pass");
                var statusStmt = connection.prepareStatement(statusSQL);
        ) {
            try (var rs = statusStmt.executeQuery()) {
                while (rs.next()) {
                    var status = rs.getString("status");
                    Assertions.assertEquals("DECLINED", status);
                }
            }
        }
    }

    @Test
    void case5PaymentIfCardNumberIsEmpty() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardNumber)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case6PaymentIfCardNumberIs15Digits() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber()).sendKeys(Keys.chord(Keys.BACK_SPACE));
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardNumber)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case7PaymentIfCardNumberIs1Digit() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(String.valueOf(randomNum));
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardNumber)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case8PaymentIfMonthIsEmpty() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case9PaymentIfMonthValue_00() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue("00");
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case10PaymentIfMonthValue_13() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue("13");
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case11PaymentIfMonthValueLessThanCurrent() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, LocalDate.now().getMonthValue() - 1);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(LocalDate.now().minusMonths(randomNum).format(DateTimeFormatter.ofPattern("MM")));
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(LocalDate.now().format(DateTimeFormatter.ofPattern("YY")));
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldMonth)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case12PaymentIfYearIsEmpty() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldYear)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case13PaymentIfYearValueLessThanCurrent() {
        int randomNum = ThreadLocalRandom.current().nextInt(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY"))) + 6, 99);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(String.valueOf(randomNum));
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldYear)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case14PaymentIfYearValueMoreThanCurrentBy5() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY"))) - 1);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(LocalDate.now().minusYears(randomNum).format(DateTimeFormatter.ofPattern("YY")));
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldYear)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case15PaymentIfSecurityCodeIsEmpty() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldSecurityCode)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case16PaymentIfSecurityCodeIs1Digit() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode()).sendKeys(Keys.chord(Keys.BACK_SPACE));
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldSecurityCode)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case17PaymentIfSecurityCodeIs2Digits() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode()).sendKeys(Keys.chord(Keys.BACK_SPACE, Keys.BACK_SPACE));
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldSecurityCode)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case18PaymentIfCardHolderIsEmpty() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardHolder)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case19PaymentIfCardHolderContainsCyrillic() {
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolderCyrillic());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardHolder)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    @Test
    void case20PaymentIfCardHolderContainsDigit() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        var card = DataGenerator.Payment.generateCard();
        $$("button").find(exactText("Купить")).click();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(String.valueOf(randomNum));
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        $$("button").find(exactText("Продолжить")).click();
        $(By.xpath(xpathFieldCardHolder)).find(".input__sub").shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }
}
