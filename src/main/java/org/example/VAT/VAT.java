package org.example.VAT;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class VAT {
    private static final BigDecimal VAT_RATE_DIVISOR = BigDecimal.valueOf(6); // 120% / 20% = 6

    private final BigDecimal fullPriceWithVAT;
    private final BigDecimal priceWithoutVAT;
    private final BigDecimal vatInReceipt;
    private final int vatForDeclaration;
    /**
     * Конструктор для создания объекта VAT по известной полной цене с НДС.
     *
     * @param fullPriceWithVAT полная стоимость с НДС (в рублях).
     *                         Должна быть ≥ 0.
     * @throws IllegalArgumentException если передано отрицательное значение.
     */
    public VAT(double fullPriceWithVAT) {
        if (fullPriceWithVAT < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.fullPriceWithVAT = roundToTwoDecimals(fullPriceWithVAT);
        this.vatInReceipt = calculateVATFromFullPrice(this.fullPriceWithVAT);
        this.priceWithoutVAT = this.fullPriceWithVAT.subtract(vatInReceipt).setScale(2, RoundingMode.HALF_UP);
        this.vatForDeclaration = calculateVATForDeclaration(this.vatInReceipt);
    }

    /**
     * Конструктор для создания объекта VAT по известной базовой цене без НДС.
     *
     * <p>Второй параметр {@code isWithoutVAT} используется только для разрешения неоднозначности
     * конструкторов (перегрузки). Его значение должно быть {@code true}.
     *
     * @param priceWithoutVAT базовая стоимость без НДС (в рублях).
     *                        Должна быть ≥ 0.
     * @param isWithoutVAT флаг, подтверждающий, что передана цена *без* НДС.
     *                     Всегда должен быть {@code true}.
     * @throws IllegalArgumentException если передано отрицательное значение.
     */
    public VAT(double priceWithoutVAT, boolean isWithoutVAT) {
        if (isWithoutVAT == false) {

        }
        if (priceWithoutVAT < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.priceWithoutVAT = roundToTwoDecimals(priceWithoutVAT);
        this.vatInReceipt = this.priceWithoutVAT.multiply(BigDecimal.valueOf(0.2))
                .setScale(2, RoundingMode.HALF_UP);
        this.fullPriceWithVAT = this.priceWithoutVAT.add(vatInReceipt).setScale(2, RoundingMode.HALF_UP);
        this.vatForDeclaration = calculateVATForDeclaration(this.vatInReceipt);
    }

    private BigDecimal roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Рассчитывает сумму НДС из полной цены с НДС по формуле:
     * {@code НДС = полная_сумма / 6}.
     *
     * <p>Результат округляется до 2 знаков после запятой по правилу {@link RoundingMode#HALF_UP}.
     *
     * @param fullPrice полная цена с НДС.
     * @return сумма НДС в рублях (с двумя знаками после запятой).
     */
    private BigDecimal calculateVATFromFullPrice(BigDecimal fullPrice) {
        return fullPrice.divide(VAT_RATE_DIVISOR, 2, RoundingMode.HALF_UP);
    }
    /**
     * Преобразует сумму НДС (в рублях с копейками) в целое количество рублей для декларации.
     *
     * <p>Правило округления: копейки ≥ 50 → округление вверх (в пользу бюджета),
     * иначе — вниз (отбрасывание копеек).
     * Это соответствует п. 4 ст. 52 НК РФ для расчёта налога в целых рублях.
     *
     * <p>Примеры:
     * <ul>
     *   <li>{@code 12.49 → 12}</li>
     *   <li>{@code 12.50 → 13}</li>
     *   <li>{@code 12.99 → 13}</li>
     * </ul>
     *
     * @param vatAmount сумма НДС в рублях (с копейками).
     * @return сумма НДС в целых рублях для декларации.
     */
    private int calculateVATForDeclaration(BigDecimal vatAmount) {
        BigDecimal totalCents = vatAmount.multiply(BigDecimal.valueOf(100));
        int cents = totalCents.intValue();
        return (cents % 100 < 50) ? cents / 100 : cents / 100 + 1;
    }

    public BigDecimal getFullPriceWithVAT() { return fullPriceWithVAT; }
    public BigDecimal getPriceWithoutVAT() { return priceWithoutVAT; }
    public BigDecimal getVATInReceipt() { return vatInReceipt; }
    public int getVATForDeclaration() { return vatForDeclaration; }

    @Override
    public String toString() {
        return "VAT{" +
                "full=" + fullPriceWithVAT +
                ", without=" + priceWithoutVAT +
                ", vatReceipt=" + vatInReceipt +
                ", vatDecl=" + vatForDeclaration +
                '}';
    }
}