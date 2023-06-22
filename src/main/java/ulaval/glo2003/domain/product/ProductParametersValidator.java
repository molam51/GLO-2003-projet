package ulaval.glo2003.domain.product;

import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;

import java.util.List;

public abstract class ProductParametersValidator {

    public static void validate(final ProductCreationParameters productCreationParameters) {
        validateProductTitle(productCreationParameters.getTitle());
        validateProductDescription(productCreationParameters.getDescription());
        validateProductSuggestedPrice(productCreationParameters.getSuggestedPrice());
        validateProductCategories(productCreationParameters.getCategories());
    }

    private static void validateProductTitle(final String title) {
        if (title == null) {
            throw new MissingParamException("The title is missing");
        }

        if (title.isBlank()) {
            throw new InvalidParamException("The product title must not be empty");
        }
    }

    private static void validateProductDescription(final String description) {
        if (description == null) {
            throw new MissingParamException("The description is missing");
        }

        if (description.isBlank()) {
            throw new InvalidParamException("The product description must not be empty");
        }
    }

    private static void validateProductSuggestedPrice(final Double suggestedPrice) {
        if (suggestedPrice == null) {
            throw new MissingParamException("The suggested price is missing");
        }

        if (suggestedPrice < 1.0) {
            throw new InvalidParamException("The suggested price of the product must be greater than 1$");
        }
    }

    private static void validateProductCategories(final List<ProductCategory> categories) {
        if (categories == null) {
            return;
        }

        if (categories.contains(null)) {
            throw new InvalidParamException("The categories contains a null value");
        }
    }
}
