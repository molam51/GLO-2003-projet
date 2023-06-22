package ulaval.glo2003.infrastructure.mongo.repositories;

import dev.morphia.Datastore;
import org.junit.jupiter.api.*;
import ulaval.glo2003.domain.exceptions.ProductDuplicateException;
import ulaval.glo2003.domain.exceptions.ProductNotFoundException;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoBuyerAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoOfferAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoProductAssembler;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestOfferBuilder;
import ulaval.glo2003.utils.TestProductBuilder;

import java.util.List;
import java.util.UUID;

public class MongoProductRepositoryTest {

    private static final double PRODUCT_TO_UPDATE_SUGGESTED_PRICE = 199.99;

    private static final Product PRODUCT_1 = new TestProductBuilder().build();
    private static final Product PRODUCT_2 = new TestProductBuilder().build();
    private static final Product PRODUCT_3 = new TestProductBuilder().build();
    private static Product product1ToUpdate;

    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static MongoProductRepository mongoProductRepository;

    @BeforeAll
    public static void setup() {
        MongoBuyerAssembler mongoBuyerAssembler = new MongoBuyerAssembler();
        MongoOfferAssembler mongoOfferAssembler = new MongoOfferAssembler(mongoBuyerAssembler);
        MongoProductAssembler mongoProductAssembler = new MongoProductAssembler(mongoOfferAssembler);

        E2ETestUtilities.dropCollections(DATASTORE);

        Offer offer = new TestOfferBuilder().build();
        MongoOffer mongoOffer = mongoOfferAssembler.toMongoEntity(offer);
        DATASTORE.save(mongoOffer);

        product1ToUpdate = new Product(
                PRODUCT_1.getId(),
                PRODUCT_1.getCreatedAt(),
                "Swimsuit",
                "Keeps you warm in cold water",
                PRODUCT_TO_UPDATE_SUGGESTED_PRICE,
                List.of(ProductCategory.SPORTS, ProductCategory.APPAREL),
                List.of(offer)
        );

        mongoProductRepository = new MongoProductRepository(DATASTORE, mongoProductAssembler);
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
    }

    @AfterEach
    public void teardownAfterEach() {
        E2ETestUtilities.dropCollection(DATASTORE, E2ETestUtilities.MONGO_PRODUCTS_DATABASE_NAME);
    }

    @Test
    public void givenValidProduct_whenAdd_thenAddsProductToRepository() {
        UUID productId = PRODUCT_1.getId();

        mongoProductRepository.add(PRODUCT_1);

        Assertions.assertEquals(PRODUCT_1, mongoProductRepository.fetch(productId));
    }

    @Test
    public void givenMultipleValidProducts_whenAdd_thenAddsProductsToRepository() {
        UUID product1Id = PRODUCT_1.getId();
        UUID product2Id = PRODUCT_2.getId();

        mongoProductRepository.add(PRODUCT_1);
        mongoProductRepository.add(PRODUCT_2);

        Assertions.assertEquals(PRODUCT_1, mongoProductRepository.fetch(product1Id));
        Assertions.assertEquals(PRODUCT_2, mongoProductRepository.fetch(product2Id));
    }

    @Test
    public void givenExistingProductId_whenAdd_thenThrowsDuplicateProductException() {
        mongoProductRepository.add(PRODUCT_1);

        Assertions.assertThrows(ProductDuplicateException.class, () -> mongoProductRepository.add(PRODUCT_1));
    }

    @Test
    public void givenNullProduct_whenAdd_thenThrowsNullPointerException() {
        Product nullProduct = null;

        Assertions.assertThrows(NullPointerException.class, () -> mongoProductRepository.add(nullProduct));
    }

    @Test
    public void givenNullProduct_whenUpdate_thenThrowsNullPointerException() {
        Product nullProduct = null;

        Assertions.assertThrows(NullPointerException.class, () -> mongoProductRepository.update(nullProduct));
    }

    @Test
    public void givenNonexistentProduct_whenUpdate_thenThrowsProductNotFoundException() {
        Assertions.assertThrows(ProductNotFoundException.class, () -> mongoProductRepository.update(PRODUCT_1));
    }

    @Test
    public void givenExistingProduct_whenUpdate_thenUpdatesCorrespondingProduct() {
        mongoProductRepository.add(PRODUCT_1);

        mongoProductRepository.update(product1ToUpdate);

        Product product1Updated = mongoProductRepository.fetch(PRODUCT_1.getId());
        Assertions.assertEquals(product1Updated.getId(), product1ToUpdate.getId());
        Assertions.assertEquals(product1Updated.getCreatedAt(), product1ToUpdate.getCreatedAt());
        Assertions.assertEquals(product1Updated.getTitle(), product1ToUpdate.getTitle());
        Assertions.assertEquals(product1Updated.getDescription(), product1ToUpdate.getDescription());
        Assertions.assertEquals(product1Updated.getSuggestedPrice(), product1ToUpdate.getSuggestedPrice());
        Assertions.assertIterableEquals(product1Updated.getCategories(), product1ToUpdate.getCategories());

        assertOffersEqualsOffers(product1Updated.getOffers(), product1ToUpdate.getOffers());
    }

    @Test
    public void givenValidProduct_whenRemove_thenThrowsUnsupportedOperationException() {
        UUID productId = PRODUCT_1.getId();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> mongoProductRepository.remove(productId));
    }

    @Test
    public void givenExistingProductId_whenFetch_thenReturnsCorrespondingProduct() {
        UUID productId = PRODUCT_3.getId();
        mongoProductRepository.add(PRODUCT_3);

        Product fetchedProduct = mongoProductRepository.fetch(productId);

        Assertions.assertEquals(PRODUCT_3, fetchedProduct);
        Assertions.assertEquals(productId, fetchedProduct.getId());
    }

    @Test
    public void givenNullProductId_whenFetch_thenThrowsNullPointerException() {
        UUID nullProductId = null;

        Assertions.assertThrows(NullPointerException.class, () -> mongoProductRepository.fetch(nullProductId));
    }

    @Test
    public void givenNonexistentProductId_whenFetch_thenThrowsProductNotFoundException() {
        UUID nonexistentProductId = PRODUCT_1.getId();

        Assertions.assertThrows(ProductNotFoundException.class, () -> mongoProductRepository.fetch(nonexistentProductId));
    }

    @Test
    public void givenNoProducts_whenGetAll_thenReturnsEmptyList() {
        List<Product> fetchedProducts = mongoProductRepository.getAll();

        Assertions.assertTrue(fetchedProducts.isEmpty());
    }

    @Test
    public void givenMultipleProducts_whenGetAll_thenReturnsSameAmountOfProducts() {
        mongoProductRepository.add(PRODUCT_1);
        mongoProductRepository.add(PRODUCT_2);
        mongoProductRepository.add(PRODUCT_3);

        List<Product> fetchedProducts = mongoProductRepository.getAll();

        Assertions.assertEquals(List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3).size(), fetchedProducts.size());
    }

    @Test
    public void givenMultipleProducts_whenGetAll_thenReturnsSameProducts() {
        mongoProductRepository.add(PRODUCT_1);
        mongoProductRepository.add(PRODUCT_2);
        mongoProductRepository.add(PRODUCT_3);

        List<Product> fetchedSellers = mongoProductRepository.getAll();

        Assertions.assertEquals(List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3), fetchedSellers);
    }

    private void assertOffersEqualsOffers(final List<Offer> offersA, final List<Offer> offersB) {
        Assertions.assertEquals(offersA.size(), offersB.size());

        for (int i = 0; i < offersA.size(); i++) {
            assertOfferEqualsOffer(offersA.get(i), offersB.get(i));
        }
    }

    private void assertOfferEqualsOffer(final Offer offerA, final Offer offerB) {
        Assertions.assertEquals(offerA.getId(), offerB.getId());
        Assertions.assertEquals(offerA.getCreatedAt(), offerB.getCreatedAt());
        Assertions.assertEquals(offerA.getAmount(), offerB.getAmount());
        Assertions.assertEquals(offerA.getMessage(), offerB.getMessage());

        assertBuyerEqualsBuyer(offerA.getBuyer(), offerB.getBuyer());
    }

    private void assertBuyerEqualsBuyer(final Buyer buyerA, final Buyer buyerB) {
        Assertions.assertEquals(buyerA.getName(), buyerB.getName());
        Assertions.assertEquals(buyerA.getEmail(), buyerB.getEmail());
        Assertions.assertEquals(buyerA.getPhoneNumber(), buyerB.getPhoneNumber());
    }
}
