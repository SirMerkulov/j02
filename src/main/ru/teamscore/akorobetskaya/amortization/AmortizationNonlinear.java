package ru.teamscore.akorobetskaya.amortization;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <h1>Нелинейная амортизация</h1>
 * Рассчитывается как процент от остаточной стоимости и
 * убывает со временем.
 * Если остаточная стоимость менее 20 000 руб., то списывается
 * полностью.
 */
public final class AmortizationNonlinear extends Amortization {
    public final static double MIN_DEPRECATION_VALUE = 20_000; // остаточная стоимость для полного списания
    private final static BigDecimal MIN_DEPRECATION_VALUE_DECIMAL = BigDecimal.valueOf(MIN_DEPRECATION_VALUE);
    private final BigDecimal amortizationNorm; // норма амортизации


    public AmortizationNonlinear(double startValue, double amortizationNorm) {
        super(startValue);
        this.amortizationNorm = BigDecimal.valueOf(amortizationNorm);
    }

    /**
     * Норма амортизации в процентах (0-100%)
     */
    public double getAmortizationNorm() {
        return amortizationNorm.doubleValue();
    }

    /**
     * Амортизация за текущий месяц без округления по нелинейному методу
     */
    @Override
    protected double getNextAmortizationNotRounded() {
        // TODO
        return 0;
    }

}
