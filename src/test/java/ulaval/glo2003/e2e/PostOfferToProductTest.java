package ulaval.glo2003.e2e;

import dev.morphia.Datastore;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.Main;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategoryUtils;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestOfferBuilder;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;

public final class PostOfferToProductTest {

    private static final int MIN_SUGGESTED_PRICE_PRODUCT = 100;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT = 200;

    private static final int MIN_AMOUNT_OFFER = 200;
    private static final int MAX_AMOUNT_OFFER = 300;

    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static final Product VALID_PRODUCT = new TestProductBuilder()
            .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT, MAX_SUGGESTED_PRICE_PRODUCT).build();
    private static final Offer VALID_OFFER = new TestOfferBuilder()
            .withAmountBetween(MIN_AMOUNT_OFFER, MAX_AMOUNT_OFFER).build();
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

    private String createValidOfferRequest() throws IOException {
        return E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());
    }

    @Test
    public void givenValidRequest_whenPostOfferToProduct_thenCreateOffer() throws IOException {
        String offerRequestBody = createValidOfferRequest();

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenTooLowAmount_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                String.format("%.2f", VALID_PRODUCT.getSuggestedPrice() - 1.0),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenTooLowAmount_whenPostOfferToProduct_thenDetailsAreCorrect() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                String.format("%.2f", VALID_PRODUCT.getSuggestedPrice() - 1.0),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "Amount must be equal or higher to suggested price.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyAmountInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                "",
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyAmountInRequest_whenPostOfferToProduct_thenReturnsInvalidParameter() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                "",
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`amount` is not a number");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNonNumberAmountInRequest_whenPostOfferToProduct_thenReturnsInvalidParameter()
            throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                "3as",
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`amount` is not a number");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullAmountInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                null,
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullAmountInRequest_whenPostOfferToProduct_thenReturnsMissingParameter()
            throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                null,
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The amount is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyMessageInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                "",
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyMessageInRequest_whenPostOfferToProduct_thenReturnsInvalidParameter()
            throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                "",
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "The message must be at least 100 characters in length");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullMessageInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                null,
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullMessageInRequest_whenPostOfferToProduct_thenReturnsMissingParameter() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                null,
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The message is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyNameInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                "",
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyNameInRequest_whenPostOfferToProduct_thenReturns400WithDetails() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                "",
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "The buyer name must not be empty");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullNameInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                null,
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullNameInRequest_whenPostOfferToProduct_thenReturnsMissingParameter() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                null,
                VALID_OFFER.getBuyer().getEmail(),
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The buyer name is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenInvalidEmailInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                "",
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenInvalidEmailInRequest_whenPostOfferToProduct_thenReturnsInvalidParameter()
            throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                "",
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "The buyer email must be a valid");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullEmailInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                null,
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullEmailInRequest_whenPostOfferToProduct_thenReturnsMissingParameter() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                null,
                VALID_OFFER.getBuyer().getPhoneNumber());

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The buyer email is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenInvalidPhoneNumberInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                "494857");

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenInvalidPhoneNumberInRequest_whenPostOfferToProduct_thenReturnsInvalidParameter()
            throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                "494857");

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "The buyer phone number must be a sequence of 11 numbers");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullPhoneNumberInRequest_whenPostOfferToProduct_thenReturns400() throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                null);

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullPhoneNumberInRequest_whenPostOfferToProduct_thenReturnsMissingParameter()
            throws IOException {
        String offerRequestBody = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(VALID_OFFER.getAmount()),
                VALID_OFFER.getMessage(),
                VALID_OFFER.getBuyer().getName(),
                VALID_OFFER.getBuyer().getEmail(),
                null);

        Response response = E2ETestUtilities.sendPostOfferToProduct(productId, offerRequestBody);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "The buyer phone number is missing");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }
}
