package ulaval.glo2003.domain;

public interface EntityFactory<T extends Entity> {
    T create(Object params);
}
