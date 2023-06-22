package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DoubleValidatorTest {

    private final DoubleValidator doubleValidator = new DoubleValidator();

    @ParameterizedTest
    @ValueSource(strings = {"32.2", "3221.321", "765.543", "43.0", "32", "432."})
    public void givenValidDoublesInStringFormat_whenIsValid_thenReturnsTrue(final String input) {
        Assertions.assertTrue(doubleValidator.isValid(input));
    }

    @Test
    public void givenNullInput_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> doubleValidator.isValid(null));
    }

    @Test
    public void givenEmptyInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(doubleValidator.isValid(""));
    }
}
