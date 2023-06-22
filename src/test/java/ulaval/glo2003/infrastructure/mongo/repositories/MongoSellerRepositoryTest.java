package ulaval.glo2003.infrastructure.mongo.repositories;

import dev.morphia.Datastore;
import org.junit.jupiter.api.*;
import ulaval.glo2003.domain.exceptions.SellerDuplicateException;
import ulaval.glo2003.domain.exceptions.SellerNotFoundException;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoBuyerAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoOfferAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoProductAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoSellerAssembler;
import ulaval.glo2003.infrastructure.mongo.entities.MongoProduct;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MongoSellerRepositoryTest {

    private static Seller seller1 = new TestSellerBuilder().build();
    private static Seller seller2 = new TestSellerBuilder().build();
    private static Seller seller3 = new TestSellerBuilder().build();

    private static final Product PRODUCT = new TestProductBuilder().build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static MongoSellerRepository mongoSellerRepository;

    @BeforeAll
    public static void setup() {
        MongoBuyerAssembler mongoBuyerAssembler = new MongoBuyerAssembler();
        MongoOfferAssembler mongoOfferAssembler = new MongoOfferAssembler(mongoBuyerAssembler);
        MongoProductAssembler mongoProductAssembler = new MongoProductAssembler(mongoOfferAssembler);
        MongoSellerAssembler mongoSellerAssembler = new MongoSellerAssembler(mongoProductAssembler);

        E2ETestUtilities.dropCollections(DATASTORE);

        MongoProduct mongoProduct = mongoProductAssembler.toMongoEntity(PRODUCT);
        DATASTORE.save(mongoProduct);

        mongoSellerRepository = new MongoSellerRepository(DATASTORE, mongoSellerAssembler);
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
    }

    @BeforeEach
    public void setupBeforeEach() {
        seller1 = new Seller("Clément", "A real clean coder", LocalDate.now());
        seller2 = new Seller("Monique", "Beast mode activated", LocalDate.now());
        seller3 = new Seller("Anthony", "Blueprint is the way to clean code", LocalDate.now());
    }

    @AfterEach
    public void teardownAfterEach() {
        E2ETestUtilities.dropCollection(DATASTORE, E2ETestUtilities.MONGO_SELLERS_DATABASE_NAME);
    }

    @Test
    public void givenValidSeller_whenAdd_thenAddsSellerToRepository() {
        UUID seller1Id = seller1.getId();

        mongoSellerRepository.add(seller1);

        Assertions.assertEquals(seller1, mongoSellerRepository.fetch(seller1Id));
    }

    @Test
    public void givenMultipleValidSellers_whenAdd_thenAddsSellersToRepository() {
        UUID seller1Id = seller1.getId();
        UUID seller2Id = seller2.getId();

        mongoSellerRepository.add(seller1);
        mongoSellerRepository.add(seller2);

        Assertions.assertEquals(seller1, mongoSellerRepository.fetch(seller1Id));
        Assertions.assertEquals(seller2, mongoSellerRepository.fetch(seller2Id));
    }

    @Test
    public void givenExistingSellerId_whenAdd_thenThrowsDuplicateSellerException() {
        mongoSellerRepository.add(seller1);

        Assertions.assertThrows(SellerDuplicateException.class, () -> mongoSellerRepository.add(seller1));
    }

    @Test
    public void givenNullSeller_whenAdd_thenThrowsNullPointerException() {
        Seller nullSeller = null;

        Assertions.assertThrows(NullPointerException.class, () -> mongoSellerRepository.add(nullSeller));
    }

    @Test
    public void givenExistingSellerId_whenFetch_thenReturnsCorrespondingProduct() {
        UUID sellerId = seller3.getId();
        mongoSellerRepository.add(seller3);

        Seller fetchedSeller = mongoSellerRepository.fetch(sellerId);

        Assertions.assertEquals(seller3, fetchedSeller);
        Assertions.assertEquals(sellerId, fetchedSeller.getId());
    }

    @Test
    public void givenNullSellerId_whenFetch_thenThrowsNullPointerException() {
        UUID sellerId = null;

        Assertions.assertThrows(NullPointerException.class, () -> mongoSellerRepository.fetch(sellerId));
    }

    @Test
    public void givenNonexistentSellerId_whenFetch_thenThrowsSellerNotFoundException() {
        UUID sellerId = seller1.getId();

        Assertions.assertThrows(SellerNotFoundException.class, () -> mongoSellerRepository.fetch(sellerId));
    }

    @Test
    public void givenValidSeller_whenRemove_thenThrowsUnsupportedOperationException() {
        UUID sellerId = seller1.getId();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> mongoSellerRepository.remove(sellerId));
    }

    @Test
    public void givenProductIdSoldBySeller_whenFetchByProduct_thenReturnsCorrespondingSeller() {
        UUID productId = PRODUCT.getId();
        seller1.addProduct(PRODUCT);
        mongoSellerRepository.add(seller1);

        Seller fetchedSeller = mongoSellerRepository.fetchByProduct(productId);

        Assertions.assertEquals(seller1, fetchedSeller);
    }

    @Test
    public void givenProductIdNotSoldBySeller_whenFetchByProduct_thenThrowsSellerNotFoundException() {
        UUID productId = PRODUCT.getId();
        mongoSellerRepository.add(seller1);

        Assertions.assertThrows(SellerNotFoundException.class, () -> mongoSellerRepository.fetchByProduct(productId));
    }

    @Test
    public void givenNonexistentProductId_whenFetchByProduct_thenThrowsSellerNotFoundException() {
        UUID nonexistentProductId = UUID.randomUUID();

        Assertions.assertThrows(SellerNotFoundException.class, () -> mongoSellerRepository.fetchByProduct(nonexistentProductId));
    }

    @Test
    public void givenNullProductId_whenFetchByProduct_thenThrowsSellerNotFoundException() {
        UUID nullProductId = null;

        Assertions.assertThrows(SellerNotFoundException.class, () -> mongoSellerRepository.fetchByProduct(nullProductId));
    }

    @Test
    public void givenNoSellers_whenGetAll_thenReturnsEmptyList() {
        List<Seller> fetchedSellers = mongoSellerRepository.getAll();

        Assertions.assertTrue(fetchedSellers.isEmpty());
    }

    @Test
    public void givenMultipleSellers_whenGetAll_thenReturnsSameAmountOfSellers() {
        mongoSellerRepository.add(seller1);
        mongoSellerRepository.add(seller2);
        mongoSellerRepository.add(seller3);

        List<Seller> fetchedSellers = mongoSellerRepository.getAll();

        Assertions.assertEquals(List.of(seller1, seller2, seller3).size(), fetchedSellers.size());
    }

    @Test
    public void givenMultipleSellers_whenGetAll_thenReturnsSameSellers() {
        mongoSellerRepository.add(seller1);
        mongoSellerRepository.add(seller2);
        mongoSellerRepository.add(seller3);

        List<Seller> fetchedSellers = mongoSellerRepository.getAll();

        Assertions.assertEquals(List.of(seller1, seller2, seller3), fetchedSellers);
    }
}
