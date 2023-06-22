package ulaval.glo2003.domain.offer;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BuyerParametersValidator {

    public static void validate(final BuyerCreationParameters buyerCreationParameters) {
        validateBuyerName(buyerCreationParameters.getName());
        validateBuyerEmail(buyerCreationParameters.getEmail());
        validateBuyerPhoneNumber(buyerCreationParameters.getPhoneNumber());
    }

    private static void validateBuyerName(final String name) {
        if (name == null) {
            throw new MissingParamException("The buyer name is missing");
        }

        if (name.isBlank()) {
            throw new InvalidParamException("The buyer name must not be empty");
        }
    }

    private static void validateBuyerEmail(final String email) {
        if (email == null) {
            throw new MissingParamException("The buyer email is missing");
        }

        String regularExpression = "^\\w+[@]\\w+[.]\\w+$";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidParamException("The buyer email must be a valid");
        }
    }

    private static void validateBuyerPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null) {
            throw new MissingParamException("The buyer phone number is missing");
        }

        String regularExpression = "^\\d{11}$";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new InvalidParamException("The buyer phone number must be a sequence of 11 numbers");
        }
    }
}
