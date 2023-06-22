package ulaval.glo2003.domain.product;

import ulaval.glo2003.domain.EntityFactory;

import java.util.List;

public class ProductFactory implements EntityFactory<Product> {

    @Override
    public Product create(final Object params) {
        ProductCreationParameters parameters = (ProductCreationParameters) params;

        ProductParametersValidator.validate(parameters);

        return new Product(
                parameters.getTitle(),
                parameters.getDescription(),
                parameters.getSuggestedPrice(),
                parameters.getCategories() == null ? List.of() : parameters.getCategories());
    }
}
