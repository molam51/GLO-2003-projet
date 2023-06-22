package ulaval.glo2003.utils;

import org.apache.commons.lang3.RandomStringUtils;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.domain.seller.SellerParametersValidator;

import java.time.LocalDate;
import java.util.List;

public class TestSellerBuilder implements TestBuilder<Seller> {

    private final int minNameLength;
    private final int maxNameLength;
    private final int minBioLength;
    private final int maxBioLength;
    private final LocalDate birthDate;
    private List<Product> products;
    private final int minProductCount;
    private final int maxProductCount;
    private final TestBuilder<Product> testProductBuilder;

    public TestSellerBuilder() {
        this.minNameLength = 10;
        this.maxNameLength = 100;
        this.minBioLength = 10;
        this.maxBioLength = 100;
        this.birthDate = LocalDate.now().minusYears(SellerParametersValidator.MIN_SELLER_AGE);
        this.products = null;
        this.minProductCount = 5;
        this.maxProductCount = 10;
        this.testProductBuilder = new TestProductBuilder();
    }

    public TestSellerBuilder withProductsSetTo(final List<Product> products) {
        this.products = products;

        return this;
    }

    @Override
    public Seller build() {
        return new Seller(generateName(), generateBio(), generateBirthDate(), generateProducts());
    }

    private String generateName() {
        return RandomStringUtils.randomAlphanumeric(this.minNameLength, this.maxNameLength);
    }

    private String generateBio() {
        return RandomStringUtils.randomAlphanumeric(this.minBioLength, this.maxBioLength);
    }

    private LocalDate generateBirthDate() {
        return this.birthDate;
    }

    private List<Product> generateProducts() {
        if (this.products != null) {
            return this.products;
        }

        return TestProductFactory.createRandomProductsUsingBuilder(
                this.minProductCount,
                this.maxProductCount,
                this.testProductBuilder);
    }
}
