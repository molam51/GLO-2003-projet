package ulaval.glo2003.ui.common.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DateTimeISO8601ValidatorTest {

    private final DateTimeISO8601Validator validator = new DateTimeISO8601Validator();

    @Test
    public void givenNullInput_whenIsValid_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> validator.isValid(null));
    }

    @Test
    public void givenEmptyInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid(""));
    }

    @Test
    public void givenDateWithoutTimeInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("2022-01-28"));
    }

    @Test
    public void givenDateWithoutTimeOffsetInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("2022-01-28T15:51:43.202647"));
    }

    @Test
    public void givenDateWithTimeZoneInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("2022-01-28T15:51:43.202647-05:00[America/Toronto]"));
    }

    @Test
    public void givenTimeInput_whenIsValid_thenReturnsFalse() {
        Assertions.assertFalse(validator.isValid("15:51:43.202647-05:00"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2-01-28T15:51:43.202647-05:00",
            "20-01-28T15:51:43.202647-05:00",
            "202-01-28T15:51:43.202647-05:00"})
    public void givenInvalidYearDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-1-28T15:51:43.202647-05:00",
            "2022-13-28T15:51:43.202647-05:00",
            "2022/01-28T15:51:43.202647-05:00"})
    public void givenInvalidMonthDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-01-8T15:51:43.202647-05:00",
            "2022-01-32T15:51:43.202647-05:00",
            "2022-01/28T15:51:43.202647-05:00"})
    public void givenInvalidDayDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-01-28T1:51:43.202647-05:00",
            "2022-01-28T25:51:43.202647-05:00",
            "2022-01-28K15:51:43.202647-05:00"})
    public void givenInvalidHourDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-01-28T15:1:43.202647-05:00",
            "2022-01-28T15:60:43.202647-05:00",
            "2022-01-28T15/51:43.202647-05:00"})
    public void givenInvalidMinutesDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-01-28T15:51:3.202647-05:00",
            "2022-01-28T15:51:60.202647-05:00",
            "2022-01-28T15:51/43.202647-05:00"})
    public void givenInvalidSecondsDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-01-28T15:51:43.2026471234-05:00", "2022-01-28T15:51:43,202647-05:00"})
    public void givenInvalidMillisecondsDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-01-28T15:51:43.202647-5:00",
            "2022-01-28T15:51:43.202647-05:0",
            "2022-01-28T15:51:43.202647-05/00",
            "2022-01-28T15:51:43.202647-25:00",
            "2022-01-28T15:51:43.202647+5:00",
            "2022-01-28T15:51:43.202647+05:0",
            "2022-01-28T15:51:43.202647+05/00",
            "2022-01-28T15:51:43.202647+25:00",
            "2022-01-28T15:51:43.202647/05:00"})
    public void givenInvalidOffsetDateTimeInput_whenIsValid_thenReturnsFalse(final String input) {
        Assertions.assertFalse(validator.isValid(input));
    }

    @Test
    public void givenValidDateTimeInput_whenIsValid_thenReturnsTrue() {
        Assertions.assertTrue(validator.isValid("2022-01-28T15:51:43.202647-05:00"));
    }

    @Test
    public void givenDateTimeWithUTCTimeZoneInput_whenIsValid_thenReturnsTrue() {
        Assertions.assertTrue(validator.isValid("2022-01-28T20:51:43.202647Z"));
    }
}
