package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.page.PaymentPage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PaymentTest {
    private DataGenerator.CardInfo card = DataGenerator.Payment.generateCard();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        //Configuration.holdBrowserOpen = true;
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
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getApprovedCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkApprovedNotification();
        String status = DataGenerator.Query.queryPayment("Payment");
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    void case2ShouldDeclinedPayment() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getDeclinedCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkDeclinedNotification();
        String status = DataGenerator.Query.queryPayment("Payment");
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    void case3ShouldApprovedCredit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getApprovedCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkApprovedNotification();
        String status = DataGenerator.Query.queryPayment("Credit");
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    void case4ShouldDeclinedCredit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getDeclinedCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkDeclinedNotification();
        String status = DataGenerator.Query.queryPayment("Credit");
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    void case5PaymentIfCardNumberIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(null, card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case5_1CreditIfCardNumberIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(null, card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case6PaymentIfCardNumberIs15Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber().substring(1), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case6_1CreditPaymentIfCardNumberIs15Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber().substring(1), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case7PaymentIfCardNumberIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber().substring(15), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case7_1CreditIfCardNumberIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber().substring(15), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case8PaymentIfMonthIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), null, card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case8_1CreditIfMonthIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), null, card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case9PaymentIfMonthValue_00() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), "00", card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case9_1CreditIfMonthValue_00() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), "00", card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case10PaymentIfMonthValue_13() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), "13", card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case10_1CreditIfMonthValue_13() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), "13", card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case11PaymentIfMonthValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMountLessThanCurrent(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case11_1CreditIfMonthValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMountLessThanCurrent(), card.getYear(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case12PaymentIfYearIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), null, card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case12_1CreditIfYearIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), null, card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case13PaymentIfYearValueMoreThanCurrentBy5() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYearMoreThanCurrentBy5(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case13_1CreditIfYearMoreThanCurrentBy5() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYearMoreThanCurrentBy5(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case14PaymentIfYearValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYearLessThanCurrent(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Истёк срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case14_1CreditIfYearValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYearLessThanCurrent(), card.getCardHolder(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Истёк срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case15PaymentIfSecurityCodeIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), null);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case15_1CreditIfSecurityCodeIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), null);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case16PaymentIfSecurityCodeIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode().substring(1));
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case16_1CreditIfSecurityCodeIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode().substring(1));
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case17PaymentIfSecurityCodeIs2Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode().substring(2));
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case17_1CreditIfSecurityCodeIs2Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolder(), card.getSecurityCode().substring(2));
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case18PaymentIfCardHolderIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), null, card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case18_1CreditIfCardHolderIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), null, card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case19PaymentIfCardHolderContainsCyrillic() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolderCyrillic(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case19_1CreditIfCardHolderContainsCyrillic() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getCardHolderCyrillic(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case20PaymentIfCardHolderContainsDigit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getDigit(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case20_1CreditIfCardHolderContainsDigit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(card.getCardNumber(), card.getMonth(), card.getYear(), card.getDigit(), card.getSecurityCode());
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }
}
