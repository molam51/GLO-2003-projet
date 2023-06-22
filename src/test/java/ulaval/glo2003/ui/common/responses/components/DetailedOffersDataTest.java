package ulaval.glo2003.ui.common.responses.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.utils.TestOfferBuilder;

import java.util.ArrayList;
import java.util.List;

public class DetailedOffersDataTest {

    private static final int DEFAULT_COUNT_VALUE = 0;
    private static final double MARGIN_OF_ERROR = 0.001;
    private static final Offer OFFER_1 = new TestOfferBuilder().build();
    private static final Offer OFFER_2 = new TestOfferBuilder().build();
    private static final Offer OFFER_3 = new TestOfferBuilder().build();

    @Test
    public void givenOfferList_whenDetailedOffersData_thenConstructsDetailedOffersData() {
        List<Offer> offers = List.of(OFFER_1, OFFER_2, OFFER_3);
        double expectedMin = offers.stream().mapToDouble(Offer::getAmount).min().getAsDouble();
        double expectedMax = offers.stream().mapToDouble(Offer::getAmount).max().getAsDouble();
        double expectedMean = offers.stream().mapToDouble(Offer::getAmount).average().getAsDouble();

        DetailedOffersData detailedOffersData = new DetailedOffersData(offers);

        Assertions.assertEquals(expectedMin, detailedOffersData.min);
        Assertions.assertEquals(expectedMax, detailedOffersData.max);
        Assertions.assertEquals(offers.size(), detailedOffersData.count);
        Assertions.assertEquals(expectedMean, detailedOffersData.mean, MARGIN_OF_ERROR);
        Assertions.assertEquals(offers.size(), detailedOffersData.items.size());
        Assertions.assertTrue(itemsAreValid(detailedOffersData.items, offers));
    }

    @Test
    public void givenEmptyOfferList_whenDetailedOffersData_thenAssignsDefaultValueToVariables() {
        List<Offer> offers = new ArrayList<>();

        DetailedOffersData detailedOffersData = new DetailedOffersData(offers);

        Assertions.assertNull(detailedOffersData.min);
        Assertions.assertNull(detailedOffersData.max);
        Assertions.assertNull(detailedOffersData.mean);
        Assertions.assertEquals(DEFAULT_COUNT_VALUE, detailedOffersData.count);
        Assertions.assertTrue(detailedOffersData.items.isEmpty());
    }

    @Test
    public void givenNullOfferList_whenDetailedOffersData_thenThrowsNullPointerException() {
        List<Offer> offers = null;

        Assertions.assertThrows(NullPointerException.class, () -> new DetailedOffersData(offers));
    }

    private boolean itemsAreValid(final List<DetailedOffersDataItem> items, final List<Offer> offers) {
        for (int i = 0; i < items.size(); i++) {
            if (!itemIsValid(items.get(i), offers.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean itemIsValid(final DetailedOffersDataItem item, final Offer offer) {
        return item.id == offer.getId()
                && item.message.equals(offer.getMessage())
                && item.amount == offer.getAmount()
                && item.createdAt == offer.getCreatedAt()
                && item.buyer.name.equals(offer.getBuyer().getName())
                && item.buyer.email.equals(offer.getBuyer().getEmail())
                && item.buyer.phoneNumber.equals(offer.getBuyer().getPhoneNumber());
    }
}
