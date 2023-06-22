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
import java.util.UUID;

public final class GetSellerByIdTest {

    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static final Product VALID_PRODUCT = new TestProductBuilder().build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static UUID sellerId;
    private static UUID productId;

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.baseURI = String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "sellers");

        E2ETestUtilities.dropCollections(DATASTORE);

        Main.startServer(
                DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());

        setupSeller();
        setupProduct();
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
        Main.shutdownServerNow();
    }

    private static void setupSeller() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response postSellerResponse = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        sellerId = E2ETestUtilities.parseUUIDFromHeaderLocation(postSellerResponse);
    }

    private static void setupProduct() throws IOException {
        String postProductRequestJson = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response postProductResponse = E2ETestUtilities.sendPostProductRequest(sellerId.toString(),
                postProductRequestJson);

        productId = E2ETestUtilities.parseUUIDFromHeaderLocation(postProductResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "banana", "\n\t", "904e301f-3c12-46d9-8e45", " "})
    public void givenInvalidSellerId_whenGetSellerById_thenReturns404(final String sellerId) {
        Response response = E2ETestUtilities.sendGetSellerByIdRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "banana", "\n\t", "904e301f-3c12-46d9-8e45", " "})
    public void givenInvalidSellerId_whenGetSellerById_thenReturnsSellerNotFoundResponse(final String sellerId) {
        Response response = E2ETestUtilities.sendGetSellerByIdRequest(sellerId);

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.ITEM_NOT_FOUND, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                String.format("Seller with ID %s not found.", sellerId),
                responseBodyJsonObject.getString("description"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a82e74bd-1f9d-4548-8bf7-1ec490317042", "7a2e98ee-10d6-441b-b601-2164b8559885"})
    public void givenNonexistentSellerId_whenGetSellerById_thenReturns404(final String sellerId) {
        Response response = E2ETestUtilities.sendGetSellerByIdRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a82e74bd-1f9d-4548-8bf7-1ec490317042", "7a2e98ee-10d6-441b-b601-2164b8559885"})
    public void givenNonexistentSellerId_whenGetSellerById_thenReturnsSellerNotFound(final String sellerId) {
        Response response = E2ETestUtilities.sendGetSellerByIdRequest(sellerId);

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.ITEM_NOT_FOUND, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                String.format("Seller with ID %s not found.", sellerId),
                responseBodyJsonObject.getString("description"));
    }

    @Test
    public void givenValidSellerId_whenGetSellerById_thenReturns200() {
        Response response = E2ETestUtilities.sendGetSellerByIdRequest(sellerId.toString());

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenValidSellerId_whenGetSellerById_thenReturnsCorrespondingSeller() {
        Response response = E2ETestUtilities.sendGetSellerByIdRequest(sellerId.toString());

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        assertResponseSellerIsCorrect(responseBodyJsonObject);
    }

    private void assertResponseSellerIsCorrect(final JSONObject responseSellerJson) {
        Assertions.assertEquals(sellerId.toString(), responseSellerJson.getString("id"));
        Assertions.assertEquals(VALID_SELLER.getName(), responseSellerJson.getString("name"));
        Assertions.assertEquals(VALID_SELLER.getBio(), responseSellerJson.getString("bio"));
        Assertions.assertTrue(responseSellerJson.has("createdAt"));
        Assertions.assertTrue(responseSellerJson.has("products"));

        JSONObject productJsonObject = responseSellerJson.getJSONArray("products").getJSONObject(0);
        assertResponseSellerProductIsCorrect(productJsonObject);
    }

    private void assertResponseSellerProductIsCorrect(final JSONObject productJsonObject) {
        Assertions.assertEquals(productId.toString(), productJsonObject.getString("id"));
        Assertions.assertEquals(VALID_PRODUCT.getTitle(), productJsonObject.getString("title"));
        Assertions.assertEquals(VALID_PRODUCT.getDescription(), productJsonObject.getString("description"));
        Assertions.assertEquals(
                VALID_PRODUCT.getSuggestedPrice(),
                productJsonObject.getDouble("suggestedPrice"));
        Assertions.assertTrue(productJsonObject.has("createdAt"));
        Assertions.assertTrue(productJsonObject.has("offers"));
        Assertions.assertTrue(productJsonObject.has("categories"));

        JSONObject offersJsonObject = productJsonObject.getJSONObject("offers");
        assertResponseSellerOffersIsCorrect(offersJsonObject);
    }

    private void assertResponseSellerOffersIsCorrect(final JSONObject offersJsonObject) {
        Assertions.assertEquals(0, offersJsonObject.getInt("count"));
        Assertions.assertFalse(offersJsonObject.has("mean"));
    }
}
