package ru.netology.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import lombok.val;
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

    public void buttonSelectFormPayment() {
        $$("button").find(exactText("Купить")).click();
        $$("h3").find(exactText("Оплата по карте")).shouldBe(exist, Duration.ofSeconds(0));
    }

    public void buttonSelectFormCredit() {
        $$("button").find(exactText("Купить в кредит")).click();
        $$("h3").find(exactText("Кредит по данным карты")).shouldBe(exist, Duration.ofSeconds(0));
    }

    public void buttonPayment() {
        $$("button").find(exactText("Продолжить")).click();
    }

    public void checkingButtonPayment() {
        $$("button").find(exactText("Отправляем запрос в Банк...")).shouldBe(not(exist), Duration.ofSeconds(0));
    }

    public void checkApprovedNotification() {
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Операция одобрена Банком."), Duration.ofSeconds(0));
    }

    public void checkDeclinedNotification() {
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(0));
    }

    public void fillingFields(DataGenerator.CardInfo card) {
        $(By.xpath(xpathFieldCardNumber + xpathInputField)).setValue(card.getCardNumber());
        $(By.xpath(xpathFieldMonth + xpathInputField)).setValue(card.getMonth());
        $(By.xpath(xpathFieldYear + xpathInputField)).setValue(card.getYear());
        $(By.xpath(xpathFieldCardHolder + xpathInputField)).setValue(card.getCardHolder());
        $(By.xpath(xpathFieldSecurityCode + xpathInputField)).setValue(card.getSecurityCode());
    }

    public void checkingSubscriptionFieldCardNumber(String subscription) {
        $(By.xpath(xpathFieldCardNumber + xpathInputFieldSub))
                .shouldBe(Condition.visible, Duration.ofSeconds(0))
                .shouldHave(Condition.text(subscription), Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
    }

    public void checkingSubscriptionFieldMonth(String subscription) {
        $(By.xpath(xpathFieldMonth + xpathInputFieldSub))
                .shouldBe(Condition.visible, Duration.ofSeconds(0))
                .shouldHave(Condition.text(subscription), Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
    }

    public void checkingSubscriptionFieldYear(String subscription) {
        $(By.xpath(xpathFieldYear + xpathInputFieldSub))
                .shouldBe(Condition.visible, Duration.ofSeconds(0))
                .shouldHave(Condition.text(subscription), Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
    }

    public void checkingSubscriptionFieldCardHolder(String subscription) {
        $(By.xpath(xpathFieldCardHolder + xpathInputFieldSub))
                .shouldBe(Condition.visible, Duration.ofSeconds(0))
                .shouldHave(Condition.text(subscription), Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
    }

    public void checkingSubscriptionFieldSecurityCode(String subscription) {
        $(By.xpath(xpathFieldSecurityCode + xpathInputFieldSub))
                .shouldBe(Condition.visible, Duration.ofSeconds(0))
                .shouldHave(Condition.text(subscription), Duration.ofSeconds(0));
        $$(By.xpath(xpathInputFieldSub)).should(CollectionCondition.size(1), Duration.ofSeconds(0));
    }
}



