package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public class SellerDuplicateException extends ItemDuplicateException {

    private static final String MESSAGE_FORMAT = "Seller with ID %s is already present.";

    public SellerDuplicateException(final UUID id) {
        super(String.format(MESSAGE_FORMAT, id.toString()));
    }

    public SellerDuplicateException(final String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
