package ulaval.glo2003.ui.product.responses;

import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;

public class GetProductByIdResponseAssemblerArgs {

    private final Product product;
    private final Seller productSeller;

    public GetProductByIdResponseAssemblerArgs(final Product product, final Seller productSeller) {
        this.product = product;
        this.productSeller = productSeller;
    }

    public Product getProduct() {
        return this.product;
    }

    public Seller getProductSeller() {
        return this.productSeller;
    }
}
