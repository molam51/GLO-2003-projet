package ulaval.glo2003.infrastructure.mongo.entities;

import dev.morphia.annotations.Entity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity("Offers")
public class MongoOffer extends MongoEntity {

    private double amount;
    private String message;
    private MongoBuyer buyer;

    public MongoOffer() {

    }

    public MongoOffer(final UUID id, final OffsetDateTime createdAt, final double amount, final String message,
                      final MongoBuyer buyer) {
        super(id, createdAt);

        this.amount = amount;
        this.message = message;
        this.buyer = buyer;
    }

    public final double getAmount() {
        return amount;
    }

    public final MongoBuyer getBuyer() {
        return buyer;
    }

    public final String getMessage() {
        return message;
    }
}
