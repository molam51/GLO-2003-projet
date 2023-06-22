package ulaval.glo2003.e2e;

import dev.morphia.Datastore;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.Main;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.utils.E2ETestUtilities;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

public final class PostSellerTest {

    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();
    private static final Seller VALID_SELLER = new TestSellerBuilder().build();

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.baseURI = String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "sellers");

        E2ETestUtilities.dropCollections(DATASTORE);

        Main.startServer(
                DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());
    }

    @AfterAll
    public static void teardown() {
        E2ETestUtilities.dropCollections(DATASTORE);
        Main.shutdownServerNow();
    }

    @Test
    public void givenValidRequest_whenPostSeller_thenReturns201() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.getStatusCode());
    }

    @Test
    public void givenValidRequest_whenPostSeller_thenCreateSellerAndReturnURI() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        String location = response.getHeader("Location");
        Assertions.assertNotNull(location);
        Assertions.assertTrue(location.contains(RestAssured.baseURI));
        String locationId = location.replace(RestAssured.baseURI + "/", "");
        Assertions.assertDoesNotThrow(() -> UUID.fromString(locationId));
    }

    @Test
    public void givenInvalidBirthDateInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                "132117-");

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenInvalidBirthDateInRequest_whenPost_thenReturnsInvalidParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                "132117-");

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`birthDate` is not formatted as an ISO-8601 date");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyNameInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                "",
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyNameInRequest_whenPost_thenReturnsInvalidParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                "",
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "Parameter `name` is blank.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyBioInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                "",
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyBioInRequest_whenPost_thenReturnsInvalidParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                "",
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "Parameter `bio` is blank.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenEmptyBirthDateInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                "");

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenEmptyBirthDateInRequest_whenPost_thenReturnsInvalidParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                "");

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description", "`birthDate` is not formatted as an ISO-8601 date");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullNameInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                null,
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullNameInRequest_whenPost_thenReturnsMissingParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                null,
                VALID_SELLER.getBio(),
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "Parameter `name` is missing.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullBioInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                null,
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullBioInRequest_whenPost_thenReturnsMissingParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                null,
                VALID_SELLER.getBirthDate().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "Parameter `bio` is missing.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenNullBirthDateInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                null);

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenNullBirthDateInRequest_whenPost_thenReturnsMissingParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                null);

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "MISSING_PARAMETER");
        expectedResponseBody.put("description", "Parameter `birthDate` is missing.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }

    @Test
    public void givenMinorIndividualInRequest_whenPost_thenReturns400() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                LocalDate.now().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatusCode());
    }

    @Test
    public void givenMinorIndividualInRequest_whenPost_thenReturnsInvalidParameter() throws IOException {
        String postSellerRequestJson = E2ETestUtilities.generatePostSellerRequestBody(
                VALID_SELLER.getName(),
                VALID_SELLER.getBio(),
                LocalDate.now().toString());

        Response response = E2ETestUtilities.sendPostSellerRequest(postSellerRequestJson);

        JSONObject expectedResponseBody = new JSONObject();
        expectedResponseBody.put("code", "INVALID_PARAMETER");
        expectedResponseBody.put("description",
                "Parameter `birthDate` is invalid. Seller must be at least 18 years old.");
        Assertions.assertEquals(expectedResponseBody.toString(), response.getBody().asString());
    }
}
