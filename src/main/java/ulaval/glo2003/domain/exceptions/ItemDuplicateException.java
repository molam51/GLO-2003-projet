package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public abstract class ItemDuplicateException extends RuntimeException {

    public ItemDuplicateException() {
        super();
    }

    public ItemDuplicateException(final String message) {
        super(message);
    }

    public ItemDuplicateException(final UUID id) {
        super(String.format("Item with ID %s is already present.", id.toString()));
    }
}
