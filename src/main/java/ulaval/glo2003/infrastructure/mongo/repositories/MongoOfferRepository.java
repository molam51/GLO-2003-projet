package ulaval.glo2003.infrastructure.mongo.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import ulaval.glo2003.domain.exceptions.OfferDuplicateException;
import ulaval.glo2003.domain.exceptions.OfferNotFoundException;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.offer.OfferRepository;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoOfferAssembler;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoOfferRepository extends OfferRepository {

    private final MongoOfferAssembler mongoOfferAssembler;
    private final Datastore datastore;

    public MongoOfferRepository(final Datastore datastore, final MongoOfferAssembler mongoOfferAssembler) {
        this.datastore = datastore;
        this.mongoOfferAssembler = mongoOfferAssembler;
    }

    @Override
    public void add(final Offer entity) {
        if (entity == null) {
            throw new NullPointerException();
        }

        if (isPresent(entity.getId())) {
            throw new OfferDuplicateException(entity.getId());
        }

        MongoOffer mongoOffer = mongoOfferAssembler.toMongoEntity(entity);
        datastore.save(mongoOffer);
    }

    @Override
    public void remove(final UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(final Offer offer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Offer fetch(final UUID id) {
        if (id == null) {
            throw new NullPointerException();
        }

        MongoOffer mongoOffer = datastore.find(MongoOffer.class).filter(Filters.eq("id", id)).first();

        if (mongoOffer == null) {
            throw new OfferNotFoundException(id);
        }

        return mongoOfferAssembler.fromMongoEntity(mongoOffer);
    }

    @Override
    public List<Offer> getAll() {
        return datastore.find(MongoOffer.class).stream()
                .map(mongoOfferAssembler::fromMongoEntity)
                .collect(Collectors.toList());
    }

    private boolean isPresent(final UUID id) {
        return datastore.find(MongoOffer.class)
                .filter(Filters.eq("id", id))
                .count() != 0;
    }
}
