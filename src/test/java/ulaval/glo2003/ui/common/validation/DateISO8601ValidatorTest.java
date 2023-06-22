package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DateISO8601ValidatorTest {

    private final DateISO8601Validator validator = new DateISO8601Validator();

    @Test
    public void givenNullInput_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> validator.isValid(null));
    }

    @Test
    public void givenEmptyInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid(""));
    }

    @Test
    public void givenDateWithTimeInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("2022-01-28T15:51:43.202647-05:00"));
    }

    @Test
    public void givenDateWithTimeZoneInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("2022-01-28-05:00[America/Toronto]"));
    }

    @Test
    public void givenTimeInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("15:51:43.202647-05:00"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2-01-28", "20-01-28", "202-01-28"})
    public void givenInvalidYearDateInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-1-28", "2022-13-28", "2022/01-28"})
    public void givenInvalidMonthDateInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-01-8", "2022-01-32", "2022-01/28"})
    public void givenInvalidDayDateInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @Test
    public void givenValidDateInput_whenIsValid_thenReturnsTrue() {
        Assertions.assertTrue(validator.isValid("2022-01-28"));
    }

    @Test
    public void givenDateWithUTCTimeZoneInput_whenIsValid_thenReturnsTrue() {
        Assertions.assertTrue(validator.isValid("2022-01-28Z"));
    }
}
