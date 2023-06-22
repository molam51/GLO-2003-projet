package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public class ProductDuplicateException extends ItemDuplicateException {

    private static final String MESSAGE_FORMAT = "Product with ID %s is already present.";

    public ProductDuplicateException(final UUID id) {
        super(String.format(MESSAGE_FORMAT, id.toString()));
    }

    public ProductDuplicateException(final String id) {
        super(String.format(MESSAGE_FORMAT, id));
    }
}
