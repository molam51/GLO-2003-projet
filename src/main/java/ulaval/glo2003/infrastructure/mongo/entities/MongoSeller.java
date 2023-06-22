package ulaval.glo2003.infrastructure.mongo.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Reference;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity("Sellers")
public class MongoSeller extends MongoEntity {

    private String name;
    private String bio;
    private LocalDate birthDate;
    @Reference(ignoreMissing = true)
    private List<MongoProduct> products;

    public MongoSeller() {

    }

    public MongoSeller(final UUID id, final OffsetDateTime createdAt, final String name, final String bio,
                       final LocalDate birthDate, final List<MongoProduct> products) {
        super(id, createdAt);

        this.name = name;
        this.bio = bio;
        this.birthDate = birthDate;
        this.products = products;
    }

    public final String getName() {
        return name;
    }

    public final String getBio() {
        return bio;
    }

    public final LocalDate getBirthDate() {
        return birthDate;
    }

    public final List<MongoProduct> getProducts() {
        return products;
    }
}
