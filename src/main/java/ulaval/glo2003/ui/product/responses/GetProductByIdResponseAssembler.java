package ulaval.glo2003.ui.product.responses;

import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.responses.ResponseAssembler;
import ulaval.glo2003.ui.common.responses.components.LimitedOffersData;

public class GetProductByIdResponseAssembler implements ResponseAssembler<
        GetProductByIdResponseAssemblerArgs,
        GetProductByIdResponse> {

    @Override
    public GetProductByIdResponse toResponse(final GetProductByIdResponseAssemblerArgs args) {
        GetProductByIdResponse responseProduct = new GetProductByIdResponse();
        responseProduct.id = args.getProduct().getId();
        responseProduct.createdAt = args.getProduct().getCreatedAt();
        responseProduct.title = args.getProduct().getTitle();
        responseProduct.description = args.getProduct().getDescription();
        responseProduct.suggestedPrice = args.getProduct().getSuggestedPrice();
        responseProduct.categories = args.getProduct().getCategories();
        responseProduct.seller = convertSellerToResponseSeller(args.getProductSeller());
        responseProduct.offers = new LimitedOffersData(args.getProduct().getOffers());

        return responseProduct;
    }

    private GetProductByIdResponseSeller convertSellerToResponseSeller(final Seller seller) {
        GetProductByIdResponseSeller responseSeller = new GetProductByIdResponseSeller();
        responseSeller.id = seller.getId();
        responseSeller.name = seller.getName();

        return responseSeller;
    }
}
