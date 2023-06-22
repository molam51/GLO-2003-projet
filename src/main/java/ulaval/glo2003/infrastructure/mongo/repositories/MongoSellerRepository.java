package ulaval.glo2003.infrastructure.mongo.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import ulaval.glo2003.domain.exceptions.SellerDuplicateException;
import ulaval.glo2003.domain.exceptions.SellerNotFoundException;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.domain.seller.SellerRepository;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoSellerAssembler;
import ulaval.glo2003.infrastructure.mongo.entities.MongoSeller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoSellerRepository extends SellerRepository {

    private final Datastore datastore;
    private final MongoSellerAssembler mongoSellerAssembler;

    public MongoSellerRepository(final Datastore datastore, final MongoSellerAssembler mongoSellerAssembler) {
        this.datastore = datastore;
        this.mongoSellerAssembler = mongoSellerAssembler;
    }

    @Override
    public void add(final Seller seller) {
        if (seller == null) {
            throw new NullPointerException();
        }

        if (isPresent(seller.getId())) {
            throw new SellerDuplicateException(seller.getId());
        }

        MongoSeller mongoSeller = mongoSellerAssembler.toMongoEntity(seller);
        datastore.save(mongoSeller);
    }

    @Override
    public void remove(final UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(final Seller seller) {
        if (seller == null) {
            throw new NullPointerException();
        }

        if (!isPresent(seller.getId())) {
            throw new SellerNotFoundException(seller.getId());
        }

        MongoSeller mongoSeller = mongoSellerAssembler.toMongoEntity(seller);

        datastore.find(MongoSeller.class)
                .filter(Filters.eq("id", seller.getId()))
                .update(UpdateOperators.set(mongoSeller))
                .execute();
    }

    @Override
    public Seller fetch(final UUID id) {
        if (id == null) {
            throw new NullPointerException();
        }

        MongoSeller mongoSeller = datastore.find(MongoSeller.class).filter(Filters.eq("id", id)).first();

        if (mongoSeller == null) {
            throw new SellerNotFoundException(id);
        }

        return mongoSellerAssembler.fromMongoEntity(mongoSeller);
    }

    @Override
    public List<Seller> getAll() {
        return datastore.find(MongoSeller.class).stream()
                .map(mongoSellerAssembler::fromMongoEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Seller fetchByProduct(final UUID productId) {
        List<Seller> sellers = getAll();

        for (Seller seller : sellers) {
            List<UUID> productIds = seller.getProducts().stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());

            if (productIds.contains(productId)) {
                return seller;
            }
        }

        throw new SellerNotFoundException();
    }

    private boolean isPresent(final UUID id) {
        return datastore.find(MongoSeller.class).filter(Filters.eq("id", id)).count() != 0;
    }
}
