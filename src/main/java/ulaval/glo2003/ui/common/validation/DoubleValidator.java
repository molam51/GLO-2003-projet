package ulaval.glo2003.ui.common.validation;

public class DoubleValidator extends UserInputValidator {

    @Override
    public boolean isValid(final String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
