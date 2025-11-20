package ru.teamscore.akorobetskaya.amortization;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmortizationTest {

    class AmortizationMock extends Amortization {
        private final double nextAmortization;
        public AmortizationMock(double startValue, double nextAmortization) {
            super(startValue);
            this.nextAmortization = nextAmortization;
        }
        @Override
        protected double getNextAmortizationNotRounded() {
            return nextAmortization;
        }
    }

    @Test
    void initialization() {
        final double startValue = 1000;
        var amortization = new AmortizationMock(startValue, 0);
        assertEquals(startValue, amortization.getStartValue());
        assertEquals(startValue, amortization.getDepricatedValue());
        assertEquals(0, amortization.getMonth());
    }

    @Test
    void getNextAmortizationRoundUp() {
        var amortization = new AmortizationMock(1000, 99.99999);
        assertEquals(100.00, amortization.getNextAmortization());
    }

    @Test
    void getNextAmortizationRoundHalfUp() {
        var amortization = new AmortizationMock(1000, 1.015);
        assertEquals(1.02, amortization.getNextAmortization());
    }

    @Test
    void getNextAmortizationRoundDown() {
        var amortization = new AmortizationMock(1000, 11.1101);
        assertEquals(11.11, amortization.getNextAmortization());
    }

    @Test
    void getTotalAmortization() {
        var amortization = new AmortizationMock(2.5, 1);
        assertEquals(0, amortization.getTotalAmortization());
        amortization.deprecate();
        assertEquals(1, amortization.getTotalAmortization());
        amortization.deprecate();
        assertEquals(2, amortization.getTotalAmortization());
        amortization.deprecate();
        assertEquals(2.5, amortization.getTotalAmortization());
        amortization.deprecate();
        assertEquals(2.5, amortization.getTotalAmortization());
    }

    @Test
    void deprecate() {
        double startValue = 4.1;
        double amortizationValue = 1;
        var amortization = new AmortizationMock(startValue, amortizationValue);
        assertEquals(startValue, amortization.getDepricatedValue());
        assertEquals(0, amortization.getMonth());
        for (int i = 1; startValue >= i * amortizationValue; i++) {
            amortization.deprecate();
            assertEquals(startValue - i * amortizationValue, amortization.getDepricatedValue());
            assertEquals(i, amortization.getMonth());
        }
        amortization.deprecate();
        assertEquals(0, amortization.getDepricatedValue());
    }
}