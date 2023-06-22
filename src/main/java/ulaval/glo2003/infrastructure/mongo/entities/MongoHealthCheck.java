package ulaval.glo2003.infrastructure.mongo.entities;

import dev.morphia.annotations.Entity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity("HealthChecks")
public class MongoHealthCheck extends MongoEntity {

    public MongoHealthCheck() {
        super(UUID.randomUUID(), OffsetDateTime.now());
    }
}
