package ulaval.glo2003.ui.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.product.responses.GetProductByIdResponse;
import ulaval.glo2003.ui.product.responses.GetProductByIdResponseAssembler;
import ulaval.glo2003.ui.product.responses.GetProductByIdResponseAssemblerArgs;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

public class GetProductByIdResponseAssemblerTest {

    private static final Product PRODUCT = new TestProductBuilder().build();
    private static final Seller PRODUCT_SELLER = new TestSellerBuilder().build();
    private static GetProductByIdResponseAssembler assembler;
    private static GetProductByIdResponseAssemblerArgs assemblerArgs;

    @BeforeAll
    static void setup() {
        assembler = new GetProductByIdResponseAssembler();
        assemblerArgs = new GetProductByIdResponseAssemblerArgs(PRODUCT, PRODUCT_SELLER);
    }

    @Test
    public void givenNullArgs_whenToResponse_thenThrowsNullPointerException() {
        GetProductByIdResponseAssemblerArgs args = null;

        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(args));
    }

    @Test
    public void givenNullProduct_whenToResponse_thenThrowsNullPointerException() {
        GetProductByIdResponseAssemblerArgs args = new GetProductByIdResponseAssemblerArgs(null, PRODUCT_SELLER);

        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(args));
    }

    @Test
    public void givenNullProductSeller_whenToResponse_thenThrowsNullPointerException() {
        GetProductByIdResponseAssemblerArgs args = new GetProductByIdResponseAssemblerArgs(PRODUCT, null);

        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(args));
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductId() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT.getId(), response.id);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductCreatedAt() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT.getCreatedAt(), response.createdAt);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductTitle() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT.getTitle(), response.title);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductDescription() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT.getDescription(), response.description);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductSuggestedPrice() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT.getSuggestedPrice(), response.suggestedPrice);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductCategories() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertIterableEquals(PRODUCT.getCategories(), response.categories);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductSellerId() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT_SELLER.getId(), response.seller.id);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseContainsProductSellerName() {
        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(PRODUCT_SELLER.getName(), response.seller.name);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseProductOffersMeanIsCorrect() {
        double productOffersMean = PRODUCT.getOffers().stream()
                .mapToDouble(Offer::getAmount)
                .average()
                .orElse(0);

        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(productOffersMean, response.offers.mean);
    }

    @Test
    public void givenValidArgs_whenToResponse_thenResponseProductOffersCountIsCorrect() {
        int productOffersCount = PRODUCT.getOffers().size();

        GetProductByIdResponse response = assembler.toResponse(assemblerArgs);

        Assertions.assertEquals(productOffersCount, response.offers.count);
    }
}
