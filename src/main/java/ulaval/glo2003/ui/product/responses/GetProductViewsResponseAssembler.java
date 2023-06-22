package ulaval.glo2003.ui.product.responses;

import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.ui.common.responses.ResponseAssembler;

public class GetProductViewsResponseAssembler implements ResponseAssembler<
        Product,
        GetProductViewsResponse> {

    @Override
    public GetProductViewsResponse toResponse(final Product product) {
        GetProductViewsResponse responseViews = new GetProductViewsResponse();
        responseViews.views = product.getViews();

        return responseViews;
    }
}
