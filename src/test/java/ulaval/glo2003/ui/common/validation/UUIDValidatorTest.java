package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

public class UUIDValidatorTest {

    private final UUIDValidator uuidValidator = new UUIDValidator();

    @ParameterizedTest
    @ValueSource(strings = {"", "", "", "", ""})
    public void givenValidUUID_whenIsValid_thenReturnsTrue() {
        String testUUID = UUID.randomUUID().toString();

        Assertions.assertTrue(uuidValidator.isValid(testUUID));
    }

    @ParameterizedTest
    @ValueSource(strings = {"dfsanokfsa", "321nmkKjdsa", "58e0a7d7-eebc-11d8-9660800200c9a66", "d324-dsj231-dfsab321-12", "cnkclsa8932"})
    public void givenInvalidUUID_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(uuidValidator.isValid(input));
    }

    @Test
    public void givenEmptyStringValue_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(uuidValidator.isValid(""));
    }

    @Test
    public void givenNullInput_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> uuidValidator.isValid(null));
    }
}
