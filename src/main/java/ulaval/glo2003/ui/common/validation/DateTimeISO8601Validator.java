package ulaval.glo2003.ui.common.validation;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeISO8601Validator extends UserInputValidator {

    @Override
    public boolean isValid(final String input) {
        if (input == null) {
            throw new NullPointerException();
        }

        try {
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(input);
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }
}
