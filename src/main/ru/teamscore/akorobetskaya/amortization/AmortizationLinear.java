package ru.teamscore.akorobetskaya.amortization;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <h1>Линейная амортизация</h1>
 * Для амортизируемого объекта задается срок службы в месяцах.
 * Каждый месяц списывается равная доля от первоначальной стоимости.
 * В конце срока службы стоимость объекта должна быть полностью списана.
 */
public final class AmortizationLinear extends Amortization {
    public AmortizationLinear(double startValue) {
        // TODO
        super(startValue);
    }

    @Override
    protected double getNextAmortizationNotRounded() {
        // TODO
        return 0;
    }
}
