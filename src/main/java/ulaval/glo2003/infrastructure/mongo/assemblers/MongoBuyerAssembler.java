package ulaval.glo2003.infrastructure.mongo.assemblers;

import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.infrastructure.mongo.entities.MongoBuyer;

public class MongoBuyerAssembler {

    public MongoBuyerAssembler() {
    }

    public MongoBuyer toMongoBuyer(final Buyer buyer) {
        return new MongoBuyer(buyer.getName(), buyer.getEmail(), buyer.getPhoneNumber());
    }

    public Buyer fromMongoBuyer(final MongoBuyer mongoBuyer) {
        return new Buyer(mongoBuyer.getName(), mongoBuyer.getEmail(), mongoBuyer.getPhoneNumber());
    }
}
