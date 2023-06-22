package ulaval.glo2003.infrastructure.mongo.assemblers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.infrastructure.mongo.entities.MongoBuyer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoProduct;
import ulaval.glo2003.infrastructure.mongo.entities.MongoSeller;
import ulaval.glo2003.utils.TestOfferBuilder;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.withSettings;

public class MongoSellerAssemblerTest {

    private static final Offer VALID_OFFER = new TestOfferBuilder().build();
    private static final Buyer VALID_BUYER = VALID_OFFER.getBuyer();
    private static final MongoBuyer VALID_MONGO_BUYER = new MongoBuyer(
            VALID_BUYER.getName(),
            VALID_BUYER.getEmail(),
            VALID_BUYER.getPhoneNumber());
    private static final MongoOffer VALID_MONGO_OFFER = new MongoOffer(
            VALID_OFFER.getId(),
            VALID_OFFER.getCreatedAt(),
            VALID_OFFER.getAmount(),
            VALID_OFFER.getMessage(),
            VALID_MONGO_BUYER);
    private static final Product VALID_PRODUCT = new TestProductBuilder().withOffersSetTo(List.of(VALID_OFFER)).build();
    private static final MongoProduct VALID_MONGO_PRODUCT = new MongoProduct(
            VALID_PRODUCT.getId(),
            VALID_PRODUCT.getCreatedAt(),
            VALID_PRODUCT.getTitle(),
            VALID_PRODUCT.getDescription(),
            VALID_PRODUCT.getSuggestedPrice(),
            VALID_PRODUCT.getCategories(),
            List.of(VALID_MONGO_OFFER),
            VALID_PRODUCT.getViews());
    private static final Seller VALID_SELLER = new TestSellerBuilder().withProductsSetTo(List.of(VALID_PRODUCT)).build();
    private static final MongoSeller VALID_MONGO_SELLER = new MongoSeller(
            VALID_SELLER.getId(),
            VALID_SELLER.getCreatedAt(),
            VALID_SELLER.getName(),
            VALID_SELLER.getBio(),
            VALID_SELLER.getBirthDate(),
            List.of(VALID_MONGO_PRODUCT));

    private static MongoBuyerAssembler mongoBuyerAssemblerMock;
    private static MongoOfferAssembler mongoOfferAssemblerMock;
    private static MongoProductAssembler mongoProductAssemblerMock;
    private static MongoSellerAssembler mongoSellerAssembler;

    @BeforeEach
    public void setup() {
        initializeMongoAssemblerMocks();

        mongoSellerAssembler = new MongoSellerAssembler(mongoProductAssemblerMock);
    }

    private void initializeMongoAssemblerMocks() {
        mongoBuyerAssemblerMock = Mockito.mock(MongoBuyerAssembler.class);
        mongoOfferAssemblerMock = Mockito.mock(MongoOfferAssembler.class,
                withSettings().useConstructor().outerInstance(mongoBuyerAssemblerMock));
        mongoProductAssemblerMock = Mockito.mock(MongoProductAssembler.class,
                withSettings().useConstructor().outerInstance(mongoOfferAssemblerMock));

        initializeMongoBuyerAssemblerMock();
        initializeMongoOfferAssemblerMock();
        initializeMongoProductAssemblerMock();
    }

    private void initializeMongoBuyerAssemblerMock() {
        Mockito.when(mongoBuyerAssemblerMock.toMongoBuyer(ArgumentMatchers.any(Buyer.class)))
                .thenReturn(VALID_MONGO_BUYER);
        Mockito.when(mongoBuyerAssemblerMock.fromMongoBuyer(ArgumentMatchers.any(MongoBuyer.class)))
                .thenReturn(VALID_BUYER);
    }

    private void initializeMongoOfferAssemblerMock() {
        Mockito.when(mongoOfferAssemblerMock.toMongoEntity(ArgumentMatchers.any(Offer.class)))
                .thenReturn(VALID_MONGO_OFFER);
        Mockito.when(mongoOfferAssemblerMock.fromMongoEntity(ArgumentMatchers.any(MongoOffer.class)))
                .thenReturn(VALID_OFFER);
    }

    private void initializeMongoProductAssemblerMock() {
        Mockito.when(mongoProductAssemblerMock.toMongoEntity(ArgumentMatchers.any(Product.class)))
                .thenReturn(VALID_MONGO_PRODUCT);
        Mockito.when(mongoProductAssemblerMock.fromMongoEntity(ArgumentMatchers.any(MongoProduct.class)))
                .thenReturn(VALID_PRODUCT);
    }

    private Seller getSellerWithNoProducts() {
        return new Seller(
                VALID_SELLER.getId(),
                VALID_SELLER.getCreatedAt(),
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate(),
                new ArrayList<>());
    }

    private MongoSeller getMongoSellerWithNoProducts() {
        return new MongoSeller(
                VALID_MONGO_SELLER.getId(),
                VALID_MONGO_SELLER.getCreatedAt(),
                VALID_MONGO_SELLER.getName(),
                VALID_MONGO_SELLER.getBio(),
                VALID_MONGO_SELLER.getBirthDate(),
                new ArrayList<>());
    }

    @Test
    public void givenNullProductAssembler_whenConvertToMongoSeller_thenThrowsNullPointerException() {
        mongoSellerAssembler = new MongoSellerAssembler(null);

        Assertions.assertThrows(NullPointerException.class, () -> mongoSellerAssembler.toMongoEntity(VALID_SELLER));
    }

    @Test
    public void givenNullProductAssembler_whenConvertToSeller_thenThrowsNullPointerException() {
        mongoSellerAssembler = new MongoSellerAssembler(null);

        Assertions.assertThrows(NullPointerException.class, () -> mongoSellerAssembler.fromMongoEntity(VALID_MONGO_SELLER));
    }

    @Test
    public void givenSellerWithNoProduct_whenConvertToMongoSeller_thenReturnsValidMongoSeller() {
        Seller seller = getSellerWithNoProducts();

        MongoSeller mongoSeller = mongoSellerAssembler.toMongoEntity(seller);

        assertSellerEqualsMongoSeller(seller, mongoSeller);
        Mockito.verify(mongoProductAssemblerMock, times(0))
                .toMongoEntity(any(Product.class));
    }

    @Test
    public void givenSellerWithOneProduct_whenConvertToMongoSeller_thenReturnsValidMongoSeller() {
        MongoSeller mongoSeller = mongoSellerAssembler.toMongoEntity(VALID_SELLER);

        assertSellerEqualsMongoSeller(VALID_SELLER, mongoSeller);
        Mockito.verify(mongoProductAssemblerMock, times(1))
                .toMongoEntity(any(Product.class));
    }

    @Test
    public void givenNullSeller_whenConvertToMongoSeller_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoSellerAssembler.toMongoEntity(null));
    }

    @Test
    public void givenMongoSellerWithOneProduct_whenConvertToSeller_thenReturnsValidSeller() {
        Seller seller = mongoSellerAssembler.fromMongoEntity(VALID_MONGO_SELLER);

        assertMongoSellerEqualsSeller(VALID_MONGO_SELLER, seller);
        Mockito.verify(mongoProductAssemblerMock, times(1))
                .fromMongoEntity(any(MongoProduct.class));
    }

    @Test
    public void givenMongoSellerWithNoProduct_whenConvertToSeller_thenReturnsValidSeller() {
        MongoSeller mongoSeller = getMongoSellerWithNoProducts();

        Seller seller = mongoSellerAssembler.fromMongoEntity(mongoSeller);

        assertMongoSellerEqualsSeller(mongoSeller, seller);
        Mockito.verify(mongoProductAssemblerMock, times(0))
                .fromMongoEntity(any(MongoProduct.class));
    }

    @Test
    public void givenNullMongoSeller_whenConvertToSeller_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoSellerAssembler.fromMongoEntity(null));
    }

    private void assertSellerEqualsMongoSeller(final Seller seller, final MongoSeller mongoSeller) {
        Assertions.assertEquals(seller.getId(), mongoSeller.getId());
        Assertions.assertEquals(seller.getCreatedAt(), mongoSeller.getCreatedAt());
        Assertions.assertEquals(seller.getName(), mongoSeller.getName());
        Assertions.assertEquals(seller.getBio(), mongoSeller.getBio());
        Assertions.assertEquals(seller.getBirthDate(), mongoSeller.getBirthDate());
        Assertions.assertEquals(seller.getProducts().size(), mongoSeller.getProducts().size());
    }

    private void assertMongoSellerEqualsSeller(final MongoSeller mongoSeller, final Seller seller) {
        Assertions.assertEquals(mongoSeller.getId(), seller.getId());
        Assertions.assertEquals(mongoSeller.getCreatedAt(), seller.getCreatedAt());
        Assertions.assertEquals(mongoSeller.getName(), seller.getName());
        Assertions.assertEquals(mongoSeller.getBio(), seller.getBio());
        Assertions.assertEquals(mongoSeller.getBirthDate(), seller.getBirthDate());
        Assertions.assertEquals(mongoSeller.getProducts().size(), seller.getProducts().size());
    }
}
