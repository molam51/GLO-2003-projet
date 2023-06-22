package ulaval.glo2003.domain.product;

import java.util.List;

public class ProductCreationParameters {

    private final String title;
    private final String description;
    private final Double suggestedPrice;
    private final List<ProductCategory> categories;

    public ProductCreationParameters(final String title, final String description, final Double suggestedPrice,
                                     final List<ProductCategory> categories) {
        this.title = title;
        this.description = description;
        this.suggestedPrice = suggestedPrice;
        this.categories = categories;
    }

    public final String getTitle() {
        return title;
    }

    public final String getDescription() {
        return description;
    }

    public final Double getSuggestedPrice() {
        return suggestedPrice;
    }

    public final List<ProductCategory> getCategories() {
        return categories;
    }
}
