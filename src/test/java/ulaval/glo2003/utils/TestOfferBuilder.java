package ulaval.glo2003.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.util.Precision;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;

public class TestOfferBuilder implements TestBuilder<Offer> {

    private static final int PRECISION_SCALE = 2;

    private final int minMessageLength;
    private final int maxMessageLength;
    private double minAmount;
    private double maxAmount;
    private final TestBuyerBuilder testBuyerBuilder;

    public TestOfferBuilder() {
        this(new TestBuyerBuilder());
    }

    public TestOfferBuilder(final TestBuyerBuilder testBuyerBuilder) {
        this.minMessageLength = 100;
        this.maxMessageLength = 200;
        this.minAmount = Double.MIN_VALUE;
        this.maxAmount = Double.MAX_VALUE;
        this.testBuyerBuilder = testBuyerBuilder;
    }

    public TestOfferBuilder withAmountBetween(final double minAmount, final double maxAmount) {
        if (minAmount > maxAmount) {
            throw new IllegalArgumentException(
                    "The minimum amount must be less or equal than the maximum amount.");
        }

        this.minAmount = minAmount;
        this.maxAmount = maxAmount;

        return this;
    }

    @Override
    public Offer build() {
        return new Offer(generateOfferedPrice(), generateMessage(), generateBuyer());
    }

    private double generateOfferedPrice() {
        return Precision.round(RandomUtils.nextDouble(this.minAmount, this.maxAmount), PRECISION_SCALE);
    }

    private String generateMessage() {
        return RandomStringUtils.randomAlphanumeric(this.minMessageLength, this.maxMessageLength);
    }

    private Buyer generateBuyer() {
        return testBuyerBuilder.build();
    }
}
