package ulaval.glo2003.ui.product.responses;

import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.responses.CollectionResponseAssembler;
import ulaval.glo2003.ui.common.responses.components.LimitedOffersData;

import java.util.ArrayList;
import java.util.List;

public class FilterProductsResponseAssembler implements CollectionResponseAssembler<
        FilterProductsResponseAssemblerArgsEntry,
        FilterProductsResponse> {

    @Override
    public FilterProductsResponse toResponse(final List<FilterProductsResponseAssemblerArgsEntry> entries) {
        FilterProductsResponse response = new FilterProductsResponse();
        response.products = convertEntriesToResponseProducts(entries);

        return response;
    }

    private List<FilterProductsResponseProduct> convertEntriesToResponseProducts(
            final List<FilterProductsResponseAssemblerArgsEntry> entries) {
        List<FilterProductsResponseProduct> responseProducts = new ArrayList<>();
        for (FilterProductsResponseAssemblerArgsEntry entry : entries) {
            responseProducts.add(convertEntryToResponseProduct(entry));
        }

        return responseProducts;
    }

    private FilterProductsResponseProduct convertEntryToResponseProduct(
            final FilterProductsResponseAssemblerArgsEntry entry) {
        FilterProductsResponseProduct responseProduct = new FilterProductsResponseProduct();
        responseProduct.id = entry.getProduct().getId();
        responseProduct.createdAt = entry.getProduct().getCreatedAt();
        responseProduct.title = entry.getProduct().getTitle();
        responseProduct.description = entry.getProduct().getDescription();
        responseProduct.suggestedPrice = entry.getProduct().getSuggestedPrice();
        responseProduct.categories = entry.getProduct().getCategories();
        responseProduct.seller = convertSellerToResponseSeller(entry.getProductSeller());
        responseProduct.offers = new LimitedOffersData(entry.getProduct().getOffers());

        return responseProduct;
    }

    private FilterProductsResponseSeller convertSellerToResponseSeller(final Seller seller) {
        FilterProductsResponseSeller responseSeller = new FilterProductsResponseSeller();
        responseSeller.id = seller.getId();
        responseSeller.name = seller.getName();

        return responseSeller;
    }
}
