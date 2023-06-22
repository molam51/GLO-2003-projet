package ulaval.glo2003.e2e;

import dev.morphia.Datastore;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ulaval.glo2003.Main;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategoryUtils;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;

public class PostViewsToProductTest {

    private static final int MIN_SUGGESTED_PRICE_PRODUCT = 100;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT = 200;

    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static final Product VALID_PRODUCT = new TestProductBuilder()
            .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT, MAX_SUGGESTED_PRICE_PRODUCT).build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static String sellerId;
    private static String productId;

    @BeforeAll
    public static void setup() throws IOException {
        Main.startServer(DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());
        E2ETestUtilities.dropCollections(DATASTORE);

        String postSellerRequestBody = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());
        Response postSellerResponse = E2ETestUtilities.sendPostSellerRequest(postSellerRequestBody);
        String sellerLocation = postSellerResponse.getHeader("Location");
        sellerId = sellerLocation.substring(sellerLocation.lastIndexOf('/') + 1);

        String postProductRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));
        Response postProductResponse = E2ETestUtilities.sendPostProductRequest(sellerId, postProductRequestBody);
        String productResponseHeader = postProductResponse.getHeader("Location");
        productId = productResponseHeader.substring(productResponseHeader.lastIndexOf('/') + 1);
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
        Main.shutdownServerNow();
    }

    @Test
    public void givenValidRequest_whenPostViewToProduct_thenReturns200() {
        Response response = E2ETestUtilities.sendPostViewsToProduct(productId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(), response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"019", "error", "error5435"})
    public void givenInvalidProductID_whenPostViewsToProduct_thenReturns404(final String productIdError) {
        Response response = E2ETestUtilities.sendPostViewsToProduct(productIdError);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"019", "error", "error5435"})
    public void givenInvalidProductID_whenPostViewsToProduct_thenReturnsDescriptionError(final String productIdError) {
        Response response = E2ETestUtilities.sendPostViewsToProduct(productIdError);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "ITEM_NOT_FOUND");
        expectedResponseBody.put("description", String.format("Product with ID %s not found.", productIdError));
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }
}
