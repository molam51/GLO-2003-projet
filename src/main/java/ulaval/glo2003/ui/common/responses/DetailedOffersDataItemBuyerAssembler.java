package ulaval.glo2003.ui.common.responses;

import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.ui.common.responses.components.DetailedOffersDataItemBuyer;

public class DetailedOffersDataItemBuyerAssembler implements ResponseAssembler<Buyer, DetailedOffersDataItemBuyer> {

    @Override
    public DetailedOffersDataItemBuyer toResponse(final Buyer buyer) {
        DetailedOffersDataItemBuyer detailedOffersDataItemBuyer = new DetailedOffersDataItemBuyer();
        detailedOffersDataItemBuyer.name = buyer.getName();
        detailedOffersDataItemBuyer.email = buyer.getEmail();
        detailedOffersDataItemBuyer.phoneNumber = buyer.getPhoneNumber();

        return detailedOffersDataItemBuyer;
    }
}
