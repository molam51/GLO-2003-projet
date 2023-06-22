package ulaval.glo2003.domain;

import java.util.List;

public interface EntityFilter<T extends Entity> {
    List<T> filterEntities(List<T> entities);
}
