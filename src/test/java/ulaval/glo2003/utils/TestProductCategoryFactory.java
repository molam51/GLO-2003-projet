package ulaval.glo2003.utils;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomUtils;
import ulaval.glo2003.domain.product.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestProductCategoryFactory {

    private static List<ProductCategory> availableCategories = EnumUtils.getEnumList(ProductCategory.class);

    private TestProductCategoryFactory() {
    }

    public static List<ProductCategory> createRandomProductCategories(
            final int minCount,
            final int maxCount,
            final List<ProductCategory> exclude) {
        availableCategories = Stream.of(ProductCategory.values())
                .filter(productCategory -> !exclude.contains(productCategory))
                .collect(Collectors.toList());

        int count = RandomUtils.nextInt(minCount, maxCount);

        return Stream.generate(TestProductCategoryFactory::createRandomProductCategory)
                .limit(count)
                .collect(Collectors.toList());
    }

    public static ProductCategory createRandomProductCategory() {
        int index = RandomUtils.nextInt(0, availableCategories.size());

        return availableCategories.get(index);
    }
}
