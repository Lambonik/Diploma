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
    private DataGenerator.CardInfo cardWithCardNumberIsEmpty = DataGenerator.Payment.generateCardWithCardNumberIsEmpty();
    private DataGenerator.CardInfo cardWithMonthIsEmpty = DataGenerator.Payment.generateCardWithMonthIsEmpty();
    private DataGenerator.CardInfo cardWithYearIsEmpty = DataGenerator.Payment.generateCardWithYearIsEmpty();
    private DataGenerator.CardInfo cardWithCardHolderIsEmpty = DataGenerator.Payment.generateCardWithCardHolderIsEmpty();
    private DataGenerator.CardInfo cardWithSecurityCodeIsEmpty = DataGenerator.Payment.generateCardWithSecurityCodeIsEmpty();
    private DataGenerator.CardInfo cardWithApprovedCardNumber = DataGenerator.Payment.generateCardWithApprovedCardNumber();
    private DataGenerator.CardInfo cardWithDeclinedCardNumber = DataGenerator.Payment.generateCardWithDeclinedCardNumber();
    private DataGenerator.CardInfo cardWithMountLessThanCurrent = DataGenerator.Payment.generateCardWithMountLessThanCurrent();
    private DataGenerator.CardInfo cardWithCardHolderCyrillic = DataGenerator.Payment.generateCardWithCardHolderCyrillic();
    private DataGenerator.CardInfo cardWithYearLessThanCurrent = DataGenerator.Payment.generateCardWithYearLessThanCurrent();
    private DataGenerator.CardInfo cardWithYearMoreThanCurrentBy5 = DataGenerator.Payment.generateCardWithYearMoreThanCurrentBy5();
    private DataGenerator.CardInfo cardWithCardHolderDigit = DataGenerator.Payment.generateCardWithCardHolderDigit();
    private DataGenerator.CardInfo cardWithCardNumberIs15Digits = DataGenerator.Payment.generateCardWithCardNumberIs15Digits();
    private DataGenerator.CardInfo cardWithCardNumberIs1Digit = DataGenerator.Payment.generateCardWithCardNumberIs1Digit();
    private DataGenerator.CardInfo cardWithMonthValue_00 = DataGenerator.Payment.generateCardWithMonthValue_00();
    private DataGenerator.CardInfo cardWithMonthValue_13 = DataGenerator.Payment.generateCardWithMonthValue_13();
    private DataGenerator.CardInfo cardWithSecurityCodeIs1Digit = DataGenerator.Payment.generateCardWithSecurityCodeIs1Digit();
    private DataGenerator.CardInfo cardWithSecurityCodeIs2Digits = DataGenerator.Payment.generateCardWithSecurityCodeIs2Digits();

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
        paymentPage.fillingFields(cardWithApprovedCardNumber);
        paymentPage.buttonPayment();

        paymentPage.checkApprovedNotification();
        String status = DataGenerator.Query.queryPayment("Payment");
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    void case2ShouldDeclinedPayment() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithDeclinedCardNumber);
        paymentPage.buttonPayment();

        paymentPage.checkDeclinedNotification();
        String status = DataGenerator.Query.queryPayment("Payment");
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    void case3ShouldApprovedCredit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithApprovedCardNumber);
        paymentPage.buttonPayment();

        paymentPage.checkApprovedNotification();
        String status = DataGenerator.Query.queryPayment("Credit");
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    void case4ShouldDeclinedCredit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithDeclinedCardNumber);
        paymentPage.buttonPayment();

        paymentPage.checkDeclinedNotification();
        String status = DataGenerator.Query.queryPayment("Credit");
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    void case5PaymentIfCardNumberIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithCardNumberIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case5_1CreditIfCardNumberIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithCardNumberIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case6PaymentIfCardNumberIs15Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithCardNumberIs15Digits);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case6_1CreditPaymentIfCardNumberIs15Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithCardNumberIs15Digits);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case7PaymentIfCardNumberIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithCardNumberIs1Digit);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case7_1CreditIfCardNumberIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithCardNumberIs1Digit);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardNumber("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case8PaymentIfMonthIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithMonthIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case8_1CreditIfMonthIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithMonthIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case9PaymentIfMonthValue_00() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithMonthValue_00);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case9_1CreditIfMonthValue_00() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithMonthValue_00);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case10PaymentIfMonthValue_13() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithMonthValue_13);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case10_1CreditIfMonthValue_13() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithMonthValue_13);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case11PaymentIfMonthValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithMountLessThanCurrent);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case11_1CreditIfMonthValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithMountLessThanCurrent);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldMonth("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case12PaymentIfYearIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithYearIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case12_1CreditIfYearIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithYearIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case13PaymentIfYearValueMoreThanCurrentBy5() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithYearMoreThanCurrentBy5);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case13_1CreditIfYearMoreThanCurrentBy5() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithYearMoreThanCurrentBy5);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Неверно указан срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case14PaymentIfYearValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithYearLessThanCurrent);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Истёк срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case14_1CreditIfYearValueLessThanCurrent() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithYearLessThanCurrent);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldYear("Истёк срок действия карты");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case15PaymentIfSecurityCodeIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithSecurityCodeIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case15_1CreditIfSecurityCodeIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithSecurityCodeIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case16PaymentIfSecurityCodeIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithSecurityCodeIs1Digit);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case16_1CreditIfSecurityCodeIs1Digit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithSecurityCodeIs1Digit);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case17PaymentIfSecurityCodeIs2Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithSecurityCodeIs2Digits);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case17_1CreditIfSecurityCodeIs2Digits() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithSecurityCodeIs2Digits);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldSecurityCode("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case18PaymentIfCardHolderIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithCardHolderIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case18_1CreditIfCardHolderIsEmpty() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithCardHolderIsEmpty);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Поле обязательно для заполнения");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case19PaymentIfCardHolderContainsCyrillic() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithCardHolderCyrillic);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case19_1CreditIfCardHolderContainsCyrillic() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithCardHolderCyrillic);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case20PaymentIfCardHolderContainsDigit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormPayment();
        paymentPage.fillingFields(cardWithCardHolderDigit);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }

    @Test
    void case20_1CreditIfCardHolderContainsDigit() {
        var paymentPage = new PaymentPage();
        paymentPage.buttonSelectFormCredit();
        paymentPage.fillingFields(cardWithCardHolderDigit);
        paymentPage.buttonPayment();

        paymentPage.checkingSubscriptionFieldCardHolder("Неверный формат");
        paymentPage.checkingButtonPayment();
    }
}
