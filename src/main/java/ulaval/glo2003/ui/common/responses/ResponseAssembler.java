package ulaval.glo2003.ui.common.responses;

public interface ResponseAssembler<T, R> {
    R toResponse(T args);
}
