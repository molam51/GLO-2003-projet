package ulaval.glo2003.infrastructure.mongo.assemblers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoBuyer;
import ulaval.glo2003.utils.TestBuyerBuilder;

public class MongoBuyerAssemblerTest {

    private static final Buyer VALID_BUYER = new TestBuyerBuilder().build();
    private static final MongoBuyer VALID_MONGO_BUYER = new MongoBuyer(
            VALID_BUYER.getName(),
            VALID_BUYER.getEmail(),
            VALID_BUYER.getPhoneNumber());

    private static MongoBuyerAssembler mongoBuyerAssembler;

    @BeforeEach
    public void setup() {
        mongoBuyerAssembler = new MongoBuyerAssembler();
    }

    @Test
    public void givenBuyer_whenConvertToMongoBuyer_thenReturnsMongoBuyer() {
        MongoBuyer mongoBuyer = mongoBuyerAssembler.toMongoBuyer(VALID_BUYER);

        Assertions.assertEquals(VALID_BUYER.getName(), mongoBuyer.getName());
        Assertions.assertEquals(VALID_BUYER.getEmail(), mongoBuyer.getEmail());
        Assertions.assertEquals(VALID_BUYER.getPhoneNumber(), mongoBuyer.getPhoneNumber());
    }

    @Test
    public void givenNullBuyer_whenConvertToMongoBuyer_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoBuyerAssembler.toMongoBuyer(null));
    }

    @Test
    public void givenMongoBuyer_whenConvertToBuyer_thenReturnsBuyer() {
        Buyer buyer = mongoBuyerAssembler.fromMongoBuyer(VALID_MONGO_BUYER);

        Assertions.assertEquals(VALID_MONGO_BUYER.getName(), buyer.getName());
        Assertions.assertEquals(VALID_MONGO_BUYER.getEmail(), buyer.getEmail());
        Assertions.assertEquals(VALID_MONGO_BUYER.getPhoneNumber(), buyer.getPhoneNumber());
    }

    @Test
    public void givenNullMongoBuyer_whenConvertToBuyer_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoBuyerAssembler.fromMongoBuyer(null));
    }
}
