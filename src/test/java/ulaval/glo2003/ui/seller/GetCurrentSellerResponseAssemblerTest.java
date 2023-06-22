package ulaval.glo2003.ui.seller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.seller.responses.GetCurrentSellerResponse;
import ulaval.glo2003.ui.seller.responses.GetCurrentSellerResponseAssembler;
import ulaval.glo2003.ui.seller.responses.GetCurrentSellerResponseProduct;
import ulaval.glo2003.utils.TestSellerBuilder;

public class GetCurrentSellerResponseAssemblerTest {

    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static GetCurrentSellerResponseAssembler assembler;

    @BeforeAll
    public static void setup() {
        assembler = new GetCurrentSellerResponseAssembler();
    }

    @Test
    public void givenNullProductSeller_whenToResponse_thenThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(null));
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsSellerId() {
        GetCurrentSellerResponse response = assembler.toResponse(VALID_SELLER);

        Assertions.assertEquals(VALID_SELLER.getId(), response.id);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsSellerCreatedAt() {
        GetCurrentSellerResponse response = assembler.toResponse(VALID_SELLER);

        Assertions.assertEquals(VALID_SELLER.getCreatedAt(), response.createdAt);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsSellerName() {
        GetCurrentSellerResponse response = assembler.toResponse(VALID_SELLER);

        Assertions.assertEquals(VALID_SELLER.getName(), response.name);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsSellerBio() {
        GetCurrentSellerResponse response = assembler.toResponse(VALID_SELLER);

        Assertions.assertEquals(VALID_SELLER.getBio(), response.bio);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsSellerBirthDate() {
        GetCurrentSellerResponse response = assembler.toResponse(VALID_SELLER);

        Assertions.assertEquals(VALID_SELLER.getBirthDate(), response.birthDate);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsSellerProducts() {
        GetCurrentSellerResponse response = assembler.toResponse(VALID_SELLER);

        Assertions.assertEquals(VALID_SELLER.getProducts().size(), response.products.size());

        for (int i = 0; i < VALID_SELLER.getProducts().size(); i++) {
            assertProductEqualsResponseProduct(VALID_SELLER.getProducts().get(i), response.products.get(i));
        }
    }

    private void assertProductEqualsResponseProduct(final Product product,
                                                    final GetCurrentSellerResponseProduct responseProduct) {
        Assertions.assertEquals(product.getId(), responseProduct.id);
        Assertions.assertEquals(product.getCreatedAt(), responseProduct.createdAt);
        Assertions.assertEquals(product.getTitle(), responseProduct.title);
        Assertions.assertEquals(product.getDescription(), responseProduct.description);
        Assertions.assertEquals(product.getSuggestedPrice(), responseProduct.suggestedPrice);
        Assertions.assertIterableEquals(product.getCategories(), responseProduct.categories);
        Assertions.assertEquals(product.getOffers().size(), responseProduct.offers.items.size());
    }
}
