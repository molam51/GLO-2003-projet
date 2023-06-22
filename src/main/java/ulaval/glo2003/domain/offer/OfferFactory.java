package ulaval.glo2003.domain.offer;

import ulaval.glo2003.domain.EntityFactory;

public class OfferFactory implements EntityFactory<Offer> {

    @Override
    public Offer create(final Object params) {
        OfferCreationParameters offerParameters = (OfferCreationParameters) params;
        BuyerCreationParameters buyerParameters = new BuyerCreationParameters(
                offerParameters.getName(),
                offerParameters.getEmail(),
                offerParameters.getPhoneNumber());

        BuyerParametersValidator.validate(buyerParameters);
        OfferParametersValidator.validate(offerParameters);

        Buyer buyer = new Buyer(buyerParameters.getName(), buyerParameters.getEmail(), buyerParameters.getPhoneNumber());

        return new Offer(offerParameters.getAmount(), offerParameters.getMessage(), buyer);
    }
}
