package ulaval.glo2003.infrastructure.mongo.assemblers;

import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoProduct;

import java.util.ArrayList;
import java.util.List;

public class MongoProductAssembler implements MongoEntityAssembler<MongoProduct, Product> {

    private final MongoEntityAssembler<MongoOffer, Offer> mongoOfferAssembler;

    public MongoProductAssembler(final MongoEntityAssembler<MongoOffer, Offer> mongoOfferAssembler) {
        this.mongoOfferAssembler = mongoOfferAssembler;
    }

    @Override
    public MongoProduct toMongoEntity(final Product entity) {
        List<MongoOffer> mongoOffers = offersToMongoOffers(entity.getOffers());
        return new MongoProduct(entity.getId(), entity.getCreatedAt(), entity.getTitle(), entity.getDescription(),
                entity.getSuggestedPrice(), entity.getCategories(), mongoOffers, entity.getViews());
    }

    @Override
    public Product fromMongoEntity(final MongoProduct mongoEntity) {
        List<Offer> offers = mongoOffersToOffers(mongoEntity.getOffers());
        return new Product(mongoEntity.getId(), mongoEntity.getCreatedAt(), mongoEntity.getTitle(),
                mongoEntity.getDescription(), mongoEntity.getSuggestedPrice(), mongoEntity.getCategories(), offers, mongoEntity.getViews());
    }

    private List<Offer> mongoOffersToOffers(final List<MongoOffer> mongoOffers) {
        ArrayList<Offer> offers = new ArrayList<>();
        for (MongoOffer mongoOffer : mongoOffers) {
            Offer offer = mongoOfferAssembler.fromMongoEntity(mongoOffer);
            offers.add(offer);
        }

        return offers;
    }

    private List<MongoOffer> offersToMongoOffers(final List<Offer> offers) {
        ArrayList<MongoOffer> mongoOffers = new ArrayList<>();
        for (Offer offer : offers) {
            MongoOffer mongoOffer = mongoOfferAssembler.toMongoEntity(offer);
            mongoOffers.add(mongoOffer);
        }

        return mongoOffers;
    }
}
