package ru.netology.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    String xpathFieldCardNumber = "//*[@class='input__top'][text()='Номер карты']/..";
    String xpathFieldMonth = "//*[@class='input__top'][text()='Месяц']/..";
    String xpathFieldYear = "//*[@class='input__top'][text()='Год']/..";
    String xpathFieldCardHolder = "//*[@class='input__top'][text()='Владелец']/..";
    String xpathFieldSecurityCode = "//*[@class='input__top'][text()='CVC/CVV']/..";
    String xpathInputField = "//*[@class='input__control']";
    String xpathInputFieldSub = "//*[@class='input__sub']";

    private DataGenerator.CardInfo card;

    public String buttonSelectFormPayment() {
        $$("button").find(exactText("Купить")).click();
        String typePayment = "Payment";
        return typePayment;
    }

    public String buttonSelectFormCredit() {
        $$("button").find(exactText("Купить в кредит")).click();
        String typePayment = "Credit";
        return typePayment;
    }

    public PaymentPage buttonPayment() {
        $$("button").find(exactText("Продолжить")).click();
        return this;
    }

    public PaymentPage checkApprovedNotification() {
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage checkDeclinedNotification() {
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage fillingFieldsWhereCardNumberIs(String testedValue) {
        card = DataGenerator.Payment.generateCard();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(testedValue);
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        return this;
    }

    public PaymentPage fillingFieldsWhereMonthIs(String testedValue, Integer valueYear) {
        card = DataGenerator.Payment.generateCard();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(testedValue);
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(String.valueOf(valueYear));
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        return this;
    }

    public PaymentPage fillingFieldsWhereYearIs(String testedValue) {
        card = DataGenerator.Payment.generateCard();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(testedValue);
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        return this;
    }

    public PaymentPage fillingFieldsWhereCardHolderIs(String testedValue) {
        card = DataGenerator.Payment.generateCard();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(testedValue);
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
        return this;
    }

    public PaymentPage fillingFieldsWhereSecurityCodeIs(String testedValue) {
        card = DataGenerator.Payment.generateCard();
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(testedValue);
        return this;
    }

    public PaymentPage checkingSubscriptionFieldCardNumber() {
        $(By.xpath(xpathFieldCardNumber + xpathInputFieldSub)).shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage checkingSubscriptionFieldMonth() {
        $(By.xpath(xpathFieldMonth + xpathInputFieldSub)).shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage checkingSubscriptionFieldYear() {
        $(By.xpath(xpathFieldYear + xpathInputFieldSub)).shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage checkingSubscriptionFieldCardHolder() {
        $(By.xpath(xpathFieldCardHolder + xpathInputFieldSub)).shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage checkingSubscriptionFieldSecurityCode() {
        $(By.xpath(xpathFieldSecurityCode + xpathInputFieldSub)).shouldBe(Condition.visible, Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
        return this;
    }

    public PaymentPage checkingButtonPayment() {
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
        return this;
    }
}



