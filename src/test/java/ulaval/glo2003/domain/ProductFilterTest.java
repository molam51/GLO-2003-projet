package ulaval.glo2003.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.domain.product.ProductFilter;
import ulaval.glo2003.utils.ProductFilterTestUtilities;
import ulaval.glo2003.utils.TestProductBuilder;

import java.util.List;
import java.util.stream.Stream;

public class ProductFilterTest {

    private static final int MIN_SUGGESTED_PRICE_PRODUCT_1 = 300;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT_1 = 400;
    private static final int MIN_SUGGESTED_PRICE_PRODUCT_2 = 9000;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT_2 = 9500;

    private static final Product PRODUCT_1 = new TestProductBuilder()
            .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT_1, MAX_SUGGESTED_PRICE_PRODUCT_1).build();
    private static final Product PRODUCT_2 = new TestProductBuilder()
            .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT_2, MAX_SUGGESTED_PRICE_PRODUCT_2).build();
    private static final List<Product> PRODUCTS = List.of(PRODUCT_1, PRODUCT_2);

    private static Stream<Product> productGenerator() {
        return Stream.of(PRODUCT_1, PRODUCT_2);
    }

    private static ProductFilter createMatchingProductFilter(final List<Product> products) {
        return new ProductFilter(
                null,
                null,
                ProductFilterTestUtilities.getMatchingCategories(products),
                ProductFilterTestUtilities.getMatchingMinPrice(products),
                ProductFilterTestUtilities.getMatchingMaxPrice(products));
    }

    private static ProductFilter createUnmatchingProductFilter(final List<Product> products) {
        return new ProductFilter(
                null,
                null,
                ProductFilterTestUtilities.getUnmatchingCategories(products),
                ProductFilterTestUtilities.getUnmatchingMinPrice(products),
                ProductFilterTestUtilities.getUnmatchingMaxPrice(products));
    }

    private static String getUnmatchingTitle() {
        int minUnmatchingTitleLength = Stream.of(PRODUCT_1, PRODUCT_2)
                .mapToInt((product) -> product.getTitle().length())
                .min()
                .getAsInt();
        int maxUnmatchingTitleLength = Stream.of(PRODUCT_1, PRODUCT_2)
                .mapToInt((product) -> product.getTitle().length())
                .max()
                .getAsInt();

        return RandomStringUtils.randomAlphanumeric(minUnmatchingTitleLength, maxUnmatchingTitleLength);
    }

    @Test
    public void givenFilterWithNullValues_whenFilterEntities_thenReturnsAllProducts() {
        ProductFilter filter = new ProductFilter();

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertIterableEquals(List.of(PRODUCT_1, PRODUCT_2), filteredProducts);
    }

    @ParameterizedTest
    @MethodSource("productGenerator")
    public void givenFilterWithMatchingTitle_whenFilterEntities_thenReturnsProduct(final Product product) {
        String title = product.getTitle().substring(1, product.getTitle().length() - 1);
        ProductFilter filter = new ProductFilter();
        filter.setTitle(title);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertIterableEquals(List.of(product), filteredProducts);
    }

    @Test
    public void givenFilterWithUnmatchingTitle_whenFilterEntities_thenReturnsEmptyList() {
        String title = getUnmatchingTitle();
        ProductFilter filter = new ProductFilter();
        filter.setTitle(title);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertTrue(filteredProducts.isEmpty());
    }

    @Test
    public void givenFilterWithExistingProductCategories_whenFilterEntities_thenReturnsProduct() {
        List<ProductCategory> categories = ProductFilterTestUtilities.getMatchingCategories(PRODUCTS);
        ProductFilter filter = new ProductFilter();
        filter.setCategories(categories);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertIterableEquals(List.of(PRODUCT_1, PRODUCT_2), filteredProducts);
    }

    @Test
    public void givenFilterWithNonExistentProductCategories_whenFilterEntities_thenReturnsEmptyList() {
        List<ProductCategory> categories = ProductFilterTestUtilities.getUnmatchingCategories(PRODUCTS);
        ProductFilter filter = new ProductFilter();
        filter.setCategories(categories);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertTrue(filteredProducts.isEmpty());
    }

    @Test
    public void givenFilterWithMatchingMinPrice_whenFilterEntities_thenReturnsProduct() {
        Double minPrice = ProductFilterTestUtilities.getMatchingMinPrice(PRODUCTS);
        ProductFilter filter = new ProductFilter();
        filter.setMinPrice(minPrice);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertIterableEquals(List.of(PRODUCT_1, PRODUCT_2), filteredProducts);
    }

    @Test
    public void givenFilterWithUnmatchingMinPrice_whenFilterEntities_thenReturnsEmptyList() {
        Double minPrice = ProductFilterTestUtilities.getUnmatchingMinPrice(PRODUCTS);
        ProductFilter filter = new ProductFilter();
        filter.setMinPrice(minPrice);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertTrue(filteredProducts.isEmpty());
    }

    @Test
    public void givenFilterWithMatchingMaxPrice_whenFilterEntities_thenReturnsProduct() {
        Double maxPrice = ProductFilterTestUtilities.getMatchingMaxPrice(PRODUCTS);
        ProductFilter filter = new ProductFilter();
        filter.setMaxPrice(maxPrice);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertIterableEquals(List.of(PRODUCT_1, PRODUCT_2), filteredProducts);
    }

    @Test
    public void givenFilterWithUnmatchingMaxPrice_whenFilterEntities_thenReturnsEmptyList() {
        Double maxPrice = ProductFilterTestUtilities.getUnmatchingMaxPrice(PRODUCTS);
        ProductFilter filter = new ProductFilter();
        filter.setMaxPrice(maxPrice);

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertTrue(filteredProducts.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("productGenerator")
    public void givenFilterWithMatchingParams_whenFilterEntities_thenReturnsProduct(final Product product) {
        ProductFilter filter = createMatchingProductFilter(List.of(product));

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertIterableEquals(List.of(product), filteredProducts);
    }

    @Test
    public void givenFilterWithUnmatchingParams_whenFilterEntities_thenReturnsEmptyList() {
        ProductFilter filter = createUnmatchingProductFilter(List.of(PRODUCT_1, PRODUCT_2));

        List<Product> filteredProducts = filter.filterEntities(PRODUCTS);

        Assertions.assertTrue(filteredProducts.isEmpty());
    }
}
