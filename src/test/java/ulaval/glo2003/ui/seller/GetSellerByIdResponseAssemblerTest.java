package ulaval.glo2003.ui.seller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.seller.responses.GetSellerByIdResponse;
import ulaval.glo2003.ui.seller.responses.GetSellerByIdResponseAssembler;
import ulaval.glo2003.ui.seller.responses.GetSellerByIdResponseProduct;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.util.List;

public class GetSellerByIdResponseAssemblerTest {

    private static final Seller SELLER = new TestSellerBuilder().build();
    private static GetSellerByIdResponseAssembler assembler;

    @BeforeAll
    public static void setup() {
        assembler = new GetSellerByIdResponseAssembler();

        SELLER.addProduct(new TestProductBuilder().build());
        SELLER.addProduct(new TestProductBuilder().build());
        SELLER.addProduct(new TestProductBuilder().build());
    }

    @Test
    public void givenNullSeller_whenToResponse_thenThrowsNullPointerException() {
        Seller seller = null;

        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(seller));
    }

    @Test
    public void givenSeller_whenToResponse_thenResponseContainsSellerId() {
        GetSellerByIdResponse response = assembler.toResponse(SELLER);

        Assertions.assertEquals(SELLER.getId(), response.id);
    }

    @Test
    public void givenSeller_whenToResponse_thenResponseContainsSellerCreatedAt() {
        GetSellerByIdResponse response = assembler.toResponse(SELLER);

        Assertions.assertEquals(SELLER.getCreatedAt(), response.createdAt);
    }

    @Test
    public void givenSeller_whenToResponse_thenResponseContainsSellerName() {
        GetSellerByIdResponse response = assembler.toResponse(SELLER);

        Assertions.assertEquals(SELLER.getName(), response.name);
    }

    @Test
    public void givenSeller_whenToResponse_thenResponseContainsSellerBio() {
        GetSellerByIdResponse response = assembler.toResponse(SELLER);

        Assertions.assertEquals(SELLER.getBio(), response.bio);
    }

    @Test
    public void givenSeller_whenToResponse_thenResponseContainsSellerProducts() {
        GetSellerByIdResponse response = assembler.toResponse(SELLER);

        assertProductsEqualsResponseProducts(SELLER.getProducts(), response.products);
    }

    private void assertProductsEqualsResponseProducts(final List<Product> products,
                                                      final List<GetSellerByIdResponseProduct> responseProducts) {
        Assertions.assertEquals(products.size(), responseProducts.size());

        for (int i = 0; i < products.size(); i++) {
            assertProductEqualsResponseProduct(products.get(i), responseProducts.get(i));
        }
    }

    private void assertProductEqualsResponseProduct(final Product product,
                                                    final GetSellerByIdResponseProduct responseProduct) {
        Assertions.assertEquals(product.getId(), responseProduct.id);
        Assertions.assertEquals(product.getCreatedAt(), responseProduct.createdAt);
        Assertions.assertEquals(product.getTitle(), responseProduct.title);
        Assertions.assertEquals(product.getDescription(), responseProduct.description);
        Assertions.assertEquals(product.getSuggestedPrice(), responseProduct.suggestedPrice);
        Assertions.assertIterableEquals(product.getCategories(), responseProduct.categories);
        Assertions.assertEquals(product.getOffers().size(), responseProduct.offers.count);
    }
}
