package ulaval.glo2003.e2e;

import dev.morphia.Datastore;
import io.restassured.RestAssured;
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
import ulaval.glo2003.ui.common.errors.Error;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;

public class GetProductViewsTest {
    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static final Product VALID_PRODUCT = new TestProductBuilder().build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();
    private static String sellerId;
    private static String productId;

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.baseURI = String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products");

        E2ETestUtilities.dropCollections(DATASTORE);

        Main.startServer(
                DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());

        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response postSellerResponse = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        sellerId = E2ETestUtilities.parseUUIDFromHeaderLocation(postSellerResponse).toString();

        String postProductRequestJson = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response postProductResponse = E2ETestUtilities.sendPostProductRequest(sellerId, postProductRequestJson);

        productId = E2ETestUtilities.parseUUIDFromHeaderLocation(postProductResponse).toString();
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
        Main.shutdownServerNow();
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "\n\t", "904e301f-3c12-46d9-8e45", " "})
    public void givenInvalidProductId_whenGetProductViews_thenReturns404(final String productId) {
        Response response = E2ETestUtilities.sendGetProductViewsRequest(productId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "\n\t", "904e301f-3c12-46d9-8e45", " "})
    public void givenInvalidProductId_whenGetProductViews_thenReturnsCorrectErrorDescription(final String productId) {
        Response response = E2ETestUtilities.sendGetProductViewsRequest(productId);

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.ITEM_NOT_FOUND, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                String.format("Product with ID %s not found.", productId),
                responseBodyJsonObject.getString("description"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a82e74bd-1f9d-4548-8bf7-1ec490317042", "7a2e98ee-10d6-441b-b601-2164b8559885"})
    public void givenNonexistentProductId_whenGetProductViews_thenReturns404(final String productId) {
        Response response = E2ETestUtilities.sendGetProductViewsRequest(productId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a82e74bd-1f9d-4548-8bf7-1ec490317042", "7a2e98ee-10d6-441b-b601-2164b8559885"})
    public void givenNonexistentProductId_whenGetProductViews_thenReturnsCorrectErrorDescription(final String productId) {
        Response response = E2ETestUtilities.sendGetProductViewsRequest(productId);

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.ITEM_NOT_FOUND, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                String.format("Product with ID %s not found.", productId),
                responseBodyJsonObject.getString("description"));
    }

    @Test
    public void givenValidRequest_whenGetProductViews_thenReturns200() {
        Response response = E2ETestUtilities.sendGetProductViewsRequest(productId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(), response.getStatusCode());
    }

    @Test
    public void givenValidRequest_whenGetProductViews_thenReturnsViews() {
        Response response = E2ETestUtilities.sendGetProductViewsRequest(productId);

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Product.DEFAULT_VIEWS, responseBodyJsonObject.getInt("views"));
    }
}
