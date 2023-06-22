package ulaval.glo2003.infrastructure.mongo.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import ulaval.glo2003.domain.exceptions.ProductDuplicateException;
import ulaval.glo2003.domain.exceptions.ProductNotFoundException;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductRepository;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoProductAssembler;
import ulaval.glo2003.infrastructure.mongo.entities.MongoProduct;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoProductRepository extends ProductRepository {

    private final Datastore datastore;
    private final MongoProductAssembler mongoProductAssembler;

    public MongoProductRepository(final Datastore datastore, final MongoProductAssembler mongoProductAssembler) {
        this.datastore = datastore;
        this.mongoProductAssembler = mongoProductAssembler;
    }

    @Override
    public void add(final Product product) {
        if (product == null) {
            throw new NullPointerException();
        }

        if (isPresent(product.getId())) {
            throw new ProductDuplicateException(product.getId());
        }

        MongoProduct mongoProduct = mongoProductAssembler.toMongoEntity(product);
        datastore.save(mongoProduct);
    }

    @Override
    public void remove(final UUID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(final Product product) {
        if (product == null) {
            throw new NullPointerException();
        }

        if (!isPresent(product.getId())) {
            throw new ProductNotFoundException(product.getId());
        }

        MongoProduct mongoProduct = mongoProductAssembler.toMongoEntity(product);

        datastore.find(MongoProduct.class)
                .filter(Filters.eq("id", product.getId()))
                .update(UpdateOperators.set(mongoProduct))
                .execute();
    }

    @Override
    public Product fetch(final UUID id) {
        if (id == null) {
            throw new NullPointerException();
        }

        MongoProduct mongoProduct = datastore.find(MongoProduct.class).filter(Filters.eq("id", id)).first();

        if (mongoProduct == null) {
            throw new ProductNotFoundException(id);
        }

        return mongoProductAssembler.fromMongoEntity(mongoProduct);
    }

    @Override
    public List<Product> getAll() {
        return datastore.find(MongoProduct.class).stream()
                .map(mongoProductAssembler::fromMongoEntity)
                .collect(Collectors.toList());
    }

    private boolean isPresent(final UUID id) {
        return datastore.find(MongoProduct.class).filter(Filters.eq("id", id)).count() != 0;
    }
}
