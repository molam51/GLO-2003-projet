package ulaval.glo2003.domain.seller;

import ulaval.glo2003.domain.EntityFactory;

public class SellerFactory implements EntityFactory<Seller> {

    @Override
    public Seller create(final Object params) {
        SellerCreationParameters parameters = (SellerCreationParameters) params;

        SellerParametersValidator.validate(parameters);

        return new Seller(parameters.getName(), parameters.getBio(), parameters.getBirthDate());
    }
}
