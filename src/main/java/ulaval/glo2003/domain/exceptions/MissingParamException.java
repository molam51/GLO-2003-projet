package ulaval.glo2003.domain.exceptions;

public class MissingParamException extends RuntimeException {

    public MissingParamException() {
        super();
    }

    public MissingParamException(final String message) {
        super(message);
    }
}
