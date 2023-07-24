package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.page.PaymentPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static com.codeborne.selenide.Selenide.*;

public class PaymentTest {
    private DataGenerator.CardInfo card = DataGenerator.Payment.generateCard();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.holdBrowserOpen = true;
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
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardNumberIs(card.getApprovedCardNumber());
        new PaymentPage().buttonPayment();

        new PaymentPage().checkApprovedNotification();
        String status = DataGenerator.Query.queryPayment(new PaymentPage().buttonSelectFormPayment());
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    void case2ShouldDeclinedPayment() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardNumberIs(card.getApprovedCardNumber());
        new PaymentPage().buttonPayment();

        new PaymentPage().checkDeclinedNotification();
        String status = DataGenerator.Query.queryPayment(new PaymentPage().buttonSelectFormPayment());
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    void case3ShouldApprovedCredit() {
        new PaymentPage().buttonSelectFormCredit();
        new PaymentPage().fillingFieldsWhereCardNumberIs(card.getApprovedCardNumber());
        new PaymentPage().buttonPayment();

        new PaymentPage().checkApprovedNotification();
        String status = DataGenerator.Query.queryPayment(new PaymentPage().buttonSelectFormCredit());
        Assertions.assertEquals("APPROVED", status);
    }

    @Test
    void case4ShouldDeclinedCredit() {
        new PaymentPage().buttonSelectFormCredit();
        new PaymentPage().fillingFieldsWhereCardNumberIs(card.getDeclinedCardNumber());
        new PaymentPage().buttonPayment();

        new PaymentPage().checkDeclinedNotification();
        String status = DataGenerator.Query.queryPayment(new PaymentPage().buttonSelectFormCredit());
        Assertions.assertEquals("DECLINED", status);
    }

    @Test
    void case5PaymentIfCardNumberIsEmpty() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardNumberIs(null);
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldCardNumber();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case6PaymentIfCardNumberIs15Digits() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardNumberIs(card.getCardNumber().substring(1));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldCardNumber();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case7PaymentIfCardNumberIs1Digit() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardNumberIs(String.valueOf(randomNum));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldCardNumber();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case8PaymentIfMonthIsEmpty() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereMonthIs(null, Integer.valueOf(card.getYear()));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldMonth();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case9PaymentIfMonthValue_00() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereMonthIs("00", Integer.valueOf(card.getYear()) + 1);
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldMonth();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case10PaymentIfMonthValue_13() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereMonthIs("13", Integer.valueOf(card.getYear()));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldMonth();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case11PaymentIfMonthValueLessThanCurrent() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, LocalDate.now().getMonthValue() - 1);
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereMonthIs(LocalDate.now().minusMonths(randomNum).format(DateTimeFormatter.ofPattern("MM")), Integer.valueOf(card.getYear()));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldMonth();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case12PaymentIfYearIsEmpty() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereYearIs(null);
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldYear();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case13PaymentIfYearValueLessThanCurrent() {
        int randomNum = ThreadLocalRandom.current().nextInt(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY"))) + 6, 99);
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereYearIs(String.valueOf(randomNum));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldYear();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case14PaymentIfYearValueMoreThanCurrentBy5() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY"))) - 1);
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereYearIs(LocalDate.now().minusYears(randomNum).format(DateTimeFormatter.ofPattern("YY")));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldYear();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case15PaymentIfSecurityCodeIsEmpty() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereSecurityCodeIs(null);
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldSecurityCode();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case16PaymentIfSecurityCodeIs1Digit() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereSecurityCodeIs(card.getSecurityCode().substring(1));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldSecurityCode();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case17PaymentIfSecurityCodeIs2Digits() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereSecurityCodeIs(card.getSecurityCode().substring(2));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldSecurityCode();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case18PaymentIfCardHolderIsEmpty() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardHolderIs(null);
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldCardHolder();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case19PaymentIfCardHolderContainsCyrillic() {
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardHolderIs(card.getCardHolderCyrillic());
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldCardHolder();
        new PaymentPage().checkingButtonPayment();
    }

    @Test
    void case20PaymentIfCardHolderContainsDigit() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        new PaymentPage().buttonSelectFormPayment();
        new PaymentPage().fillingFieldsWhereCardHolderIs(String.valueOf(randomNum));
        new PaymentPage().buttonPayment();

        new PaymentPage().checkingSubscriptionFieldCardHolder();
        new PaymentPage().checkingButtonPayment();
    }
}
