package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public class OfferNotFoundException extends ItemNotFoundException {

    private static final String MESSAGE_FORMAT = "Offer with ID %s not found.";

    public OfferNotFoundException(final UUID id) {
        super(String.format(MESSAGE_FORMAT, id.toString()));
    }

    public OfferNotFoundException(final String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
