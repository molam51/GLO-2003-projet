package ulaval.glo2003.ui.seller.responses;

import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.responses.ResponseAssembler;
import ulaval.glo2003.ui.common.responses.components.DetailedOffersData;

import java.util.List;
import java.util.stream.Collectors;

public class GetCurrentSellerResponseAssembler implements ResponseAssembler<Seller, GetCurrentSellerResponse> {

    @Override
    public GetCurrentSellerResponse toResponse(final Seller seller) {
        GetCurrentSellerResponse responseCurrentSeller = new GetCurrentSellerResponse();
        responseCurrentSeller.id = seller.getId();
        responseCurrentSeller.createdAt = seller.getCreatedAt();
        responseCurrentSeller.name = seller.getName();
        responseCurrentSeller.bio = seller.getBio();
        responseCurrentSeller.birthDate = seller.getBirthDate();
        responseCurrentSeller.products = convertProductsToSellerResponseProducts(seller.getProducts());

        return responseCurrentSeller;
    }

    private List<GetCurrentSellerResponseProduct> convertProductsToSellerResponseProducts(final List<Product> products) {
        return products.stream()
                .map(this::convertProductToResponseProduct)
                .collect(Collectors.toList());
    }

    private GetCurrentSellerResponseProduct convertProductToResponseProduct(final Product product) {
        GetCurrentSellerResponseProduct responseProduct = new GetCurrentSellerResponseProduct();
        responseProduct.id = product.getId();
        responseProduct.createdAt = product.getCreatedAt();
        responseProduct.title = product.getTitle();
        responseProduct.description = product.getDescription();
        responseProduct.suggestedPrice = product.getSuggestedPrice();
        responseProduct.offers = new DetailedOffersData(product.getOffers());
        responseProduct.categories = product.getCategories();

        return responseProduct;
    }
}
