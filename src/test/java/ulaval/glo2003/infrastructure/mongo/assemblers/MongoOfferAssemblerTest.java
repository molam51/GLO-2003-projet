package ulaval.glo2003.infrastructure.mongo.assemblers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoBuyer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoOffer;
import ulaval.glo2003.utils.TestOfferBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class MongoOfferAssemblerTest {

    private static final Offer VALID_OFFER = new TestOfferBuilder().build();
    private static final Buyer VALID_BUYER = VALID_OFFER.getBuyer();
    private static final MongoBuyer VALID_MONGO_BUYER = new MongoBuyer(
            VALID_BUYER.getName(),
            VALID_BUYER.getEmail(),
            VALID_BUYER.getPhoneNumber());
    private final MongoOffer validMongoOffer = new MongoOffer(
            VALID_OFFER.getId(),
            VALID_OFFER.getCreatedAt(),
            VALID_OFFER.getAmount(),
            VALID_OFFER.getMessage(),
            VALID_MONGO_BUYER);

    private static MongoBuyerAssembler mongoBuyerAssemblerMock;
    private static MongoOfferAssembler mongoOfferAssembler;

    @BeforeEach
    public void setup() {
        initializeMongoAssemblerMocks();

        mongoOfferAssembler = new MongoOfferAssembler(mongoBuyerAssemblerMock);
    }

    private void initializeMongoAssemblerMocks() {
        mongoBuyerAssemblerMock = Mockito.mock(MongoBuyerAssembler.class);

        initializeMongoOfferAssemblerMock();
    }

    private void initializeMongoOfferAssemblerMock() {
        Mockito.when(mongoBuyerAssemblerMock.toMongoBuyer(ArgumentMatchers.any(Buyer.class)))
                .thenReturn(VALID_MONGO_BUYER);
        Mockito.when(mongoBuyerAssemblerMock.fromMongoBuyer(ArgumentMatchers.any(MongoBuyer.class)))
                .thenReturn(VALID_BUYER);
    }

    @Test
    public void givenNullProductAssemblerWithOffer_whenConvertToMongoOffer_thenThrowsNullPointerException() {
        mongoOfferAssembler = new MongoOfferAssembler(null);

        Assertions.assertThrows(NullPointerException.class, () -> mongoOfferAssembler.toMongoEntity(VALID_OFFER));
    }

    @Test
    public void givenNullProductAssemblerWithMongoOffer_whenConvertToOffer_thenThrowsNullPointerException() {
        mongoOfferAssembler = new MongoOfferAssembler(null);

        Assertions.assertThrows(NullPointerException.class, () -> mongoOfferAssembler.fromMongoEntity(validMongoOffer));
    }

    @Test
    public void givenOffer_whenConvertToMongoOffer_thenReturnsMongoOffer() {
        MongoOffer mongoOffer = mongoOfferAssembler.toMongoEntity(VALID_OFFER);

        Assertions.assertEquals(VALID_OFFER.getId(), mongoOffer.getId());
        Assertions.assertEquals(VALID_OFFER.getCreatedAt(), mongoOffer.getCreatedAt());
        Assertions.assertEquals(VALID_OFFER.getAmount(), mongoOffer.getAmount());
        Assertions.assertEquals(VALID_OFFER.getMessage(), mongoOffer.getMessage());
        Mockito.verify(mongoBuyerAssemblerMock, times(1)).toMongoBuyer(any(Buyer.class));
    }

    @Test
    public void givenNullOffer_whenConvertToMongoOffer_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoOfferAssembler.toMongoEntity(null));
    }

    @Test
    public void givenMongoOffer_whenConvertToOffer_thenReturnsOffer() {
        Offer offer = mongoOfferAssembler.fromMongoEntity(validMongoOffer);

        Assertions.assertEquals(validMongoOffer.getId(), offer.getId());
        Assertions.assertEquals(validMongoOffer.getCreatedAt(), offer.getCreatedAt());
        Assertions.assertEquals(validMongoOffer.getAmount(), offer.getAmount());
        Assertions.assertEquals(validMongoOffer.getMessage(), offer.getMessage());
        Mockito.verify(mongoBuyerAssemblerMock, times(1)).fromMongoBuyer(any(MongoBuyer.class));
    }

    @Test
    public void givenNullMongoOffer_whenConvertToOffer_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoOfferAssembler.fromMongoEntity(null));
    }
}
