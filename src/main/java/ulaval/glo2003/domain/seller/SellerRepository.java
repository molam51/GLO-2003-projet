package ulaval.glo2003.domain.seller;

import ulaval.glo2003.domain.EntityRepository;

import java.util.UUID;

public abstract class SellerRepository implements EntityRepository<Seller> {

    public abstract Seller fetchByProduct(UUID productId);
}
