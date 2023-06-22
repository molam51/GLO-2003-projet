package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public class OfferDuplicateException extends ItemDuplicateException {

    private static final String MESSAGE_FORMAT = "Offer with ID %s is already present.";

    public OfferDuplicateException(final UUID id) {
        super(String.format(MESSAGE_FORMAT, id.toString()));
    }

    public OfferDuplicateException(final String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
