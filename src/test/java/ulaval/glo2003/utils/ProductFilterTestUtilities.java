package ulaval.glo2003.utils;

import org.apache.commons.lang3.EnumUtils;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductFilterTestUtilities {

    private ProductFilterTestUtilities() {
    }

    public static List<ProductCategory> getUnmatchingCategories(final List<Product> products) {
        Stream<ProductCategory> productCategoryStream = EnumUtils.getEnumList(ProductCategory.class).stream();

        for (Product product : products) {
            productCategoryStream = productCategoryStream
                    .filter(productCategory -> !product.getCategories().contains(productCategory));
        }

        return productCategoryStream.distinct().collect(Collectors.toList());
    }

    public static List<ProductCategory> getMatchingCategories(final List<Product> products) {
        Stream<ProductCategory> productCategoryStream = Stream.empty();

        for (Product product : products) {
            productCategoryStream = Stream.concat(productCategoryStream, product.getCategories().stream());
        }

        return productCategoryStream.distinct().collect(Collectors.toList());
    }

    public static double getMatchingMaxPrice(final List<Product> products) {
        return products.stream().mapToDouble(Product::getSuggestedPrice).max().getAsDouble() + 1.0;
    }

    public static double getUnmatchingMaxPrice(final List<Product> products) {
        return products.stream().mapToDouble(Product::getSuggestedPrice).min().getAsDouble() - 1.0;
    }

    public static double getMatchingMinPrice(final List<Product> products) {
        return products.stream().mapToDouble(Product::getSuggestedPrice).min().getAsDouble() - 1.0;
    }

    public static double getUnmatchingMinPrice(final List<Product> products) {
        return products.stream().mapToDouble(Product::getSuggestedPrice).max().getAsDouble() + 1.0;
    }
}
