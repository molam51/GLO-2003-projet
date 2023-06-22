package ulaval.glo2003.ui.offer;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.ui.common.validation.AmountValidator;

public class OfferUtilities {

    private OfferUtilities() {

    }

    public static Double parseStringToAmount(final String amount) {
        AmountValidator amountValidator = new AmountValidator();

        if (amount == null) {
            return null;
        } else if (!amountValidator.isValid(amount)) {
            throw new InvalidParamException("`amount` is not a number");
        }

        return Double.parseDouble(amount);
    }
}
