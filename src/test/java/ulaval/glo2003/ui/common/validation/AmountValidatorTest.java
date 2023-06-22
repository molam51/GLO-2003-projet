package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AmountValidatorTest {

    private final AmountValidator amountValidator = new AmountValidator();

    @ParameterizedTest
    @ValueSource(strings = {"33", "33.3", "33.90"})
    public void givenValidPrice_whenIsValid_thenReturnsTrue(final String input) {
        Assertions.assertTrue(amountValidator.isValid(input));
    }

    @Test
    public void givenTooMuchDecimals_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(amountValidator.isValid("300.201"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a", "\n", " "})
    public void givenInvalidPrice_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(amountValidator.isValid(input));
    }

    @Test
    public void givenNullPrice_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> amountValidator.isValid(null));
    }
}
