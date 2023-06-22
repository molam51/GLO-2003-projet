package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public class ProductNotFoundException extends ItemNotFoundException {

    private static final String MESSAGE_FORMAT = "Product with ID %s not found.";

    public ProductNotFoundException(final UUID id) {
        super(String.format(MESSAGE_FORMAT, id.toString()));
    }

    public ProductNotFoundException(final String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
