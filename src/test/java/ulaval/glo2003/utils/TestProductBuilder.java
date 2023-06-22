package ulaval.glo2003.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.util.Precision;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class TestProductBuilder implements TestBuilder<Product> {

    private static final int PRECISION_SCALE = 2;

    private final int minTitleLength;
    private final int maxTitleLength;
    private final int minDescriptionLength;
    private final int maxDescriptionLength;
    private final int minProductCategoryCount;
    private final int maxProductCategoryCount;
    private List<Offer> offers;
    private final int minOfferCount;
    private final int maxOfferCount;
    private final int minViewCount;
    private final int maxViewCount;
    private final TestBuilder<Offer> testOfferBuilder;
    private double minSuggestedPrice;
    private double maxSuggestedPrice;
    private List<ProductCategory> excludedCategories;

    public TestProductBuilder() {
        this(new TestOfferBuilder());
    }

    public TestProductBuilder(final TestOfferBuilder testOfferBuilder) {
        this.minTitleLength = 10;
        this.maxTitleLength = 100;
        this.minDescriptionLength = 10;
        this.maxDescriptionLength = 100;
        this.minProductCategoryCount = 1;
        this.maxProductCategoryCount = 3;
        this.offers = null;
        this.minOfferCount = 5;
        this.maxOfferCount = 10;
        this.minViewCount = 0;
        this.maxViewCount = 100;
        this.testOfferBuilder = testOfferBuilder;
        this.minSuggestedPrice = Double.MIN_VALUE;
        this.maxSuggestedPrice = Double.MAX_VALUE;
        this.excludedCategories = new ArrayList<>();
    }

    public TestProductBuilder withSuggestedPriceBetween(final double minSuggestedPrice, final double maxSuggestedPrice) {
        if (minSuggestedPrice > maxSuggestedPrice) {
            throw new IllegalArgumentException(
                    "The minimum suggested price must be less or equal than the maximum suggested price.");
        }

        this.minSuggestedPrice = minSuggestedPrice;
        this.maxSuggestedPrice = maxSuggestedPrice;

        return this;
    }

    public TestProductBuilder withoutCategories(final List<ProductCategory> excludedCategories) {
        this.excludedCategories = excludedCategories;

        return this;
    }

    public TestProductBuilder withOffersSetTo(final List<Offer> offers) {
        this.offers = offers;

        return this;
    }

    @Override
    public Product build() {
        return new Product(
                generateTitle(),
                generateDescription(),
                generateSuggestedPrice(),
                generateCategories(),
                getOffers(),
                generateViews());
    }

    private String generateTitle() {
        return RandomStringUtils.randomAlphanumeric(this.minTitleLength, this.maxTitleLength);
    }

    private String generateDescription() {
        return RandomStringUtils.randomAlphanumeric(this.minDescriptionLength, this.maxDescriptionLength);
    }

    private double generateSuggestedPrice() {
        return Precision.round(RandomUtils.nextDouble(this.minSuggestedPrice, this.maxSuggestedPrice), PRECISION_SCALE);
    }

    private List<ProductCategory> generateCategories() {
        return TestProductCategoryFactory.createRandomProductCategories(
                this.minProductCategoryCount,
                this.maxProductCategoryCount,
                this.excludedCategories);
    }

    private List<Offer> getOffers() {
        if (this.offers != null) {
            return this.offers;
        }

        return TestOfferFactory.createRandomOffersUsingBuilder(
                this.minOfferCount,
                this.maxOfferCount,
                this.testOfferBuilder);
    }

    private int generateViews() {
        return RandomUtils.nextInt(this.minViewCount, this.maxViewCount);
    }
}
