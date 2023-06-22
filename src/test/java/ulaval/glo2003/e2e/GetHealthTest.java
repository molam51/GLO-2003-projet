package ulaval.glo2003.e2e;

import dev.morphia.Datastore;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.Main;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.application.FloppaMongoUtilities;
import ulaval.glo2003.utils.E2ETestUtilities;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GetHealthTest {

    private static final Datastore DATASTORE = E2ETestUtilities.createTestDatastore();

    @BeforeAll
    public static void setup() throws IOException {
        RestAssured.baseURI = String.format("%s/%s", FloppaConfiguration.getLocalApiHostUrl(), "health");
    }

    @AfterEach
    public void teardownAfterEach() {
        Main.shutdownServerNow();
    }

    @Test
    public void givenFunctioningApi_whenGetHealth_thenReturnDatabaseStatus() throws IOException {
        Main.startServer(
                DATASTORE,
                FloppaConfiguration.getMongoDevelopmentDatabaseName());

        Response healthResponse = given().header("Content-Type", "application/json")
                .when()
                .get()
                .then()
                .extract()
                .response();

        if (FloppaMongoUtilities.isDatastoreOperational(DATASTORE)) {
            Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(),
                    healthResponse.getStatusCode());
            Assertions.assertEquals(E2ETestUtilities.generateGetHealthResponseBody(true, true),
                    healthResponse.getBody().asString());
        } else {
            Assertions.assertEquals(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    healthResponse.getStatusCode());
            Assertions.assertEquals(E2ETestUtilities.generateGetHealthResponseBody(true, false),
                    healthResponse.getBody().asString());
        }
    }
}
