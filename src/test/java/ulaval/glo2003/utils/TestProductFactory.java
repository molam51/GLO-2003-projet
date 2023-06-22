package ulaval.glo2003.utils;

import org.apache.commons.lang3.RandomUtils;
import ulaval.glo2003.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestProductFactory {

    private TestProductFactory() {
    }

    public static List<Product> createRandomProductsUsingBuilder(final int minCount,
                                                                 final int maxCount,
                                                                 final TestBuilder<Product> testProductBuilder) {
        int count = RandomUtils.nextInt(minCount, maxCount);

        return Stream.generate(() -> createRandomProductUsingBuilder(testProductBuilder))
                .limit(count)
                .collect(Collectors.toList());
    }

    public static Product createRandomProductUsingBuilder(final TestBuilder<Product> testProductBuilder) {
        return testProductBuilder.build();
    }
}
