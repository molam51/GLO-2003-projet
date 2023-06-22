package ulaval.glo2003.ui.seller;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.SellerNotFoundException;
import ulaval.glo2003.ui.common.validation.DateISO8601Validator;
import ulaval.glo2003.ui.common.validation.UUIDValidator;

import java.time.LocalDate;
import java.util.UUID;

public class SellerUtilities {

    private SellerUtilities() {

    }

    public static UUID parseStringToSellerUUID(final String requestSellerId) {
        UUIDValidator uuidValidator = new UUIDValidator();

        if (requestSellerId == null) {
            return null;
        } else if (!uuidValidator.isValid(requestSellerId)) {
            throw new SellerNotFoundException(requestSellerId);
        }

        return UUID.fromString(requestSellerId);
    }

    public static LocalDate parseStringToBirthDate(final String birthDate) {
        DateISO8601Validator dateValidator = new DateISO8601Validator();

        if (birthDate == null) {
            return null;
        } else if (!dateValidator.isValid(birthDate)) {
            throw new InvalidParamException("`birthDate` is not formatted as an ISO-8601 date");
        }

        return LocalDate.parse(birthDate);
    }
}
