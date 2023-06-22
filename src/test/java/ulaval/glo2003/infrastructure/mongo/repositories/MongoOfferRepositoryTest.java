package ulaval.glo2003.infrastructure.mongo.repositories;

import dev.morphia.Datastore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.exceptions.OfferDuplicateException;
import ulaval.glo2003.domain.exceptions.OfferNotFoundException;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoBuyerAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoOfferAssembler;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestOfferBuilder;

import java.util.List;
import java.util.UUID;

public class MongoOfferRepositoryTest {

    private static final Offer OFFER_1 = new TestOfferBuilder().build();
    private static final Offer OFFER_2 = new TestOfferBuilder().build();
    private static final Offer OFFER_3 = new TestOfferBuilder().build();

    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static MongoOfferRepository mongoOfferRepository;

    @BeforeAll
    public static void setup() {
        MongoBuyerAssembler mongoBuyerAssembler = new MongoBuyerAssembler();
        MongoOfferAssembler mongoOfferAssembler = new MongoOfferAssembler(mongoBuyerAssembler);

        E2ETestUtilities.dropCollections(DATASTORE);

        mongoOfferRepository = new MongoOfferRepository(DATASTORE, mongoOfferAssembler);
    }

    @AfterEach
    public void teardownAfterEach() {
        E2ETestUtilities.dropCollections(DATASTORE);
    }

    @Test
    public void givenValidOffer_whenAdd_thenAddsOfferToRepository() {
        UUID offerId = OFFER_1.getId();

        mongoOfferRepository.add(OFFER_1);

        Assertions.assertEquals(OFFER_1, mongoOfferRepository.fetch(offerId));
    }

    @Test
    public void givenMultipleValidOffers_whenAdd_thenAddsOffersToRepository() {
        UUID offer1Id = OFFER_1.getId();
        UUID offer2Id = OFFER_2.getId();

        mongoOfferRepository.add(OFFER_1);
        mongoOfferRepository.add(OFFER_2);

        Assertions.assertEquals(OFFER_1, mongoOfferRepository.fetch(offer1Id));
        Assertions.assertEquals(OFFER_2, mongoOfferRepository.fetch(offer2Id));
    }

    @Test
    public void givenExistingOfferId_whenAdd_thenThrowsOfferDuplicateException() {
        mongoOfferRepository.add(OFFER_1);

        Assertions.assertThrows(OfferDuplicateException.class, () -> mongoOfferRepository.add(OFFER_1));
    }

    @Test
    public void givenANullOffer_whenAdd_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoOfferRepository.add(null));
    }

    @Test
    public void givenExistingOfferId_whenFetch_thenReturnsValidOfferWithSaidId() {
        UUID offerId = OFFER_1.getId();
        mongoOfferRepository.add(OFFER_1);

        Offer fetchedOffer = mongoOfferRepository.fetch(offerId);

        Assertions.assertEquals(OFFER_1, fetchedOffer);
        Assertions.assertEquals(offerId, fetchedOffer.getId());
    }

    @Test
    public void givenANullId_whenFetch_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> mongoOfferRepository.fetch(null));
    }

    @Test
    public void givenNonexistentOfferId_whenFetch_thenThrowsOfferNotFoundException() {
        UUID offerId = OFFER_1.getId();

        Assertions.assertThrows(OfferNotFoundException.class, () -> mongoOfferRepository.fetch(offerId));
    }

    @Test
    public void givenRepositoryWithOffers_whenGetAll_thenReturnsAllOffers() {
        mongoOfferRepository.add(OFFER_1);
        mongoOfferRepository.add(OFFER_2);
        mongoOfferRepository.add(OFFER_3);

        List<Offer> retrievedOffers = mongoOfferRepository.getAll();

        Assertions.assertIterableEquals(List.of(OFFER_1, OFFER_2, OFFER_3), retrievedOffers);
    }

    @Test
    public void givenEmptyRepository_whenGetAll_thenReturnsNoOffers() {
        List<Offer> retrievedOffers = mongoOfferRepository.getAll();

        Assertions.assertTrue(retrievedOffers.isEmpty());
    }
}
