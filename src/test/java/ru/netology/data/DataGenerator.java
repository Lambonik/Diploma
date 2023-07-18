package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    static Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {

    }

    public static String generateCardNumber() {
        String cardNumber = Long.toString(faker.number().randomNumber(16,true));
        return cardNumber;
    }

    public static String generateMonth() {
        var random = new SecureRandom();
        List<String> enableCities = List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        String month = enableCities.get(random.nextInt(enableCities.size()));
        return month;
    }

    public static String generateYear() {
        int currentYear = Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yy")));
        String year = Integer.toString(faker.number().numberBetween(currentYear,currentYear+5));
        return year;
    }

    public static String generateCardHolder() {
        String cardHolder = faker.name().firstName()+" "+faker.name().lastName();
        return cardHolder;
    }

    public static String generateSecurityCode() {
        String securityCode = Long.toString(faker.number().randomNumber(3,true));
        return securityCode;
    }

    public static String generateCardHolderCyrillic() {
        char[] alphabetA = new String("абвгдеёжзийклмнопрстуфхцчшщъыьэюя").toCharArray();
        String cardHolderCyrillic= String.valueOf(alphabetA[ThreadLocalRandom.current().nextInt(0, alphabetA.length-1)]);
        return cardHolderCyrillic;
    }

    public static class Payment {
        private Payment() {
        }

        public static CardInfo generateCard() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), generateCardHolder(), generateSecurityCode(), generateCardHolderCyrillic());
            return card;
        }
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String cardHolder;
        String securityCode;
        String cardHolderCyrillic;
    }
}
