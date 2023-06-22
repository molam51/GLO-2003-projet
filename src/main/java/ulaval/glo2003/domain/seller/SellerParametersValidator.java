package ulaval.glo2003.domain.seller;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;

import java.time.LocalDate;
import java.time.Period;

public abstract class SellerParametersValidator {

    public static final int MIN_SELLER_AGE = 18;

    public static void validate(final SellerCreationParameters sellerCreationParameters) {
        validateSellerName(sellerCreationParameters.getName());
        validateSellerBio(sellerCreationParameters.getBio());
        validateSellerBirthDate(sellerCreationParameters.getBirthDate());
    }

    private static void validateSellerName(final String name) {
        if (name == null) {
            throw new MissingParamException("Parameter `name` is missing.");
        }

        if (name.isBlank()) {
            throw new InvalidParamException("Parameter `name` is blank.");
        }
    }

    private static void validateSellerBio(final String bio) {
        if (bio == null) {
            throw new MissingParamException("Parameter `bio` is missing.");
        }

        if (bio.isBlank()) {
            throw new InvalidParamException("Parameter `bio` is blank.");
        }
    }

    private static void validateSellerBirthDate(final LocalDate birthDate) {
        if (birthDate == null) {
            throw new MissingParamException("Parameter `birthDate` is missing.");
        }

        Period sellerAge = Period.between(birthDate, LocalDate.now());
        if (sellerAge.getYears() < MIN_SELLER_AGE) {
            throw new InvalidParamException("Parameter `birthDate` is invalid. Seller must be at least 18 years old.");
        }
    }
}
