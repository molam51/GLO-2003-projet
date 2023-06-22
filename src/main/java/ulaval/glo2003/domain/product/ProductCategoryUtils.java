package ulaval.glo2003.domain.product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryUtils {

    private ProductCategoryUtils() {

    }

    public static List<String> productCategoriesToStrings(final List<ProductCategory> productCategories) {
        return productCategories.stream()
                .map(ProductCategory::toString)
                .collect(Collectors.toList());
    }
}
