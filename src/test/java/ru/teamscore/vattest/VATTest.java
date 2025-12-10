package ru.teamscore.vattest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.teamscore.vat.VAT;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VATTest {


    @Test
    void fromFullPrice_withDouble_positiveAmount() {
        VAT vat = VAT.fromFullPrice(120.0);
        assertNotNull(vat);
        assertEquals(new BigDecimal("120.00"), vat.getFullPriceWithVAT());
    }

    @Test
    void fromFullPrice_withDouble_negativeAmount_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> VAT.fromFullPrice(-10.0)
        );
        assertEquals("Сумма не может быть отрицательной", exception.getMessage());
    }

    @Test
    void fromFullPrice_withBigDecimal_positiveAmount() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.00"));
        assertNotNull(vat);
        assertEquals(new BigDecimal("120.00"), vat.getFullPriceWithVAT());
    }

    @Test
    void fromFullPrice_withBigDecimal_negativeAmount_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> VAT.fromFullPrice(new BigDecimal("-10.00"))
        );
        assertTrue(exception.getMessage().contains("Сумма не может быть отрицательной"));
    }

    @Test
    void fromFullPrice_withBigDecimal_zeroAmount() {
        VAT vat = VAT.fromFullPrice(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, vat.getFullPriceWithVAT().setScale(0));
        assertEquals(BigDecimal.ZERO, vat.getPriceWithoutVAT().setScale(0));
        assertEquals(BigDecimal.ZERO, vat.getVATInReceipt().setScale(0));
        assertEquals(0, vat.getVATForDeclaration());
    }

    @Test
    void fromPriceWithoutVAT_withDouble_positiveAmount() {
        VAT vat = VAT.fromPriceWithoutVAT(100.0);
        assertNotNull(vat);
        assertEquals(new BigDecimal("100.00"), vat.getPriceWithoutVAT());
    }

    @Test
    void fromPriceWithoutVAT_withDouble_negativeAmount_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> VAT.fromPriceWithoutVAT(-50.0)
        );
        assertEquals("Сумма не может быть отрицательной", exception.getMessage());
    }

    @Test
    void fromPriceWithoutVAT_withBigDecimal_positiveAmount() {
        VAT vat = VAT.fromPriceWithoutVAT(new BigDecimal("100.00"));
        assertNotNull(vat);
        assertEquals(new BigDecimal("100.00"), vat.getPriceWithoutVAT());
    }

    @Test
    void fromPriceWithoutVAT_withBigDecimal_negativeAmount_throwsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> VAT.fromPriceWithoutVAT(new BigDecimal("-50.00"))
        );
        assertTrue(exception.getMessage().contains("Сумма не может быть отрицательной"));
    }

    @Test
    void fromPriceWithoutVAT_withBigDecimal_zeroAmount() {
        VAT vat = VAT.fromPriceWithoutVAT(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, vat.getFullPriceWithVAT().setScale(0));
        assertEquals(BigDecimal.ZERO, vat.getPriceWithoutVAT().setScale(0));
        assertEquals(BigDecimal.ZERO, vat.getVATInReceipt().setScale(0));
        assertEquals(0, vat.getVATForDeclaration());
    }

    @ParameterizedTest
    @CsvSource({
            // fullPriceWithVAT, expectedNet, expectedVAT
            "120.00, 100.00, 20.00",
            "60.00, 50.00, 10.00",
            "0.00, 0.00, 0.00",
            "1.20, 1.00, 0.20",
            "119.99, 99.99, 20.00",
            "120.01, 100.01, 20.00",
            "240.00, 200.00, 40.00"
    })
    void fromFullPrice_calculationsCorrect(String fullPrice, String expectedNet, String expectedVAT) {
        VAT vat = VAT.fromFullPrice(new BigDecimal(fullPrice));

        assertEquals(new BigDecimal(expectedNet), vat.getPriceWithoutVAT(),
                "Некорректная сумма без НДС для полной суммы " + fullPrice);

        assertEquals(new BigDecimal(expectedVAT), vat.getVATInReceipt(),
                "Некорректный НДС для полной суммы " + fullPrice);
    }

    @ParameterizedTest
    @CsvSource({
            // priceWithoutVAT, expectedGross, expectedVAT
            "100.00, 120.00, 20.00",
            "50.00, 60.00, 10.00",
            "0.00, 0.00, 0.00",
            "1.00, 1.20, 0.20",
            "99.99, 119.99, 20.00",
            "100.01, 120.01, 20.00",
            "200.00, 240.00, 40.00"
    })
    void fromPriceWithoutVAT_calculationsCorrect(String netPrice, String expectedGross, String expectedVAT) {
        VAT vat = VAT.fromPriceWithoutVAT(new BigDecimal(netPrice));

        assertEquals(new BigDecimal(expectedGross), vat.getFullPriceWithVAT(),
                "Некорректная сумма с НДС для суммы без НДС " + netPrice);

        assertEquals(new BigDecimal(expectedVAT), vat.getVATInReceipt(),
                "Некорректный НДС для суммы без НДС " + netPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "0.00, 0",
            "20.00, 20",
            "20.49, 20",
            "20.50, 21",
            "20.99, 21",
            "100.01, 100",
            "100.50, 101",
            "100.99, 101",
            "1234.49, 1234",
            "1234.50, 1235",
            "1234.99, 1235"
    })
    void vatForDeclaration_roundingCorrect(String vatAmount, int expectedRoundedVAT) {
        // Создаем объект VAT через конструктор для тестирования округления
        BigDecimal full = new BigDecimal("600.00"); // произвольное значение
        BigDecimal net = full.subtract(new BigDecimal(vatAmount));
        BigDecimal vat = new BigDecimal(vatAmount);

        VAT vatObj = new VAT(full, net, vat);

        assertEquals(expectedRoundedVAT, vatObj.getVATForDeclaration(),
                "Некорректное округление для НДС " + vatAmount);
    }

    // Проверяем, что округление работает корректно при создании через разные фабричные методы
    @Test
    void vatForDeclaration_fromFullPrice_withRoundingCases() {
        // Случай с 49 копейками
        VAT vat1 = VAT.fromFullPrice(new BigDecimal("122.45")); // НДС = 20.4083 → 20.41 → 20
        assertEquals(20, vat1.getVATForDeclaration());

        // Случай с 50 копейками
        VAT vat2 = VAT.fromFullPrice(new BigDecimal("122.50")); // НДС = 20.4167 → 20.42 → 21
        assertEquals(20, vat2.getVATForDeclaration());

        // Случай с 99 копейками
        VAT vat3 = VAT.fromFullPrice(new BigDecimal("122.99")); // НДС = 20.4983 → 20.50 → 21
        assertEquals(21, vat3.getVATForDeclaration());
    }

    @Test
    void fromFullPrice_roundsToTwoDecimals() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.005")); // 120.005 → округляется до 120.01
        assertEquals(new BigDecimal("120.01"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("100.01"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("20.00"), vat.getVATInReceipt());
    }

    @Test
    void fromPriceWithoutVAT_roundsToTwoDecimals() {
        VAT vat = VAT.fromPriceWithoutVAT(new BigDecimal("100.005")); // 100.005 → 100.01
        assertEquals(new BigDecimal("100.01"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("120.01"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("20.00"), vat.getVATInReceipt());
    }

    @Test
    void equals_sameValues_returnsTrue() {
        VAT vat1 = VAT.fromFullPrice(new BigDecimal("120.00"));
        VAT vat2 = VAT.fromFullPrice(new BigDecimal("120.00"));

        assertEquals(vat1, vat2);
        assertEquals(vat1.hashCode(), vat2.hashCode());
    }

    @Test
    void equals_differentValues_returnsFalse() {
        VAT vat1 = VAT.fromFullPrice(new BigDecimal("120.00"));
        VAT vat2 = VAT.fromFullPrice(new BigDecimal("121.00"));

        assertNotEquals(vat1, vat2);
    }

    @Test
    void equals_differentType_returnsFalse() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.00"));
        assertNotEquals(vat, new Object());
    }

    @Test
    void equals_null_returnsFalse() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.00"));
        assertNotEquals(null, vat);
    }

    @Test
    void equals_sameObject_returnsTrue() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.00"));
        assertEquals(vat, vat);
    }

    @Test
    void toString_containsAllFields() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.00"));
        String result = vat.toString();

        assertTrue(result.contains("сНДС=120.00"));
        assertTrue(result.contains("безНДС=100.00"));
        assertTrue(result.contains("НДСвЧеке=20.00"));
        assertTrue(result.contains("НДСвДекл=20"));
    }

    @Test
    void verySmallAmounts() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("0.01"));
        // Проверяем, что нет исключений и значения корректны
        assertTrue(vat.getFullPriceWithVAT().compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(vat.getPriceWithoutVAT().compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(vat.getVATInReceipt().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void veryLargeAmounts() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        VAT vat = VAT.fromFullPrice(largeAmount);

        // Проверяем, что расчеты выполнены без переполнения
        assertTrue(vat.getPriceWithoutVAT().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(vat.getVATInReceipt().compareTo(BigDecimal.ZERO) > 0);

        // Проверяем, что сумма с НДС = сумма без НДС + НДС
        BigDecimal sum = vat.getPriceWithoutVAT().add(vat.getVATInReceipt());
        assertEquals(0, sum.compareTo(vat.getFullPriceWithVAT()));
    }

    @Test
    void consistency_betweenFactoryMethods() {
        // Если создать объект с суммой без НДС, а затем создать другой объект
        // с полученной полной суммой, результаты должны быть согласованы

        BigDecimal netPrice = new BigDecimal("100.00");
        VAT vatFromNet = VAT.fromPriceWithoutVAT(netPrice);

        BigDecimal grossPrice = vatFromNet.getFullPriceWithVAT();
        VAT vatFromGross = VAT.fromFullPrice(grossPrice);

        // Сравниваем с допуском на округление
        assertEquals(0, vatFromNet.getPriceWithoutVAT().compareTo(vatFromGross.getPriceWithoutVAT()));
        assertEquals(0, vatFromNet.getVATInReceipt().compareTo(vatFromGross.getVATInReceipt()));
        assertEquals(vatFromNet.getVATForDeclaration(), vatFromGross.getVATForDeclaration());
    }

    @Test
    void relationship_betweenAmounts() {
        VAT vat = VAT.fromFullPrice(new BigDecimal("120.00"));

        // Проверяем основное соотношение: full = net + vat
        BigDecimal calculatedFull = vat.getPriceWithoutVAT().add(vat.getVATInReceipt());
        assertEquals(0, calculatedFull.compareTo(vat.getFullPriceWithVAT()));

        // Проверяем, что НДС = net * 0.20
        BigDecimal calculatedVAT = vat.getPriceWithoutVAT().multiply(new BigDecimal("0.20"))
                .setScale(2, java.math.RoundingMode.HALF_UP);
        assertEquals(0, calculatedVAT.compareTo(vat.getVATInReceipt()));
    }
}