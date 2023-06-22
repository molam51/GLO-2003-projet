package ulaval.glo2003.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.morphia.Datastore;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.tuple.Pair;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.services.MongoService;
import ulaval.glo2003.ui.health.responses.GetHealthResponse;
import ulaval.glo2003.ui.product.requests.PostOfferToProductRequest;
import ulaval.glo2003.ui.product.requests.PostProductRequest;
import ulaval.glo2003.ui.seller.requests.PostSellerRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public final class E2ETestUtilities {

    public static final String MONGO_PRODUCTS_DATABASE_NAME = "Products";
    public static final String MONGO_SELLERS_DATABASE_NAME = "Sellers";
    public static final String MONGO_OFFERS_DATABASE_NAME = "Offers";

    private static final String MONGO_ENTITIES_PACKAGE_NAME = "ulaval.glo2003.domain.mongo.entities";
    private static final ObjectWriter JSON_WRITER = new ObjectMapper().writer();

    private E2ETestUtilities() {
    }

    public static String generatePostSellerRequestBody(final String name, final String bio,
                                                       final String birthDate) throws IOException {
        PostSellerRequest postSellerRequest = new PostSellerRequest();
        postSellerRequest.name = name;
        postSellerRequest.bio = bio;
        postSellerRequest.birthDate = birthDate;

        return JSON_WRITER.writeValueAsString(postSellerRequest);
    }

    public static Response sendPostSellerRequest(final String requestBody) {
        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "sellers"))
                .then()
                .extract()
                .response();
    }

    public static Response sendPostProductRequest(final String sellerId, final String requestBody) {
        return given()
                .header("Content-Type", "application/json")
                .header("X-Seller-Id", sellerId)
                .body(requestBody)
                .when()
                .post(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products"))
                .then()
                .extract()
                .response();
    }

    public static Response sendPostProductRequestWithoutSellerId(final String requestBody) {
        return given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products"))
                .then()
                .extract()
                .response();
    }

    public static Response sendPostOfferToProduct(final String productId, final String requestBody) {
        return given()
                .header("Content-Type", "application/json")
                .pathParam("id", productId)
                .body(requestBody)
                .when()
                .post(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products/{id}/offers"))
                .then()
                .extract()
                .response();
    }

    public static Response sendPostViewsToProduct(final String productId) {
        return given()
                .header("Content-Type", "application/json")
                .pathParam("id", productId)
                .when()
                .post(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products/{id}/views"))
                .then()
                .extract()
                .response();
    }

    public static String generatePostProductRequestBody(final String title,
                                                        final String description,
                                                        final String suggestedPrice,
                                                        final List<String> categories) throws IOException {
        PostProductRequest productRequest = generatePostProductRequestObject(
                title,
                description,
                suggestedPrice,
                categories);

        return JSON_WRITER.writeValueAsString(productRequest);
    }

    public static PostProductRequest generatePostProductRequestObject(final String title,
                                                                      final String description,
                                                                      final String suggestedPrice,
                                                                      final List<String> categories) {
        PostProductRequest productRequest = new PostProductRequest();
        productRequest.title = title;
        productRequest.description = description;
        productRequest.suggestedPrice = suggestedPrice;
        productRequest.categories = categories;

        return productRequest;
    }

    public static String generatePostOfferToProductRequestBody(final String amount,
                                                               final String message,
                                                               final String name,
                                                               final String email,
                                                               final String phoneNumber) throws IOException {
        PostOfferToProductRequest offerRequest = generatePostOfferRequestObject(amount, message, name,
                email, phoneNumber);

        return JSON_WRITER.writeValueAsString(offerRequest);
    }

    public static PostOfferToProductRequest generatePostOfferRequestObject(final String amount,
                                                                           final String message,
                                                                           final String name,
                                                                           final String email,
                                                                           final String phoneNumber) {
        PostOfferToProductRequest offerRequest = new PostOfferToProductRequest();
        offerRequest.amount = amount;
        offerRequest.message = message;
        offerRequest.name = name;
        offerRequest.email = email;
        offerRequest.phoneNumber = phoneNumber;

        return offerRequest;
    }

    public static Response sendGetProductByIdRequest(final String productId) {
        return given()
                .header("Content-Type", "application/json")
                .pathParam("id", productId)
                .when()
                .get(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products/{id}"))
                .then()
                .extract()
                .response();
    }

    public static Response sendGetSellerByIdRequest(final String sellerId) {
        return given()
                .header("Content-Type", "application/json")
                .pathParam("id", sellerId)
                .when()
                .get(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "sellers/{id}"))
                .then()
                .extract()
                .response();
    }

    public static Response sendGetProductViewsRequest(final String productId) {
        return given()
                .header("Content-Type", "application/json")
                .pathParam("id", productId)
                .when()
                .get(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products/{id}/views"))
                .then()
                .extract()
                .response();
    }

    public static Response sendGetCurrentSellerRequest(final String sellerId) {
        return given()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
                .header("X-Seller-Id", sellerId)
                .when()
                .get(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "sellers/@me"))
                .then()
                .extract()
                .response();
    }

    public static Response sendGetCurrentSellerRequestWithoutSellerId() {
        return given()
                .urlEncodingEnabled(false)
                .header("Content-Type", "application/json")
                .when()
                .get(String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "sellers/@me"))
                .then()
                .extract()
                .response();
    }

    public static String generateGetHealthResponseBody(final boolean api, final boolean db) throws IOException {
        GetHealthResponse getHealthResponse = new GetHealthResponse();
        getHealthResponse.api = api;
        getHealthResponse.db = db;

        return JSON_WRITER.writeValueAsString(getHealthResponse);
    }

    public static Datastore createTestDatastore() {
        String localMongoConnectionString = FloppaConfiguration.getLocalMongoConnectionString();
        String mongoDevelopmentDatabaseName = FloppaConfiguration.getMongoDevelopmentDatabaseName();

        return MongoService.createMorphiaDatastore(
                localMongoConnectionString,
                mongoDevelopmentDatabaseName,
                MONGO_ENTITIES_PACKAGE_NAME);
    }

    public static void dropCollections(final Datastore datastore) {
        dropCollection(datastore, MONGO_PRODUCTS_DATABASE_NAME);
        dropCollection(datastore, MONGO_SELLERS_DATABASE_NAME);
        dropCollection(datastore, MONGO_OFFERS_DATABASE_NAME);
    }

    public static void dropCollection(final Datastore datastore, final String collectionName) {
        datastore.getDatabase().getCollection(collectionName).drop();
    }

    public static Response sendFilterProductsRequest() {
        return sendFilterProductsRequest(List.of());
    }

    public static Response sendFilterProductsRequest(final List<Pair<String, Object>> params) {
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON);

        for (Pair<String, Object> param : params) {
            requestSpecification = requestSpecification.param(param.getLeft(), param.getRight());
        }

        return requestSpecification.when().get().then().extract().response();
    }

    public static UUID parseUUIDFromHeaderLocation(final Response response) {
        String location = response.getHeader("Location");

        String uuid = location.substring(location.lastIndexOf('/') + 1);

        return UUID.fromString(uuid);
    }
}
