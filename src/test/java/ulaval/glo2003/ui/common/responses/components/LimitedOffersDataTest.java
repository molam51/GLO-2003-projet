package ulaval.glo2003.ui.common.responses.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.utils.TestOfferBuilder;

import java.util.ArrayList;
import java.util.List;

public class LimitedOffersDataTest {

    private static final Offer OFFER_1 = new TestOfferBuilder().build();
    private static final Offer OFFER_2 = new TestOfferBuilder().build();
    private static final int DEFAULT_COUNT_VALUE = 0;
    private static final double MARGIN_OF_ERROR = 0.001;

    @Test
    public void givenOfferList_whenLimitedOffersData_thenConstructsLimitedOfferData() {
        List<Offer> offers = List.of(OFFER_1, OFFER_2);
        double expectedMean = offers.stream().mapToDouble(Offer::getAmount).average().getAsDouble();

        LimitedOffersData limitedOffersData = new LimitedOffersData(offers);

        Assertions.assertEquals(offers.size(), limitedOffersData.count);
        Assertions.assertEquals(expectedMean, limitedOffersData.mean, MARGIN_OF_ERROR);
    }

    @Test
    public void givenNullOfferList_whenLimitedOffersData_thenThrowsNullPointerException() {
        List<Offer> offers = null;

        Assertions.assertThrows(NullPointerException.class, () -> new LimitedOffersData(offers));
    }

    @Test
    public void givenEmptyOfferList_whenLimitedOffersData_thenAssignsDefaultValuesToVariable() {
        List<Offer> offers = new ArrayList<>();

        LimitedOffersData limitedOffersData = new LimitedOffersData(offers);

        Assertions.assertEquals(DEFAULT_COUNT_VALUE, limitedOffersData.count);
        Assertions.assertNull(limitedOffersData.mean);
    }
}
