package ulaval.glo2003.infrastructure.mongo.assemblers;

import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.infrastructure.mongo.entities.MongoProduct;
import ulaval.glo2003.infrastructure.mongo.entities.MongoSeller;

import java.util.ArrayList;
import java.util.List;

public class MongoSellerAssembler implements MongoEntityAssembler<MongoSeller, Seller> {

    private final MongoEntityAssembler<MongoProduct, Product> mongoProductAssembler;

    public MongoSellerAssembler(final MongoEntityAssembler<MongoProduct, Product> mongoProductAssembler) {
        this.mongoProductAssembler = mongoProductAssembler;
    }

    @Override
    public MongoSeller toMongoEntity(final Seller entity) {
        List<MongoProduct> mongoProducts = productsToMongoProducts(entity.getProducts());
        return new MongoSeller(entity.getId(), entity.getCreatedAt(), entity.getName(), entity.getBio(),
                entity.getBirthDate(), mongoProducts);
    }

    @Override
    public Seller fromMongoEntity(final MongoSeller mongoEntity) {
        List<Product> products = mongoProductsToProducts(mongoEntity.getProducts());
        return new Seller(mongoEntity.getId(), mongoEntity.getCreatedAt(), mongoEntity.getName(), mongoEntity.getBio(),
                mongoEntity.getBirthDate(), products);
    }

    private List<MongoProduct> productsToMongoProducts(final List<Product> products) {
        List<MongoProduct> mongoProducts = new ArrayList<>();
        for (Product product : products) {
            MongoProduct mongoProduct = mongoProductAssembler.toMongoEntity(product);
            mongoProducts.add(mongoProduct);
        }

        return mongoProducts;
    }

    private List<Product> mongoProductsToProducts(final List<MongoProduct> mongoProducts) {
        List<Product> products = new ArrayList<>();
        for (MongoProduct mongoProduct : mongoProducts) {
            Product product = mongoProductAssembler.fromMongoEntity(mongoProduct);
            products.add(product);
        }

        return products;
    }
}
