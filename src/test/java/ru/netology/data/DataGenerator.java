package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    static Faker faker = new Faker(new Locale("en"));
    static int currentYear = Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yy")));
    static String randomYear = Integer.toString(faker.number().numberBetween(currentYear, currentYear + 5));

    private DataGenerator() {

    }

    public static String generateCardNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        String cardNumber = Long.toString(faker.number().randomNumber(16, true));
        return cardNumber;
    }

    public static String generateMonth() {
        String month;
        var random = new SecureRandom();
        List<String> enableMonth = List.of("12", "11", "10", "09", "08", "07", "06", "05", "04", "03", "02", "01");
        if (Integer.valueOf(randomYear) == currentYear) {
            month = enableMonth.get(random.nextInt(enableMonth.size() - Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))) + 1));
        } else {
            month = enableMonth.get(random.nextInt(enableMonth.size()));
        }
        return month;
    }

    public static String generateYear() {
        //int currentYear = Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yy")));
        //String year = Integer.toString(faker.number().numberBetween(currentYear,currentYear+5));
        String year = randomYear;
        return year;
    }

    public static String generateCardHolder() {
        String cardHolder = faker.name().firstName() + " " + faker.name().lastName();
        return cardHolder;
    }

    public static String generateSecurityCode() {
        String securityCode = Long.toString(faker.number().randomNumber(3, true));
        return securityCode;
    }

    public static String generateCardHolderCyrillic() {
        char[] alphabetA = new String("абвгдеёжзийклмнопрстуфхцчшщъыьэюя").toCharArray();
        String cardHolderCyrillic = String.valueOf(alphabetA[ThreadLocalRandom.current().nextInt(0, alphabetA.length - 1)]);
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
