package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public class SellerNotFoundException extends ItemNotFoundException {

    private static final String DEFAULT_MESSAGE = "Seller not found.";
    private static final String MESSAGE_FORMAT = "Seller with ID %s not found.";

    public SellerNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public SellerNotFoundException(final UUID id) {
        super(String.format(MESSAGE_FORMAT, id.toString()));
    }

    public SellerNotFoundException(final String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
