package ulaval.glo2003.infrastructure.mongo.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Reference;
import ulaval.glo2003.domain.product.ProductCategory;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity("Products")
public class MongoProduct extends MongoEntity {

    private String title;
    private String description;
    private double suggestedPrice;
    private List<ProductCategory> categories;
    @Reference(ignoreMissing = true)
    private List<MongoOffer> offers;
    private int views;

    public MongoProduct() {

    }

    public MongoProduct(final UUID id, final OffsetDateTime createdAt, final String title, final String description,
                        final double suggestedPrice, final List<ProductCategory> categories,
                        final List<MongoOffer> offers, final int views) {
        super(id, createdAt);

        this.title = title;
        this.description = description;
        this.suggestedPrice = suggestedPrice;
        this.categories = categories;
        this.offers = offers;
        this.views = views;
    }

    public final String getTitle() {
        return title;
    }

    public final String getDescription() {
        return description;
    }

    public final double getSuggestedPrice() {
        return suggestedPrice;
    }

    public final List<ProductCategory> getCategories() {
        return categories;
    }

    public final List<MongoOffer> getOffers() {
        return offers;
    }

    public final int getViews() {
        return views;
    }
}
