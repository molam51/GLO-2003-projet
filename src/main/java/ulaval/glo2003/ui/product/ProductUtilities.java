package ulaval.glo2003.ui.product;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.ProductNotFoundException;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.ui.common.validation.AmountValidator;
import ulaval.glo2003.ui.common.validation.CategoryValidator;
import ulaval.glo2003.ui.common.validation.DoubleValidator;
import ulaval.glo2003.ui.common.validation.UUIDValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductUtilities {

    public static final String CATEGORY_SEPARATOR = ",";

    private ProductUtilities() {

    }

    public static Double parseStringToSuggestedPrice(final String suggestedPrice) {
        DoubleValidator doubleValidator = new DoubleValidator();

        if (suggestedPrice == null) {
            return null;
        } else if (!doubleValidator.isValid(suggestedPrice)) {
            throw new InvalidParamException("`suggestedPrice` is not a number");
        } else if (Double.parseDouble(suggestedPrice) < 1.0) {
            throw new InvalidParamException("`suggestedPrice` is too low");
        }

        return Double.parseDouble(suggestedPrice);
    }

    public static Double parseStringToMinPrice(final String requestMinPrice) {
        AmountValidator amountValidator = new AmountValidator();

        if (requestMinPrice == null) {
            return null;
        } else if (!amountValidator.isValid(requestMinPrice)) {
            throw new InvalidParamException("`minPrice` is invalid");
        }

        return Double.parseDouble(requestMinPrice);
    }

    public static Double parseStringToMaxPrice(final String requestMaxPrice) {
        AmountValidator amountValidator = new AmountValidator();

        if (requestMaxPrice == null) {
            return null;
        } else if (!amountValidator.isValid(requestMaxPrice)) {
            throw new InvalidParamException("`maxPrice` is invalid");
        }

        return Double.parseDouble(requestMaxPrice);
    }

    public static UUID parseStringToProductUUID(final String requestProductId) {
        UUIDValidator uuidValidator = new UUIDValidator();

        if (requestProductId == null) {
            return null;
        } else if (!uuidValidator.isValid(requestProductId)) {
            throw new ProductNotFoundException(requestProductId);
        }

        return UUID.fromString(requestProductId);
    }

    public static List<ProductCategory> parseStringsToProductCategories(final List<String> requestCategories) {
        if (requestCategories == null) {
            return null;
        }

        List<ProductCategory> categories = new ArrayList<>();
        for (String category : requestCategories) {
            categories.add(parseStringToProductCategory(category));
        }

        return categories;
    }

    private static ProductCategory parseStringToProductCategory(final String category) {
        CategoryValidator categoryValidator = new CategoryValidator();

        if (category == null) {
            throw new InvalidParamException("One of the categories is null");
        } else if (!categoryValidator.isValid(category)) {
            throw new InvalidParamException("One of the categories is invalid");
        }

        return ProductCategory.valueOf(category.toUpperCase().strip());
    }
}
