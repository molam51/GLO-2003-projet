package ulaval.glo2003.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.morphia.Datastore;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.Precision;
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
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.domain.product.ProductCategoryUtils;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.errors.Error;
import ulaval.glo2003.ui.product.requests.PostProductRequest;
import ulaval.glo2003.ui.product.responses.FilterProductsResponse;
import ulaval.glo2003.ui.product.responses.FilterProductsResponseProduct;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.ProductFilterTestUtilities;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterProductsTest {

    private static final ObjectWriter JSON_WRITER = new ObjectMapper().writer();
    private static final List<GetProductsWithFiltersTestEntry> TEST_ENTRIES = new ArrayList<>();
    private static final Seller VALID_SELLER = new TestSellerBuilder().build();
    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    private static Product product1;
    private static Product product2;
    private static Product product3;
    private static UUID sellerId;

    private static final int MIN_SUGGESTED_PRICE_PRODUCT_1 = 100;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT_1 = 200;
    private static final int MIN_SUGGESTED_PRICE_PRODUCT_2 = 400;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT_2 = 600;
    private static final int MIN_SUGGESTED_PRICE_PRODUCT_3 = 800;
    private static final int MAX_SUGGESTED_PRICE_PRODUCT_3 = 1000;

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.baseURI = String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "products");
        RestAssured.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JAXB));

        E2ETestUtilities.dropCollections(DATASTORE);

        Main.startServer(
                DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());

        setupSeller();
        setupProduct1();
        setupProduct2();
        setupProduct3();
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

    private static void setupProduct1() throws IOException {
        product1 = new TestProductBuilder()
                .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT_1, MAX_SUGGESTED_PRICE_PRODUCT_1)
                .build();

        setupProduct(product1);
    }

    private static void setupProduct2() throws IOException {
        product2 = new TestProductBuilder()
                .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT_2, MAX_SUGGESTED_PRICE_PRODUCT_2)
                .withoutCategories(product1.getCategories())
                .build();

        setupProduct(product2);
    }

    private static void setupProduct3() throws IOException {
        List<ProductCategory> excludedCategories = Stream.concat(
                        product1.getCategories().stream(),
                        product2.getCategories().stream())
                .distinct()
                .collect(Collectors.toList());

        product3 = new TestProductBuilder()
                .withSuggestedPriceBetween(MIN_SUGGESTED_PRICE_PRODUCT_3, MAX_SUGGESTED_PRICE_PRODUCT_3)
                .withoutCategories(excludedCategories)
                .build();

        setupProduct(product3);
    }

    private static void setupProduct(final Product product) throws IOException {
        String suggestedPrice = Double.toString(product.getSuggestedPrice());
        List<String> categories = ProductCategoryUtils.productCategoriesToStrings(product.getCategories());

        PostProductRequest postProductRequest = E2ETestUtilities.generatePostProductRequestObject(
                product.getTitle(),
                product.getDescription(),
                suggestedPrice,
                categories);

        GetProductsWithFiltersTestEntry testEntry = createTestEntry(postProductRequest, sellerId);
        TEST_ENTRIES.add(testEntry);
    }

    private static GetProductsWithFiltersTestEntry createTestEntry(
            final PostProductRequest postProductRequest,
            final UUID sellerId) throws JsonProcessingException {
        String postProductRequestJson = JSON_WRITER.writeValueAsString(postProductRequest);
        Response postProductResponse = E2ETestUtilities.sendPostProductRequest(sellerId.toString(),
                postProductRequestJson);

        UUID productId = E2ETestUtilities.parseUUIDFromHeaderLocation(postProductResponse);

        return new GetProductsWithFiltersTestEntry(productId, sellerId, postProductRequest);
    }

    @Test
    public void givenNoFilters_whenFilterProducts_thenReturns200() {
        Response response = E2ETestUtilities.sendFilterProductsRequest();

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNoFilters_whenFilterProducts_thenReturnsAllProducts() {
        Response response = E2ETestUtilities.sendFilterProductsRequest();

        FilterProductsResponse filterProductsResponse = response.getBody().as(FilterProductsResponse.class);

        assertResponseProductsMatchesTestEntries(filterProductsResponse.products, TEST_ENTRIES);
    }

    @Test
    public void givenTitle_whenFilterProducts_thenReturns200() {
        String title = product1.getTitle().substring(1, product1.getTitle().length() - 1);

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("title", title)
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenTitle_whenFilterProducts_thenReturnsMatchingProducts() {
        String title = product1.getTitle().substring(1, product1.getTitle().length() - 1);

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("title", title)
        ));

        FilterProductsResponse filterProductsResponse = response.getBody().as(FilterProductsResponse.class);
        assertResponseProductsMatchesTestEntries(filterProductsResponse.products, List.of(TEST_ENTRIES.get(0)));
    }

    @Test
    public void givenCategory_whenFilterProducts_thenReturns200() {
        ProductCategory product1Category = product1.getCategories().get(0);
        ProductCategory product3Category = product3.getCategories().get(0);

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("categories", product1Category),
                Pair.of("categories", product3Category)
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenCategory_whenFilterProducts_thenReturnsMatchingProducts() {
        ProductCategory product1Category = product1.getCategories().get(0);
        ProductCategory product3Category = product3.getCategories().get(0);

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("categories", product1Category),
                Pair.of("categories", product3Category)
        ));

        FilterProductsResponse filterProductsResponse = response.getBody().as(FilterProductsResponse.class);
        assertResponseProductsMatchesTestEntries(
                filterProductsResponse.products,
                List.of(TEST_ENTRIES.get(0), TEST_ENTRIES.get(2)));
    }

    @Test
    public void givenInvalidCategory_whenFilterProducts_thenReturns400() {
        String category = null;

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("categories", category)
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenInvalidCategory_whenFilterProducts_thenReturnsInvalidParameter() {
        String category = null;

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("categories", category)
        ));

        JSONObject responseJson = new JSONObject(response.asString());
        Assertions.assertEquals(Error.INVALID_PARAM, responseJson.get("code"));
    }

    @Test
    public void givenMinPrice_whenFilterProducts_thenReturns200() {
        double minPrice = ProductFilterTestUtilities.getMatchingMinPrice(List.of(product2));

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("minPrice", Precision.round(minPrice, 2))
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenMinPrice_whenFilterProducts_thenReturnsMatchingProducts() {
        double minPrice = ProductFilterTestUtilities.getMatchingMinPrice(List.of(product2));

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("minPrice", Precision.round(minPrice, 2))
        ));

        FilterProductsResponse filterProductsResponse = response.getBody().as(FilterProductsResponse.class);
        assertResponseProductsMatchesTestEntries(
                filterProductsResponse.products,
                List.of(TEST_ENTRIES.get(1), TEST_ENTRIES.get(2)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "10.999", "abc"})
    public void givenInvalidMinPrice_whenFilterProducts_thenReturns400(final String minPrice) {
        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("minPrice", minPrice)
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "10.999", "abc"})
    public void givenInvalidMinPrice_whenFilterProducts_thenReturnsInvalidParameter(final String minPrice) {
        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("minPrice", minPrice)
        ));

        JSONObject responseJson = new JSONObject(response.asString());
        Assertions.assertEquals(Error.INVALID_PARAM, responseJson.get("code"));
    }

    @Test
    public void givenMaxPrice_whenFilterProducts_thenReturns200() {
        double maxPrice = ProductFilterTestUtilities.getMatchingMaxPrice(List.of(product2));

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("maxPrice", Precision.round(maxPrice, 2))
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenMaxPrice_whenFilterProducts_thenReturnsMatchingProducts() {
        double maxPrice = ProductFilterTestUtilities.getMatchingMaxPrice(List.of(product2));

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("maxPrice", Precision.round(maxPrice, 2))
        ));

        FilterProductsResponse filterProductsResponse = response.getBody().as(FilterProductsResponse.class);
        assertResponseProductsMatchesTestEntries(
                filterProductsResponse.products,
                List.of(TEST_ENTRIES.get(0), TEST_ENTRIES.get(1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "10.999", "abc"})
    public void givenInvalidMaxPrice_whenFilterProducts_thenReturns400(final String maxPrice) {
        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("maxPrice", maxPrice)
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "10.999", "abc"})
    public void givenInvalidMaxPrice_whenFilterProducts_thenReturnsInvalidParameter(final String maxPrice) {
        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("maxPrice", maxPrice)
        ));

        JSONObject responseJson = new JSONObject(response.asString());
        Assertions.assertEquals(Error.INVALID_PARAM, responseJson.get("code"));
    }

    @Test
    public void givenAllFilters_whenFilterProducts_thenReturns200() {
        String title = product1.getTitle().substring(1, product1.getTitle().length() - 1);
        String category = product1.getCategories().get(0).toString();
        double minPrice = ProductFilterTestUtilities.getMatchingMinPrice(List.of(product1));
        double maxPrice = ProductFilterTestUtilities.getMatchingMaxPrice(List.of(product1));

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("sellerId", sellerId),
                Pair.of("title", title),
                Pair.of("category", category),
                Pair.of("minPrice", Precision.round(minPrice, 2)),
                Pair.of("maxPrice", Precision.round(maxPrice, 2))
        ));

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenAllFilters_whenFilterProducts_thenReturnsMatchingProducts() {
        String title = product1.getTitle().substring(1, product1.getTitle().length() - 1);
        String category = product1.getCategories().get(0).toString();
        double minPrice = ProductFilterTestUtilities.getMatchingMinPrice(List.of(product1));
        double maxPrice = ProductFilterTestUtilities.getMatchingMaxPrice(List.of(product1));

        Response response = E2ETestUtilities.sendFilterProductsRequest(List.of(
                Pair.of("sellerId", sellerId),
                Pair.of("title", title),
                Pair.of("category", category),
                Pair.of("minPrice", Precision.round(minPrice, 2)),
                Pair.of("maxPrice", Precision.round(maxPrice, 2))
        ));

        FilterProductsResponse filterProductsResponse = response.getBody().as(FilterProductsResponse.class);
        assertResponseProductsMatchesTestEntries(
                filterProductsResponse.products,
                List.of(TEST_ENTRIES.get(0)));
    }

    private void assertResponseProductsMatchesTestEntries(
            final List<FilterProductsResponseProduct> responseProducts,
            final List<GetProductsWithFiltersTestEntry> testEntries) {
        Assertions.assertEquals(testEntries.size(), responseProducts.size());

        for (int i = 0; i < testEntries.size(); i++) {
            assertResponseProductMatchesTestEntry(responseProducts.get(i), testEntries.get(i));
        }
    }

    private void assertResponseProductMatchesTestEntry(
            final FilterProductsResponseProduct responseProduct,
            final GetProductsWithFiltersTestEntry testEntry) {
        Assertions.assertEquals(testEntry.productId, responseProduct.id);
        Assertions.assertEquals(testEntry.postProductRequest.title, responseProduct.title);
        Assertions.assertEquals(testEntry.postProductRequest.description, responseProduct.description);
        Assertions.assertEquals(
                Double.parseDouble(testEntry.postProductRequest.suggestedPrice),
                responseProduct.suggestedPrice);
        List<String> responseProductCategories = responseProduct.categories.stream()
                .map(ProductCategory::toString)
                .collect(Collectors.toList());
        Assertions.assertIterableEquals(testEntry.postProductRequest.categories, responseProductCategories);
        Assertions.assertEquals(testEntry.sellerId, responseProduct.seller.id);
        Assertions.assertEquals(VALID_SELLER.getName(), responseProduct.seller.name);
        Assertions.assertNotNull(responseProduct.createdAt);
    }

    private static final class GetProductsWithFiltersTestEntry {
        final UUID productId;
        final UUID sellerId;
        final PostProductRequest postProductRequest;

        GetProductsWithFiltersTestEntry(final UUID productId,
                                        final UUID sellerId,
                                        final PostProductRequest postProductRequest) {
            this.productId = productId;
            this.sellerId = sellerId;
            this.postProductRequest = postProductRequest;
        }
    }
}
