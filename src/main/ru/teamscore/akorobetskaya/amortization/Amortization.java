package ru.teamscore.akorobetskaya.amortization;

/**
 * <h1>Расчет амортизации</h1>
 * Амортизация - постепенное списание стоимости основных средств
 * (инструменты, оборудование, здания и т.п.) в связи с их
 * износом и устареванием.
 * Амортизация списывается раз в месяц и уменьшает первоначальную стоимость.
 * Методов расчета амортизации несколько.
 */
public abstract class Amortization {
    protected final double startValue; // первоначальная стоимость
    protected double depricatedValue; // остаточная стоимость
    private int month = 0; // месяц

    public Amortization(double startValue) {
        this.startValue = startValue;
        depricatedValue = startValue;
    }

    /**
     * Начальная стоимость объекта.
     */
    public double getStartValue() {
        return startValue;
    }

    /**
     * Остаточная стоимость объекта на текущий месяц.
     */
    public double getDepricatedValue() {
        return depricatedValue;
    }

    /**
     * Текущий месяц.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Сумма амортизационных отчислений за текущий месяц без округления до копеек.
     */
    protected abstract double getNextAmortizationNotRounded();

    /**
     * Сумма амортизационных отчислений за текущий месяц с округлением до копеек.
     */
    public double getNextAmortization() {
        if (getNextAmortizationNotRounded() > depricatedValue) {
            return depricatedValue;
        }
        return Math.round(getNextAmortizationNotRounded() * 100) / 100.0;
    }


    /**
     * Общая сумма амортизации за весь период.
     */
    public final double getTotalAmortization() {
        return startValue - depricatedValue;
    }

    /**
     * Списать амортизацию за текущий месяц.
     */
    public void deprecate() {
        month++;
        depricatedValue -= getNextAmortization();
    }
}
