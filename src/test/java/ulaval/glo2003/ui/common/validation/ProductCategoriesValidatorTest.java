package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ProductCategoriesValidatorTest {

    private final ProductCategoriesValidator productCategoriesValidator = new ProductCategoriesValidator();

    @ParameterizedTest
    @ValueSource(strings = {"", "ELECTRONICS", "ELECTRONICS, APPAREL"})
    public void givenValidProductCategoriesString_whenIsValid_thenReturnsTrue(final String input) {
        Assertions.assertTrue(productCategoriesValidator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"321", "RONICS", "ELECTICS, APPAREL", "dsajklsa,", "890nkas", "ELECTRONICS; APPAREL",
            "ELECTRONICS APPAREL"})
    public void givenInvalidProductCategoriesString_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(productCategoriesValidator.isValid(input));
    }

    @Test
    public void givenNullParameter_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> productCategoriesValidator.isValid(null));
    }
}
