package ulaval.glo2003.infrastructure.mongo.assemblers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.infrastructure.mongo.entities.MongoBuyer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoProduct;
import ulaval.glo2003.utils.TestOfferBuilder;
import ulaval.glo2003.utils.TestProductBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.withSettings;

public class MongoProductAssemblerTest {

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

    private static MongoBuyerAssembler mongoBuyerAssemblerMock;
    private static MongoOfferAssembler mongoOfferAssemblerMock;
    private static MongoProductAssembler mongoProductAssembler;

    @BeforeEach
    public void setup() {
        initializeMongoAssemblerMocks();

        mongoProductAssembler = new MongoProductAssembler(mongoOfferAssemblerMock);
    }

    private void initializeMongoAssemblerMocks() {
        mongoBuyerAssemblerMock = Mockito.mock(MongoBuyerAssembler.class);
        mongoOfferAssemblerMock = Mockito.mock(MongoOfferAssembler.class,
                withSettings().useConstructor().outerInstance(mongoBuyerAssemblerMock));

        initializeMongoBuyerAssemblerMock();
        initializeMongoOfferAssemblerMock();
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

    private Product getProductWithNoOffers() {
        return new Product(
                VALID_PRODUCT.getId(),
                VALID_PRODUCT.getCreatedAt(),
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                VALID_PRODUCT.getSuggestedPrice(),
                VALID_PRODUCT.getCategories(),
                new ArrayList<>(),
                VALID_PRODUCT.getViews());
    }

    private MongoProduct getMongoProductWithNoOffers() {
        return new MongoProduct(
                VALID_MONGO_PRODUCT.getId(),
                VALID_MONGO_PRODUCT.getCreatedAt(),
                VALID_MONGO_PRODUCT.getTitle(),
                VALID_MONGO_PRODUCT.getDescription(),
                VALID_MONGO_PRODUCT.getSuggestedPrice(),
                VALID_MONGO_PRODUCT.getCategories(),
                new ArrayList<>(),
                VALID_MONGO_PRODUCT.getViews());
    }

    @Test
    public void givenNullMongoAssembler_whenConvertToMongoProduct_thenThrowsNullPointerException() {
        mongoProductAssembler = new MongoProductAssembler(null);

        Assertions.assertThrows(NullPointerException.class, () -> mongoProductAssembler.toMongoEntity(VALID_PRODUCT));
    }

    @Test
    public void givenNullMongoAssembler_whenConvertToProduct_thenThrowsNullPointerException() {
        mongoProductAssembler = new MongoProductAssembler(null);

        Assertions.assertThrows(NullPointerException.class, () -> mongoProductAssembler.fromMongoEntity(VALID_MONGO_PRODUCT));
    }

    @Test
    public void givenProductWithNoOffers_whenConvertToMongoProduct_thenReturnsValidMongoProduct() {
        Product product = getProductWithNoOffers();

        MongoProduct mongoProduct = mongoProductAssembler.toMongoEntity(product);

        assertProductEqualsMongoProduct(product, mongoProduct);
        Mockito.verify(mongoOfferAssemblerMock, times(0)).toMongoEntity(any(Offer.class));
    }

    @Test
    public void givenProductWithOffers_whenConvertToMongoProduct_thenReturnsValidMongoProduct() {
        MongoProduct mongoProduct = mongoProductAssembler.toMongoEntity(VALID_PRODUCT);

        assertProductEqualsMongoProduct(VALID_PRODUCT, mongoProduct);
        Mockito.verify(mongoOfferAssemblerMock, times(1)).toMongoEntity(any(Offer.class));
    }

    @Test
    public void givenNullProduct_whenConvertToMongoProduct_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoProductAssembler.toMongoEntity(null));
    }

    @Test
    public void givenMongoProductWithNoOffers_whenConvertToProduct_thenReturnsValidProduct() {
        MongoProduct mongoProduct = getMongoProductWithNoOffers();

        Product product = mongoProductAssembler.fromMongoEntity(mongoProduct);

        assertMongoProductEqualsProduct(mongoProduct, product);
        Mockito.verify(mongoOfferAssemblerMock, times(0)).fromMongoEntity(any(MongoOffer.class));
    }

    @Test
    public void givenMongoProductWithOffers_whenConvertToProduct_thenReturnsValidProduct() {
        Product product = mongoProductAssembler.fromMongoEntity(VALID_MONGO_PRODUCT);

        assertMongoProductEqualsProduct(VALID_MONGO_PRODUCT, product);
        Mockito.verify(mongoOfferAssemblerMock, times(1)).fromMongoEntity(any(MongoOffer.class));
    }

    @Test
    public void givenNullMongoProduct_whenConvertToProduct_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoProductAssembler.fromMongoEntity(null));
    }

    private void assertProductEqualsMongoProduct(final Product product, final MongoProduct mongoProduct) {
        Assertions.assertEquals(product.getId(), mongoProduct.getId());
        Assertions.assertEquals(product.getCreatedAt(), mongoProduct.getCreatedAt());
        Assertions.assertEquals(product.getTitle(), mongoProduct.getTitle());
        Assertions.assertEquals(product.getDescription(), mongoProduct.getDescription());
        Assertions.assertEquals(product.getSuggestedPrice(), mongoProduct.getSuggestedPrice());
        Assertions.assertEquals(product.getOffers().size(), mongoProduct.getOffers().size());
        Assertions.assertEquals(product.getViews(), mongoProduct.getViews());
    }

    private void assertMongoProductEqualsProduct(final MongoProduct mongoProduct, final Product product) {
        Assertions.assertEquals(mongoProduct.getId(), product.getId());
        Assertions.assertEquals(mongoProduct.getCreatedAt(), product.getCreatedAt());
        Assertions.assertEquals(mongoProduct.getTitle(), product.getTitle());
        Assertions.assertEquals(mongoProduct.getDescription(), product.getDescription());
        Assertions.assertEquals(mongoProduct.getSuggestedPrice(), product.getSuggestedPrice());
        Assertions.assertEquals(mongoProduct.getOffers().size(), product.getOffers().size());
        Assertions.assertEquals(mongoProduct.getViews(), product.getViews());
    }
}
