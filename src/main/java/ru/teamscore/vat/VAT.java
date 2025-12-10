package ru.teamscore.vat;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class VAT {

    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(20, 2); // 20% → 0.20
    private static final BigDecimal VAT_INCLUSIVE_FACTOR = BigDecimal.ONE.add(VAT_RATE); // 1.20
    private final BigDecimal fullPriceWithVAT;   // полная сумма с НДС (копейки сохраняются)
    private final BigDecimal priceWithoutVAT;    // базовая сумма без НДС (копейки сохраняются)
    private final BigDecimal vatInReceipt;       // НДС в чеке/счете — с копейками
    private final int vatForDeclaration;         // НДС для декларации — в целых рублях

    public VAT(BigDecimal full, BigDecimal net, BigDecimal vat) {
        this.fullPriceWithVAT = full;
        this.priceWithoutVAT = net;
        this.vatInReceipt = vat;
        this.vatForDeclaration = roundVatToRublesForDeclaration(vat);
    }
    /**
     * Создаёт объект НДС по полной стоимости (включая НДС)
     *
     * @param fullPriceWithVAT полная стоимость с НДС (≥ 0)
     * @return новый объект {@link VAT}
     * @throws IllegalArgumentException если сумма отрицательна
     */
    public static VAT fromFullPrice(double fullPriceWithVAT) {
        if (fullPriceWithVAT < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной");
        }
        return fromFullPrice(BigDecimal.valueOf(fullPriceWithVAT));
    }

    /**
     * Создаёт объект НДС по полной стоимости (включая НДС), как указано в чеке/счете-фактуре.
     */
    public static VAT fromFullPrice(BigDecimal fullPriceWithVAT) {
        validateNonNegative(fullPriceWithVAT);
        BigDecimal roundedFull = fullPriceWithVAT.setScale(2, RoundingMode.HALF_UP);
        BigDecimal vat = roundedFull.multiply(VAT_RATE)
                .divide(VAT_INCLUSIVE_FACTOR, 2, RoundingMode.HALF_UP);
        BigDecimal net = roundedFull.subtract(vat).setScale(2, RoundingMode.HALF_UP);
        return new VAT(roundedFull, net, vat);
    }

    /**
     * Создаёт объект НДС по базовой стоимости (без НДС).
     *
     * @param priceWithoutVAT базовая стоимость без НДС (≥ 0)
     * @return новый объект {@link VAT}
     * @throws IllegalArgumentException если сумма отрицательна
     */
    public static VAT fromPriceWithoutVAT(double priceWithoutVAT) {
        if (priceWithoutVAT < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной");
        }
        return fromPriceWithoutVAT(BigDecimal.valueOf(priceWithoutVAT));
    }

    /**
     * Создаёт объект НДС по базовой стоимости (без НДС).
     */
    public static VAT fromPriceWithoutVAT(BigDecimal priceWithoutVAT) {
        validateNonNegative(priceWithoutVAT);
        BigDecimal roundedNet = priceWithoutVAT.setScale(2, RoundingMode.HALF_UP);
        BigDecimal vat = roundedNet.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal gross = roundedNet.add(vat).setScale(2, RoundingMode.HALF_UP);
        return new VAT(gross, roundedNet, vat);
    }

    private static void validateNonNegative(BigDecimal amount) {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + amount);
        }
    }

    /**
     * Округляет сумму НДС до целых рублей по правилам налоговой декларации:
     * - менее 50 копеек → отбрасываются,
     * - 50 копеек и более → округляются вверх до полного рубля.
     *
     * Примеры:
     *   12.49 → 12
     *   12.50 → 13
     *   12.99 → 13
     */
    private static int roundVatToRublesForDeclaration(BigDecimal vatAmount) {
        // Переводим в копейки и работаем с целыми числами — надёжно
        long totalKopecks = vatAmount.multiply(BigDecimal.valueOf(100)).longValue();
        long rubles = totalKopecks / 100;
        long kopecks = totalKopecks % 100;
        return (kopecks < 50) ? (int) rubles : (int) rubles + 1;
    }

    public BigDecimal getFullPriceWithVAT() { return fullPriceWithVAT; }
    public BigDecimal getPriceWithoutVAT() { return priceWithoutVAT; }
    public BigDecimal getVATInReceipt() { return vatInReceipt; }
    public int getVATForDeclaration() { return vatForDeclaration; }

    @Override
    public String toString() {
        return "VAT{" +
                "сНДС=" + fullPriceWithVAT +
                ", безНДС=" + priceWithoutVAT +
                ", НДСвЧеке=" + vatInReceipt +
                ", НДСвДекл=" + vatForDeclaration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VAT vat)) return false;
        return vatForDeclaration == vat.vatForDeclaration &&
                fullPriceWithVAT.equals(vat.fullPriceWithVAT) &&
                priceWithoutVAT.equals(vat.priceWithoutVAT) &&
                vatInReceipt.equals(vat.vatInReceipt);
    }

    @Override
    public int hashCode() {
        return fullPriceWithVAT.hashCode() ^ priceWithoutVAT.hashCode() ^ vatInReceipt.hashCode();
    }
}