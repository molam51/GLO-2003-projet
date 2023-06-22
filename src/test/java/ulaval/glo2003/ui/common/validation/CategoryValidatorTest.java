package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CategoryValidatorTest {

    private final CategoryValidator categoryValidator = new CategoryValidator();

    @ParameterizedTest
    @ValueSource(strings = {"sports", "SPORTS", "electronics ", " beauty", "aPpArel", "housing", "other"})
    public void givenValidCategory_whenIsValid_thenReturnsTrue(final String input) {
        Assertions.assertTrue(categoryValidator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "\n", "SPORTS ELECTRONICS"})
    public void givenInvalidCategory_whenIsValid_thenReturnsTrue(final String input) {
        Assertions.assertFalse(categoryValidator.isValid(input));
    }

    @Test
    public void givenNullPrice_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> categoryValidator.isValid(null));
    }
}
