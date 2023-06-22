package ulaval.glo2003.ui.health;

import dev.morphia.Datastore;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import ulaval.glo2003.application.FloppaMongoUtilities;
import ulaval.glo2003.ui.health.responses.GetHealthResponse;
import ulaval.glo2003.ui.health.responses.GetHealthResponseAssembler;
import ulaval.glo2003.ui.health.responses.GetHealthResponseAssemblerArgs;

@Path("/health")
public class HealthResource {

    final Datastore datastore;

    public HealthResource(final Datastore datastore) {
        this.datastore = datastore;
    }

    @GET
    public Response getHealth() {
        boolean dbOk = FloppaMongoUtilities.isDatastoreOperational(datastore);

        GetHealthResponseAssembler healthResponseAssembler = new GetHealthResponseAssembler();
        GetHealthResponseAssemblerArgs healthResponseAssemblerArgs = new GetHealthResponseAssemblerArgs(true, dbOk);
        GetHealthResponse healthResponse = healthResponseAssembler.toResponse(healthResponseAssemblerArgs);

        if (dbOk) {
            return Response.ok(healthResponse).build();
        } else {
            return Response.serverError().entity(healthResponse).build();
        }
    }
}
