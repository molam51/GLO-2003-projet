package ulaval.glo2003.domain.product;

import ulaval.glo2003.domain.Entity;
import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.offer.Offer;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product extends Entity {
    public static final int DEFAULT_VIEWS = 0;

    private final String title;
    private final String description;
    private final double suggestedPrice;
    private final List<ProductCategory> categories;
    private final List<Offer> offers;
    private int views;

    public Product(final String title, final String description, final double suggestedPrice,
                   final List<ProductCategory> categories) {
        this(title, description, suggestedPrice, categories, new ArrayList<>());
    }

    public Product(final String title, final String description, final double suggestedPrice,
                   final List<ProductCategory> categories, final List<Offer> offers) {
        super();

        this.title = title;
        this.description = description;
        this.suggestedPrice = suggestedPrice;
        this.categories = categories;
        this.offers = offers;
        this.views = DEFAULT_VIEWS;
    }


    public Product(final String title, final String description,
                   final double suggestedPrice, final List<ProductCategory> categories, final List<Offer> offers, final int views) {
        this(title, description, suggestedPrice, categories, offers);

        this.views = views;
    }

    public Product(final UUID id, final OffsetDateTime createdAt, final String title, final String description,
                   final double suggestedPrice, final List<ProductCategory> categories, final List<Offer> offers) {
        super(id, createdAt);

        this.title = title;
        this.description = description;
        this.suggestedPrice = suggestedPrice;
        this.categories = categories;
        this.offers = offers;
        this.views = DEFAULT_VIEWS;
    }

    public Product(final UUID id, final OffsetDateTime createdAt, final String title, final String description,
                   final double suggestedPrice, final List<ProductCategory> categories, final List<Offer> offers, final int views) {
        this(id, createdAt, title, description, suggestedPrice, categories, offers);

        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getSuggestedPrice() {
        return suggestedPrice;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public int getViews() {
        return views;
    }

    public void addOffer(final Offer offer) {
        if (offer == null) {
            throw new NullPointerException();
        }

        if (offer.getAmount() < this.suggestedPrice) {
            throw new InvalidParamException("Amount must be equal or higher to suggested price.");
        }

        if (!offers.contains(offer)) {
            offers.add(offer);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Product)) {
            return false;
        }

        Product product = (Product) object;
        return product.getId().equals(this.getId());
    }

    public void incrementViews() {
        this.views++;
    }
}
