package ulaval.glo2003.ui.common.validation;

import ulaval.glo2003.domain.product.ProductCategory;

public class CategoryValidator extends UserInputValidator {

    @Override
    public boolean isValid(final String input) {
        try {
            ProductCategory.valueOf(input.toUpperCase().strip());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
