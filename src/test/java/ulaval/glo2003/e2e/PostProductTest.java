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
import org.junit.jupiter.params.provider.MethodSource;
import ulaval.glo2003.Main;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategoryUtils;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class PostProductTest {

    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static final Product VALID_PRODUCT = new TestProductBuilder().build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static String sellerId;

    private static Stream<List<String>> categoriesGenerator() {
        return Stream.of(
                List.of(),
                List.of("ELECTRONICS"),
                List.of("BEAUTY", "APPAREL"));
    }

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.baseURI = String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products");

        E2ETestUtilities.dropCollections(DATASTORE);

        Main.startServer(
                DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());

        String postSellerRequestBody = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response postSellerResponse = E2ETestUtilities.sendPostSellerRequest(postSellerRequestBody);

        sellerId = E2ETestUtilities.parseUUIDFromHeaderLocation(postSellerResponse).toString();
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
        Main.shutdownServerNow();
    }

    @ParameterizedTest
    @MethodSource("categoriesGenerator")
    public void givenValidRequest_whenPostProduct_thenReturns201(final List<String> categories) throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                categories);

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("categoriesGenerator")
    public void givenValidRequest_whenPostProduct_thenCreateProductAndReturnURI(final List<String> categories)
            throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                categories);

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        String location = response.getHeader("Location");
        Assertions.assertNotNull(location);
        Assertions.assertTrue(location.contains(RestAssured.baseURI));
        String locationId = location.replace(RestAssured.baseURI + "/", "");
        Assertions.assertDoesNotThrow(() -> UUID.fromString(locationId));
    }

    @Test
    public void givenValidRequestWithNullCategories_whenPostProduct_thenReturns201() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                null);

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.getStatusCode());
    }

    @Test
    public void givenValidRequestWithNullCategories_whenPostProduct_thenCreateProductAndReturnURI() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                null);

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        String location = response.getHeader("Location");
        Assertions.assertNotNull(location);
        Assertions.assertTrue(location.contains(RestAssured.baseURI));
        String locationId = location.replace(RestAssured.baseURI + "/", "");
        Assertions.assertDoesNotThrow(() -> UUID.fromString(locationId));
    }

    @Test
    public void givenNullSellerId_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequestWithoutSellerId(productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullSellerId_whenPostProduct_thenReturnsDetails() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequestWithoutSellerId(productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The seller ID is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyTitleInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                "",
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyTitleInRequest_whenPostProduct_thenReturnsInvalidParameter() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                "",
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "The product title must not be empty");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullTitleInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                null,
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullTitleInRequest_whenPostProduct_thenReturnsMissingParameter() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                null,
                VALID_PRODUCT.getDescription(),
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The title is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyDescriptionInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                "",
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyDescriptionInRequest_whenPostProduct_thenReturnsInvalidParameter() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                "",
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "The product description must not be empty");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullDescriptionInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                null,
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullDescriptionInRequest_whenPostProduct_thenReturnsMissingParameter() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                null,
                Double.toString(VALID_PRODUCT.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The description is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptySuggestedPriceInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                "",
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptySuggestedPriceInRequest_whenPostProduct_thenReturnsInvalidParameter()
            throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                "",
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`suggestedPrice` is not a number");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNonNumberSuggestedPriceInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                "abc",
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNonNumberSuggestedPriceInRequest_whenPostProduct_thenReturnsInvalidParameter()
            throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                "abc",
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`suggestedPrice` is not a number");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullSuggestedPriceInRequest_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                null,
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullSuggestedPriceInRequest_whenPostProduct_thenReturns400MissingParamWithDetails()
            throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                null,
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The suggested price is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenTooLowSuggestedPrice_whenPostProduct_thenReturns400() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                "0.01",
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenTooLowSuggestedPrice_whenPostProduct_thenReturnsInvalidParameter() throws IOException {
        String productRequestBody = E2ETestUtilities.generatePostProductRequestBody(
                VALID_PRODUCT.getTitle(),
                VALID_PRODUCT.getDescription(),
                "0.01",
                ProductCategoryUtils.productCategoriesToStrings(VALID_PRODUCT.getCategories()));

        Response response = E2ETestUtilities.sendPostProductRequest(sellerId, productRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`suggestedPrice` is too low");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }
}
