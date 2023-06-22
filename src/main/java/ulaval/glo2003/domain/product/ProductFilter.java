package ulaval.glo2003.domain.product;

import ulaval.glo2003.domain.EntityFilter;
import ulaval.glo2003.domain.seller.Seller;

import java.util.ArrayList;
import java.util.List;

public class ProductFilter implements EntityFilter<Product> {

    private final Seller seller;
    private String title;
    private List<ProductCategory> categories;
    private Double minPrice;
    private Double maxPrice;

    public ProductFilter(final Seller seller, final String title, final List<ProductCategory> categories,
                         final Double minPrice, final Double maxPrice) {
        this.seller = seller;
        this.title = title;
        this.categories = categories;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public ProductFilter() {
        this.seller = null;
        this.title = null;
        this.categories = null;
        this.minPrice = null;
        this.maxPrice = null;
    }

    @Override
    public List<Product> filterEntities(final List<Product> entities) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : entities) {
            if (isMatching(product)) {
                filteredProducts.add(product);
            }
        }

        return filteredProducts;
    }

    private boolean isMatching(final Product product) {
        return productOwnedBySeller(product)
                && productTitleContainsFilterTitle(product)
                && productCategoryIsInFilterCategories(product)
                && productPriceIsHigherThanFilterMinPrice(product)
                && productPriceIsLowerThanFilterMaxPrice(product);
    }

    private boolean productOwnedBySeller(final Product product) {
        if (seller == null) {
            return true;
        }

        return seller.getProducts().contains(product);
    }

    private boolean productTitleContainsFilterTitle(final Product product) {
        return title == null || product.getTitle().toLowerCase().contains(title.toLowerCase());
    }

    private boolean productCategoryIsInFilterCategories(final Product product) {
        if (categories == null) {
            return true;
        }

        if (categories.isEmpty()) {
            return true;
        }

        for (ProductCategory category : product.getCategories()) {
            if (categories.contains(category)) {
                return true;
            }
        }

        return false;
    }

    private boolean productPriceIsHigherThanFilterMinPrice(final Product product) {
        return minPrice == null || product.getSuggestedPrice() >= minPrice;
    }

    private boolean productPriceIsLowerThanFilterMaxPrice(final Product product) {
        return maxPrice == null || product.getSuggestedPrice() <= maxPrice;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(final List<ProductCategory> categories) {
        this.categories = categories;
    }

    public void setMinPrice(final Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(final Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
