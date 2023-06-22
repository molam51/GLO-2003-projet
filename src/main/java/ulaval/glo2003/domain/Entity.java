package ulaval.glo2003.domain;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public abstract class Entity {

    private final UUID id;
    private final OffsetDateTime createdAt;

    public Entity() {
        this(UUID.randomUUID(), OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC));
    }

    public Entity(final UUID id, final OffsetDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public final UUID getId() {
        return id;
    }

    public final OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
