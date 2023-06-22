package ulaval.glo2003.infrastructure.mongo.assemblers;

import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoBuyer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;

public class MongoOfferAssembler implements MongoEntityAssembler<MongoOffer, Offer> {

    private final MongoBuyerAssembler mongoBuyerAssembler;

    public MongoOfferAssembler(final MongoBuyerAssembler mongoBuyerAssembler) {
        this.mongoBuyerAssembler = mongoBuyerAssembler;
    }

    @Override
    public MongoOffer toMongoEntity(final Offer entity) {
        MongoBuyer mongoBuyer = mongoBuyerAssembler.toMongoBuyer(entity.getBuyer());
        return new MongoOffer(entity.getId(), entity.getCreatedAt(), entity.getAmount(), entity.getMessage(),
                mongoBuyer);
    }

    @Override
    public Offer fromMongoEntity(final MongoOffer mongoEntity) {
        Buyer buyer = mongoBuyerAssembler.fromMongoBuyer(mongoEntity.getBuyer());
        return new Offer(mongoEntity.getId(), mongoEntity.getCreatedAt(), mongoEntity.getAmount(),
                mongoEntity.getMessage(), buyer);
    }
}
