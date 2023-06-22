package ulaval.glo2003.ui.common.validation;

import java.math.BigDecimal;

public class AmountValidator extends UserInputValidator {

    @Override
    public boolean isValid(final String input) {
        try {
            double price = Double.parseDouble(input);
            return BigDecimal.valueOf(price).scale() <= 2;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
