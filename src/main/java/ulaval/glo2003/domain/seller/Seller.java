package ulaval.glo2003.domain.seller;

import ulaval.glo2003.domain.Entity;
import ulaval.glo2003.domain.product.Product;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Seller extends Entity {

    private final String name;
    private final String bio;
    private final LocalDate birthDate;
    private final List<Product> products;

    public Seller(final String name, final String bio, final LocalDate birthDate) {
        this(name, bio, birthDate, new ArrayList<>());
    }

    public Seller(final String name, final String bio, final LocalDate birthDate, final List<Product> products) {
        super();

        this.name = name;
        this.bio = bio;
        this.birthDate = birthDate;
        this.products = products;
    }

    public Seller(final UUID id, final OffsetDateTime createdAt, final String name, final String bio,
                  final LocalDate birthDate, final List<Product> products) {
        super(id, createdAt);

        this.name = name;
        this.bio = bio;
        this.birthDate = birthDate;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(final Product product) {
        if (product == null) {
            throw new NullPointerException();
        }

        if (!products.contains(product)) {
            products.add(product);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Seller)) {
            return false;
        }

        Seller seller = (Seller) object;
        return seller.getId().equals(this.getId());
    }
}
