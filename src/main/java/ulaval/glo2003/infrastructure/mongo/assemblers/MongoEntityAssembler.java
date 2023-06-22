package ulaval.glo2003.infrastructure.mongo.assemblers;

import ulaval.glo2003.domain.Entity;
import ulaval.glo2003.infrastructure.mongo.entities.MongoEntity;

public interface MongoEntityAssembler<M extends MongoEntity, E extends Entity> {
    M toMongoEntity(E entity);

    E fromMongoEntity(M mongoEntity);
}
