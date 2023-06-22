package ulaval.glo2003.ui.common.responses;

import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.ui.common.responses.components.DetailedOffersDataItem;

public class DetailedOffersDataItemAssembler implements ResponseAssembler<Offer, DetailedOffersDataItem> {

    @Override
    public final DetailedOffersDataItem toResponse(final Offer offer) {
        DetailedOffersDataItemBuyerAssembler detailedOffersDataItemBuyerAssembler = new DetailedOffersDataItemBuyerAssembler();
        DetailedOffersDataItem detailedOffersDataItem = new DetailedOffersDataItem();

        detailedOffersDataItem.id = offer.getId();
        detailedOffersDataItem.createdAt = offer.getCreatedAt();
        detailedOffersDataItem.amount = offer.getAmount();
        detailedOffersDataItem.message = offer.getMessage();
        detailedOffersDataItem.buyer = detailedOffersDataItemBuyerAssembler.toResponse(offer.getBuyer());

        return detailedOffersDataItem;
    }
}
