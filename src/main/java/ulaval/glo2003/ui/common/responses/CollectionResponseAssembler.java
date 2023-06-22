package ulaval.glo2003.ui.common.responses;

import java.util.List;

public interface CollectionResponseAssembler<T, R> {
    R toResponse(List<T> args);
}
