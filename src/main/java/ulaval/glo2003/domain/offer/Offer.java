package ulaval.glo2003.domain.offer;

import ulaval.glo2003.domain.Entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Offer extends Entity {

    private final double amount;
    private final String message;
    private final Buyer buyer;

    public Offer(final double amount, final String message, final Buyer buyer) {
        super();

        this.amount = amount;
        this.message = message;
        this.buyer = buyer;
    }

    public Offer(final UUID id, final OffsetDateTime createdAt, final double amount, final String message,
                 final Buyer buyer) {
        super(id, createdAt);

        this.amount = amount;
        this.message = message;
        this.buyer = buyer;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Offer)) {
            return false;
        }

        Offer offer = (Offer) object;
        return offer.getId().equals(this.getId());
    }

    public double getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    public Buyer getBuyer() {
        return buyer;
    }
}
