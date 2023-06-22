package ulaval.glo2003.infrastructure.mongo.entities;

import dev.morphia.annotations.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class MongoEntity {

    @Id
    private UUID id;
    private OffsetDateTime createdAt;

    public MongoEntity() {

    }

    public MongoEntity(final UUID id, final OffsetDateTime createdAt) {
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
