package ulaval.glo2003.ui.common.validation;

import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.ui.product.ProductUtilities;

public class ProductCategoriesValidator extends UserInputValidator {

    @Override
    public boolean isValid(final String input) {
        if (input.equals("")) {
            return true;
        }

        String[] explodeCategories = input.split(ProductUtilities.CATEGORY_SEPARATOR);
        for (String category : explodeCategories) {
            try {
                ProductCategory.valueOf(category.toUpperCase().strip());
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        return true;
    }
}
