package ulaval.glo2003.ui.common.validation;

import java.util.UUID;

public class UUIDValidator extends UserInputValidator {

    @Override
    public boolean isValid(final String input) {
        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
