package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;


public class DataGenerator {
    public static class Query {
        public Query() {
        }

        @SneakyThrows
        public static String queryPayment(String paymentType) {
            String url = System.getProperty("spring.datasource.url");
            String statusSQL = null;
            if (paymentType == "Payment") {
                statusSQL = "SELECT * FROM payment_entity WHERE id=LAST_INSERT_ID();";
            } else if (paymentType == "Credit") {
                statusSQL = "SELECT * FROM credit_request_entity WHERE id=LAST_INSERT_ID();";
            }
            var status = "0";
            var connection = DriverManager.getConnection(url, "app", "pass");
            var statusStmt = connection.prepareStatement(statusSQL);
            var rs = statusStmt.executeQuery();
            while (rs.next()) {
                status = rs.getString("status");
            }
            return status;
        }
    }

    static Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    public static String generateCardNumber() {
        String cardNumber = Long.toString(faker.number().randomNumber(16, true));
        return cardNumber;
    }

    public static String generateMonth() {
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
        return month;
    }

    public static String generateYear() {
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
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
        Faker faker = new Faker(new Locale("ru"));
        String cardHolderCyrillic = faker.name().firstName();
        return cardHolderCyrillic;
    }

    public static String generateApprovedCardNumber() {
        String approvedCardNumber = "4444 4444 4444 4441";
        return approvedCardNumber;
    }

    public static String generateDeclinedCardNumber() {
        String declinedCardNumber = "4444 4444 4444 4442";
        return declinedCardNumber;
    }

    public static String generateMountLessThanCurrent() {
        String mountLessThanCurrent = LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(1, LocalDate.now().getMonthValue() - 1)).format(DateTimeFormatter.ofPattern("MM"));
        return mountLessThanCurrent;
    }

    public static String generateYearLessThanCurrent() {
        String yearLessThanCurrent = LocalDate.now().minusYears(ThreadLocalRandom.current().nextInt(1, Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY"))) - 1)).format(DateTimeFormatter.ofPattern("YY"));
        return yearLessThanCurrent;
    }

    public static String generateYearMoreThanCurrentBy5() {
        String yearMoreThanCurrentBy5 = String.valueOf(ThreadLocalRandom.current().nextInt(Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("YY"))) + 6, 99));
        return yearMoreThanCurrentBy5;
    }

    public static String generateDigit() {
        String digit = String.valueOf(ThreadLocalRandom.current().nextInt(0, 9));
        return digit;
    }

    public static class Payment {
        private Payment() {
        }

        public static CardInfo generateCardWithCardNumberIsEmpty() {
            CardInfo card = new CardInfo(null, generateMonth(), generateYear(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithMonthIsEmpty() {
            CardInfo card = new CardInfo(generateCardNumber(), null, generateYear(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithYearIsEmpty() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), null, generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithCardHolderIsEmpty() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), null, generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithSecurityCodeIsEmpty() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), generateCardHolder(), null);
            return card;
        }

        public static CardInfo generateCardWithCardHolderCyrillic() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), generateCardHolderCyrillic(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithApprovedCardNumber() {
            CardInfo card = new CardInfo(generateApprovedCardNumber(), generateMonth(), generateYear(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithDeclinedCardNumber() {
            CardInfo card = new CardInfo(generateDeclinedCardNumber(), generateMonth(), generateYear(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithMountLessThanCurrent() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMountLessThanCurrent(), generateYear(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithYearLessThanCurrent() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYearLessThanCurrent(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithYearMoreThanCurrentBy5() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYearMoreThanCurrentBy5(), generateCardHolder(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithCardHolderDigit() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), generateDigit(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithCardNumberIs15Digits() {
            CardInfo card = new CardInfo(generateCardNumber().substring(1), generateMonth(), generateYear(), generateDigit(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithCardNumberIs1Digit() {
            CardInfo card = new CardInfo(generateCardNumber().substring(15), generateMonth(), generateYear(), generateDigit(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithMonthValue_00() {
            CardInfo card = new CardInfo(generateCardNumber(), "00", generateYear(), generateDigit(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithMonthValue_13() {
            CardInfo card = new CardInfo(generateCardNumber(), "13", generateYear(), generateDigit(), generateSecurityCode());
            return card;
        }

        public static CardInfo generateCardWithSecurityCodeIs1Digit() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), generateDigit(), generateSecurityCode().substring(2));
            return card;
        }

        public static CardInfo generateCardWithSecurityCodeIs2Digits() {
            CardInfo card = new CardInfo(generateCardNumber(), generateMonth(), generateYear(), generateDigit(), generateSecurityCode().substring(1));
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
    }
}
