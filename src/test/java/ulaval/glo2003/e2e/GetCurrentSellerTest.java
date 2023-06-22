package ulaval.glo2003.e2e;

import dev.morphia.Datastore;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ulaval.glo2003.Main;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.offer.Buyer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.domain.product.ProductCategoryUtils;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.errors.Error;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestOfferBuilder;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetCurrentSellerTest {

    private static final int MIN_AMOUNT_OFFER_1 = 100;
    private static final int MAX_AMOUNT_OFFER_1 = 200;
    private static final int MIN_AMOUNT_OFFER_2 = 300;
    private static final int MAX_AMOUNT_OFFER_2 = 400;

    private static final Offer VALID_OFFER_1 = new TestOfferBuilder()
            .withAmountBetween(MIN_AMOUNT_OFFER_1, MAX_AMOUNT_OFFER_1).build();
    private static final Offer VALID_OFFER_2 = new TestOfferBuilder()
            .withAmountBetween(MIN_AMOUNT_OFFER_2, MAX_AMOUNT_OFFER_2).build();
    private static final Product VALID_PRODUCT_WITH_OFFERS = new TestProductBuilder()
            .withOffersSetTo(List.of(VALID_OFFER_1, VALID_OFFER_2))
            .build();
    private static final Seller VALID_SELLER_WITH_PRODUCTS_AND_OFFERS = new TestSellerBuilder()
            .withProductsSetTo(List.of(VALID_PRODUCT_WITH_OFFERS))
            .build();
    private static final Product VALID_PRODUCT_WITHOUT_OFFERS = new TestProductBuilder()
            .withOffersSetTo(List.of())
            .build();
    private static final Seller VALID_SELLER_WITH_PRODUCTS = new TestSellerBuilder()
            .withProductsSetTo(List.of(VALID_PRODUCT_WITHOUT_OFFERS))
            .build();
    private static final Seller VALID_SELLER_WITHOUT_PRODUCTS = new TestSellerBuilder()
            .withProductsSetTo(List.of())
            .build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static String sellerId;
    private static String productId;

    @BeforeAll
    public static void setup() throws IOException {
        Main.startServer(DATASTORE, FloppaConfiguration.getMongoDevelopmentDatabaseName());

        E2ETestUtilities.dropCollections(DATASTORE);
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
        Main.shutdownServerNow();
    }

    private static void postSeller(final Seller seller) throws IOException {
        String postSellerRequestBody = E2ETestUtilities.generatePostSellerRequestBody(
                seller.getName(),
                seller.getBio(),
                seller.getBirthDate().toString());

        Response postSellerResponse = E2ETestUtilities.sendPostSellerRequest(postSellerRequestBody);
        String sellerLocation = postSellerResponse.getHeader("Location");
        sellerId = sellerLocation.substring(sellerLocation.lastIndexOf('/') + 1);
    }

    private static void postProduct(final Product product) throws IOException {
        String postProductRequestJson = E2ETestUtilities.generatePostProductRequestBody(
                product.getTitle(),
                product.getDescription(),
                Double.toString(product.getSuggestedPrice()),
                ProductCategoryUtils.productCategoriesToStrings(product.getCategories()));

        Response postProductResponse = E2ETestUtilities.sendPostProductRequest(sellerId, postProductRequestJson);
        String productLocation = postProductResponse.getHeader("Location");
        productId = productLocation.substring(productLocation.lastIndexOf('/') + 1);
    }

    private static void postOffer(final Offer offer) throws IOException {
        String postOfferRequestJson = E2ETestUtilities.generatePostOfferToProductRequestBody(
                Double.toString(offer.getAmount()),
                offer.getMessage(),
                offer.getBuyer().getName(),
                offer.getBuyer().getEmail(),
                offer.getBuyer().getPhoneNumber());

        E2ETestUtilities.sendPostOfferToProduct(productId, postOfferRequestJson);
    }

    @AfterEach
    public void teardownAfterEach() {
        E2ETestUtilities.dropCollections(DATASTORE);
    }

    @Test
    public void givenNullSellerId_whenGetCurrentSeller_thenRespondWith400() {
        Response response = E2ETestUtilities.sendGetCurrentSellerRequestWithoutSellerId();

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullSellerId_whenGetCurrentSeller_thenDetailsAreCorrect() {
        Response response = E2ETestUtilities.sendGetCurrentSellerRequestWithoutSellerId();

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.MISSING_PARAM, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                "Seller ID must not be empty.",
                responseBodyJsonObject.getString("description"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "\n\t", "\r", " ", "banana", "904e301f-3c12-46d9-8e45"})
    public void givenInvalidCurrentId_whenGetCurrentSeller_thenRespondWith404SellerNotFound(final String sellerId) {
        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatusCode());

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.ITEM_NOT_FOUND, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                String.format("Seller with ID %s not found.", sellerId.trim()),
                responseBodyJsonObject.getString("description"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a82e74bd-1f9d-4548-8bf7-1ec490317042", "7a2e98ee-10d6-441b-b601-2164b8559885"})
    public void givenNonexistentCurrentId_whenGetCurrentSeller_thenReturns404(final String sellerId) {
        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"a82e74bd-1f9d-4548-8bf7-1ec490317042", "7a2e98ee-10d6-441b-b601-2164b8559885"})
    public void givenNonexistentCurrentId_whenGetCurrentSeller_thenReturnsSellerNotFound(final String sellerId) {
        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        JSONObject responseBodyJsonObject = new JSONObject(response.getBody().asString());
        Assertions.assertEquals(Error.ITEM_NOT_FOUND, responseBodyJsonObject.getString("code"));
        Assertions.assertEquals(
                String.format("Seller with ID %s not found.", sellerId),
                responseBodyJsonObject.getString("description"));
    }

    @Test
    public void givenCurrentIdWithoutProducts_whenGetCurrentSeller_thenReturns200() throws IOException {
        postSeller(VALID_SELLER_WITHOUT_PRODUCTS);

        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenCurrentIdWithoutProducts_whenGetCurrentSeller_thenReturnsDetails() throws IOException {
        postSeller(VALID_SELLER_WITHOUT_PRODUCTS);

        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        JSONObject responseSeller = new JSONObject(response.asString());
        assertSellerEqualsResponseSeller(VALID_SELLER_WITHOUT_PRODUCTS, responseSeller);
        Assertions.assertEquals(0, responseSeller.getJSONArray("products").length());
    }

    @Test
    public void givenCurrentIdWithProduct_whenGetCurrentSeller_thenReturns200() throws IOException {
        postSeller(VALID_SELLER_WITH_PRODUCTS);
        postProduct(VALID_PRODUCT_WITHOUT_OFFERS);

        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenCurrentIdWithProduct_whenGetCurrentSeller_thenReturnsDetails() throws IOException {
        postSeller(VALID_SELLER_WITH_PRODUCTS);
        postProduct(VALID_PRODUCT_WITHOUT_OFFERS);

        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        JSONObject responseSeller = new JSONObject(response.asString());
        assertSellerEqualsResponseSeller(VALID_SELLER_WITH_PRODUCTS, responseSeller);
        Assertions.assertEquals(1, responseSeller.getJSONArray("products").length());
    }

    @Test
    public void givenCurrentIdWithProductAndOffers_whenGetCurrentSeller_thenReturns200() throws IOException {
        postSeller(VALID_SELLER_WITH_PRODUCTS_AND_OFFERS);
        postProduct(VALID_PRODUCT_WITH_OFFERS);
        postOffer(VALID_OFFER_1);
        postOffer(VALID_OFFER_2);

        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenCurrentIdWithProductAndOffers_whenGetCurrentSeller_thenReturnsDetails() throws IOException {
        postSeller(VALID_SELLER_WITH_PRODUCTS_AND_OFFERS);
        postProduct(VALID_PRODUCT_WITH_OFFERS);
        postOffer(VALID_OFFER_1);
        postOffer(VALID_OFFER_2);

        Response response = E2ETestUtilities.sendGetCurrentSellerRequest(sellerId);

        JSONObject responseSeller = new JSONObject(response.asString());
        assertSellerEqualsResponseSeller(VALID_SELLER_WITH_PRODUCTS_AND_OFFERS, responseSeller);
        Assertions.assertEquals(1, responseSeller.getJSONArray("products").length());
    }

    private void assertSellerEqualsResponseSeller(final Seller seller, final JSONObject responseSeller) {
        Assertions.assertEquals(sellerId, responseSeller.getString("id"));

        Assertions.assertTrue(responseSeller.has("createdAt"));
        Assertions.assertNotNull(responseSeller.getString("createdAt"));

        Assertions.assertEquals(seller.getName(), responseSeller.getString("name"));
        Assertions.assertEquals(seller.getBio(), responseSeller.getString("bio"));
        Assertions.assertEquals(seller.getBirthDate().toString(), responseSeller.getString("birthDate"));

        Assertions.assertTrue(responseSeller.has("products"));
        Assertions.assertNotNull(responseSeller.get("products"));

        assertProductsEqualsResponseProducts(seller.getProducts(), responseSeller.getJSONArray("products"));
    }

    private void assertProductsEqualsResponseProducts(final List<Product> products, final JSONArray responseProducts) {
        Assertions.assertEquals(products.size(), responseProducts.length());

        for (int i = 0; i < products.size(); i++) {
            assertProductEqualsResponseProduct(products.get(i), responseProducts.getJSONObject(i));
        }
    }

    private void assertProductEqualsResponseProduct(final Product product, final JSONObject responseProduct) {
        Assertions.assertEquals(productId, responseProduct.getString("id"));

        Assertions.assertTrue(responseProduct.has("createdAt"));
        Assertions.assertNotNull(responseProduct.getString("createdAt"));

        Assertions.assertEquals(product.getTitle(), responseProduct.getString("title"));
        Assertions.assertEquals(product.getDescription(), responseProduct.getString("description"));
        Assertions.assertEquals(product.getSuggestedPrice(), responseProduct.getDouble("suggestedPrice"));

        Assertions.assertIterableEquals(
                product.getCategories().stream()
                        .map(ProductCategory::toString)
                        .collect(Collectors.toList()),
                responseProduct.getJSONArray("categories"));

        Assertions.assertTrue(responseProduct.has("offers"));
        Assertions.assertNotNull(responseProduct.get("offers"));

        assertOffersEqualsResponseOffers(product.getOffers(), responseProduct.getJSONObject("offers"));
    }

    private void assertOffersEqualsResponseOffers(final List<Offer> offers, final JSONObject responseOffers) {
        Assertions.assertTrue(responseOffers.has("items"));
        Assertions.assertNotNull(responseOffers.getJSONArray("items"));

        JSONArray responseOffersItems = responseOffers.getJSONArray("items");

        if (responseOffersItems.isEmpty()) {
            assertEmptyResponseOffersIsValid(responseOffers);
        } else {
            assertNonEmptyResponseOffersIsValid(offers, responseOffers);
        }
    }

    private void assertEmptyResponseOffersIsValid(final JSONObject responseOffers) {
        Assertions.assertFalse(responseOffers.has("mean"));
        Assertions.assertFalse(responseOffers.has("min"));
        Assertions.assertFalse(responseOffers.has("max"));
        Assertions.assertEquals(0, responseOffers.getInt("count"));
    }

    private void assertNonEmptyResponseOffersIsValid(final List<Offer> offers, final JSONObject responseOffers) {
        double expectedMean = offers.stream().mapToDouble(Offer::getAmount).average().getAsDouble();
        double expectedMin = offers.stream().mapToDouble(Offer::getAmount).min().getAsDouble();
        double expectedMax = offers.stream().mapToDouble(Offer::getAmount).max().getAsDouble();

        Assertions.assertEquals(expectedMean, responseOffers.getDouble("mean"));
        Assertions.assertEquals(expectedMin, responseOffers.getDouble("min"));
        Assertions.assertEquals(expectedMax, responseOffers.getDouble("max"));
        Assertions.assertEquals(offers.size(), responseOffers.getInt("count"));

        assertOffersEqualsResponseOffersItems(offers, responseOffers.getJSONArray("items"));
    }

    private void assertOffersEqualsResponseOffersItems(final List<Offer> offers, final JSONArray responseOffersItems) {
        Assertions.assertEquals(offers.size(), responseOffersItems.length());

        for (int i = 0; i < offers.size(); i++) {
            assertOfferEqualsResponseOffersItem(offers.get(i), responseOffersItems.getJSONObject(i));
        }
    }

    private void assertOfferEqualsResponseOffersItem(final Offer offer, final JSONObject responseOffersItem) {
        Assertions.assertTrue(responseOffersItem.has("id"));
        Assertions.assertNotNull(responseOffersItem.getString("id"));

        Assertions.assertTrue(responseOffersItem.has("createdAt"));
        Assertions.assertNotNull(responseOffersItem.getString("createdAt"));

        Assertions.assertEquals(offer.getAmount(), responseOffersItem.getDouble("amount"));
        Assertions.assertEquals(offer.getMessage(), responseOffersItem.getString("message"));

        Assertions.assertTrue(responseOffersItem.has("buyer"));
        Assertions.assertNotNull(responseOffersItem.get("buyer"));

        assertBuyerEqualsResponseBuyer(offer.getBuyer(), responseOffersItem.getJSONObject("buyer"));
    }

    private void assertBuyerEqualsResponseBuyer(final Buyer buyer, final JSONObject responseBuyer) {
        Assertions.assertEquals(buyer.getName(), responseBuyer.getString("name"));
        Assertions.assertEquals(buyer.getEmail(), responseBuyer.getString("email"));
        Assertions.assertEquals(buyer.getPhoneNumber(), responseBuyer.getString("phoneNumber"));
    }
}
