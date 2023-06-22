package ulaval.glo2003.utils;

import org.apache.commons.lang3.RandomUtils;
import ulaval.glo2003.domain.offer.Offer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestOfferFactory {

    private TestOfferFactory() {
    }

    public static List<Offer> createRandomOffersUsingBuilder(final int minCount,
                                                             final int maxCount,
                                                             final TestBuilder<Offer> testOfferBuilder) {
        int count = RandomUtils.nextInt(minCount, maxCount);

        return Stream.generate(() -> createRandomOfferUsingBuilder(testOfferBuilder))
                .limit(count)
                .collect(Collectors.toList());
    }

    public static Offer createRandomOfferUsingBuilder(final TestBuilder<Offer> testOfferBuilder) {
        return testOfferBuilder.build();
    }
}
