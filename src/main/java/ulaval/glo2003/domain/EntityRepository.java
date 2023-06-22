package ulaval.glo2003.domain;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<T extends Entity> {
    void add(T entity);

    void remove(UUID id);

    void update(T entity);

    T fetch(UUID id);

    List<T> getAll();
}
