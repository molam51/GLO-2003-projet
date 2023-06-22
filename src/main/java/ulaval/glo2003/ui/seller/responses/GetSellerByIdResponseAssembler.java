package ulaval.glo2003.ui.seller.responses;

import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.responses.ResponseAssembler;
import ulaval.glo2003.ui.common.responses.components.LimitedOffersData;

import java.util.List;
import java.util.stream.Collectors;

public class GetSellerByIdResponseAssembler implements ResponseAssembler<Seller, GetSellerByIdResponse> {

    @Override
    public GetSellerByIdResponse toResponse(final Seller seller) {
        GetSellerByIdResponse responseSeller = new GetSellerByIdResponse();
        responseSeller.id = seller.getId();
        responseSeller.createdAt = seller.getCreatedAt();
        responseSeller.name = seller.getName();
        responseSeller.bio = seller.getBio();
        responseSeller.products = convertProductsToResponseProducts(seller.getProducts());

        return responseSeller;
    }

    private List<GetSellerByIdResponseProduct> convertProductsToResponseProducts(final List<Product> products) {
        return products.stream()
                .map(this::convertProductToResponseProduct)
                .collect(Collectors.toList());
    }

    private GetSellerByIdResponseProduct convertProductToResponseProduct(final Product product) {
        GetSellerByIdResponseProduct responseProduct = new GetSellerByIdResponseProduct();
        responseProduct.id = product.getId();
        responseProduct.createdAt = product.getCreatedAt();
        responseProduct.title = product.getTitle();
        responseProduct.description = product.getDescription();
        responseProduct.suggestedPrice = product.getSuggestedPrice();
        responseProduct.offers = new LimitedOffersData(product.getOffers());
        responseProduct.categories = product.getCategories();

        return responseProduct;
    }
}
