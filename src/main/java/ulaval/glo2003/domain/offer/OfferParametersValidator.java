package ulaval.glo2003.domain.offer;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;

public abstract class OfferParametersValidator {

    private static final int MAX_CHARACTER_REQUIRED = 100;

    public static void validate(final OfferCreationParameters offerCreationParameters) {
        validateOfferAmount(offerCreationParameters.getAmount());
        validateOfferMessage(offerCreationParameters.getMessage());
    }

    private static void validateOfferAmount(final Double amount) {
        if (amount == null) {
            throw new MissingParamException("The amount is missing");
        }
    }

    private static void validateOfferMessage(final String message) {
        if (message == null) {
            throw new MissingParamException("The message is missing");
        }

        if (message.length() < MAX_CHARACTER_REQUIRED) {
            throw new InvalidParamException("The message must be at least 100 characters in length");
        }
    }
}
