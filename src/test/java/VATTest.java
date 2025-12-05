import org.example.VAT.VAT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VATTest {

    @Test
    @DisplayName("Полная стоимость с НДС = 1200.00")
    void testFullPrice1200() {
        VAT vat = new VAT(1200.00);

        assertEquals(new BigDecimal("1200.00"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("1000.00"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("200.00"), vat.getVATInReceipt());
        assertEquals(200, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 500000.00")
    void testFullPrice500000() {
        VAT vat = new VAT(500000.00);

        assertEquals(new BigDecimal("500000.00"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("416666.67"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("83333.33"), vat.getVATInReceipt());
        assertEquals(83333, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 119.99")
    void testFullPrice119_99() {
        VAT vat = new VAT(119.99);

        assertEquals(new BigDecimal("119.99"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("99.99"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("20.00"), vat.getVATInReceipt());
        assertEquals(20, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 99.99")
    void testFullPrice99_99() {
        VAT vat = new VAT(99.99);

        assertEquals(new BigDecimal("99.99"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("83.32"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("16.67"), vat.getVATInReceipt());
        assertEquals(17, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 92.40")
    void testFullPrice92_40() {
        VAT vat = new VAT(92.40);

        assertEquals(new BigDecimal("92.40"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("77.00"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("15.40"), vat.getVATInReceipt());
        assertEquals(15, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 183.00")
    void testFullPrice183_00() {
        VAT vat = new VAT(183.00);

        assertEquals(new BigDecimal("183.00"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("152.50"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("30.50"), vat.getVATInReceipt());
        assertEquals(31, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 5.94")
    void testFullPrice5_94() {
        VAT vat = new VAT(5.94);

        assertEquals(new BigDecimal("5.94"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("4.95"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("0.99"), vat.getVATInReceipt());
        assertEquals(1, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 0.06")
    void testFullPrice0_06() {
        VAT vat = new VAT(0.06);

        assertEquals(new BigDecimal("0.06"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("0.05"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("0.01"), vat.getVATInReceipt());
        assertEquals(0, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 0.01")
    void testFullPrice0_01() {
        VAT vat = new VAT(0.01);

        assertEquals(new BigDecimal("0.01"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("0.01"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("0.00"), vat.getVATInReceipt());
        assertEquals(0, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = 0.00")
    void testFullPrice0_00() {
        VAT vat = new VAT(0.00);

        assertEquals(new BigDecimal("0.00"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("0.00"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("0.00"), vat.getVATInReceipt());
        assertEquals(0, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Полная стоимость с НДС = -0.01 — должно выбросить исключение")
    void testNegativePriceThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VAT(-0.01);
        });
    }

    @Test
    @DisplayName("Создание через цену без НДС = 1000.00")
    void testCreateFromPriceWithoutVAT() {
        VAT vat = new VAT(1000.00, true); // true означает, что передано значение без НДС

        assertEquals(new BigDecimal("1200.00"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("1000.00"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("200.00"), vat.getVATInReceipt());
        assertEquals(200, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Создание через цену без НДС = 0.00")
    void testZeroPriceWithoutVAT() {
        VAT vat = new VAT(0.00, true);

        assertEquals(new BigDecimal("0.00"), vat.getFullPriceWithVAT());
        assertEquals(new BigDecimal("0.00"), vat.getPriceWithoutVAT());
        assertEquals(new BigDecimal("0.00"), vat.getVATInReceipt());
        assertEquals(0, vat.getVATForDeclaration());
    }

    @Test
    @DisplayName("Создание через цену без НДС = -1.0 — должно выбросить исключение")
    void testNegativePriceWithoutVATThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new VAT(-1.0, true);
        });
    }
}