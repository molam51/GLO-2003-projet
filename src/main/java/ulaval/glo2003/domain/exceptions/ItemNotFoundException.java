package ulaval.glo2003.domain.exceptions;

import java.util.UUID;

public abstract class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException() {
        super();
    }

    public ItemNotFoundException(final String message) {
        super(message);
    }

    public ItemNotFoundException(final UUID id) {
        super(String.format("Item with ID %s not found.", id.toString()));
    }
}
