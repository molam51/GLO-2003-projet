package ulaval.glo2003.domain.exceptions;

public class InvalidParamException extends RuntimeException {

    public InvalidParamException() {
        super();
    }

    public InvalidParamException(final String message) {
        super(message);
    }
}
