package ulaval.glo2003.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.domain.product.ProductCreationParameters;
import ulaval.glo2003.domain.product.ProductFactory;
import ulaval.glo2003.utils.TestProductBuilder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ulaval.glo2003.domain.product.Product.DEFAULT_VIEWS;

public class ProductFactoryTest {

    private final Product validProduct = new TestProductBuilder().build();

    private static final double INVALID_SUGGESTED_PRICE = 0.1;

    private ProductFactory productFactory;

    @BeforeEach
    public void setup() {
        productFactory = new ProductFactory();
    }

    @Test
    public void givenValidParams_whenCreateProduct_thenCreatesValidProduct() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                validProduct.getDescription(),
                validProduct.getSuggestedPrice(),
                validProduct.getCategories());

        Product product = productFactory.create(params);

        Assertions.assertNotNull(product.getId());
        Assertions.assertNotNull(product.getCreatedAt());
        Assertions.assertEquals(validProduct.getTitle(), product.getTitle());
        Assertions.assertEquals(validProduct.getDescription(), product.getDescription());
        Assertions.assertEquals(validProduct.getSuggestedPrice(), product.getSuggestedPrice());
        Assertions.assertIterableEquals(validProduct.getCategories(), product.getCategories());
        Assertions.assertEquals(DEFAULT_VIEWS, product.getViews());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public void givenBlankName_whenCreateProduct_thenThrowsInvalidParamException(final String string) {
        ProductCreationParameters params = new ProductCreationParameters(
                string,
                validProduct.getDescription(),
                validProduct.getSuggestedPrice(),
                validProduct.getCategories());

        Assertions.assertThrows(InvalidParamException.class,
                () -> productFactory.create(params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public void givenBlankDescription_whenCreateProduct_thenThrowsInvalidParamException(final String string) {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                string,
                validProduct.getSuggestedPrice(),
                validProduct.getCategories());

        Assertions.assertThrows(InvalidParamException.class,
                () -> productFactory.create(params));
    }

    @Test
    public void givenASuggestedPriceUnderOne_whenCreateProduct_thenThrowsInvalidParamException() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                validProduct.getDescription(),
                INVALID_SUGGESTED_PRICE,
                validProduct.getCategories());

        Assertions.assertThrows(InvalidParamException.class,
                () -> productFactory.create(params));
    }

    @Test
    public void givenNullTitle_whenCreateProduct_thenThrowsMissingParamException() {
        ProductCreationParameters params = new ProductCreationParameters(
                null,
                validProduct.getDescription(),
                validProduct.getSuggestedPrice(),
                validProduct.getCategories());

        Assertions.assertThrows(MissingParamException.class,
                () -> productFactory.create(params));
    }

    @Test
    public void givenNullDescription_whenCreateProduct_thenThrowsMissingParamException() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                null,
                validProduct.getSuggestedPrice(),
                validProduct.getCategories());

        Assertions.assertThrows(MissingParamException.class,
                () -> productFactory.create(params));
    }

    @Test
    public void givenNullSuggestedPrice_whenCreateProduct_thenThrowsMissingParamException() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                validProduct.getDescription(),
                null,
                validProduct.getCategories());

        Assertions.assertThrows(MissingParamException.class,
                () -> productFactory.create(params));
    }

    @Test
    public void givenNullCategories_whenCreateProduct_thenCategoriesNotNull() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                validProduct.getDescription(),
                validProduct.getSuggestedPrice(),
                null);

        Product product = productFactory.create(params);

        Assertions.assertNotNull(product.getCategories());
    }

    @Test
    public void givenNullCategories_whenCreateProduct_thenCategoriesIsEmpty() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                validProduct.getDescription(),
                validProduct.getSuggestedPrice(),
                null);

        Product product = productFactory.create(params);

        Assertions.assertTrue(product.getCategories().isEmpty());
    }

    @Test
    public void givenNullObjectInCategories_whenCreateProduct_thenThrowsInvalidParamException() {
        ProductCreationParameters params = new ProductCreationParameters(
                validProduct.getTitle(),
                validProduct.getDescription(),
                validProduct.getSuggestedPrice(),
                Stream.concat(validProduct.getCategories().stream(), Stream.of((ProductCategory) null))
                        .collect(Collectors.toList()));

        Assertions.assertThrows(InvalidParamException.class,
                () -> productFactory.create(params));
    }
}
