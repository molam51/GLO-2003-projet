package ulaval.glo2003.ui.seller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.EntityFactory;
import ulaval.glo2003.domain.exceptions.MissingParamException;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.domain.seller.SellerCreationParameters;
import ulaval.glo2003.domain.seller.SellerRepository;
import ulaval.glo2003.ui.seller.requests.PostSellerRequest;
import ulaval.glo2003.ui.seller.responses.GetCurrentSellerResponseAssembler;
import ulaval.glo2003.ui.seller.responses.GetSellerByIdResponseAssembler;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@Path("/sellers")
public class SellerResource {

    private final SellerRepository sellerRepository;
    private final EntityFactory<Seller> sellerFactory;

    public SellerResource(final SellerRepository sellerRepository, final EntityFactory<Seller> sellerFactory) {
        this.sellerRepository = sellerRepository;
        this.sellerFactory = sellerFactory;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSellerById(final @PathParam("id") String id) {
        UUID sellerId = SellerUtilities.parseStringToSellerUUID(id);

        Seller seller = this.sellerRepository.fetch(sellerId);

        GetSellerByIdResponseAssembler responseAssembler = new GetSellerByIdResponseAssembler();
        return Response.ok(responseAssembler.toResponse(seller)).build();
    }

    @GET
    @Path("/@me")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentSeller(final @HeaderParam("X-Seller-Id") String id) {
        UUID sellerId = SellerUtilities.parseStringToSellerUUID(id);

        if (sellerId == null) {
            throw new MissingParamException("Seller ID must not be empty.");
        }

        Seller seller = this.sellerRepository.fetch(sellerId);

        GetCurrentSellerResponseAssembler getCurrentSellerResponseAssembler = new GetCurrentSellerResponseAssembler();
        return Response.ok(getCurrentSellerResponseAssembler.toResponse(seller)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSeller(final PostSellerRequest request) {
        LocalDate sellerBirthDate = SellerUtilities.parseStringToBirthDate(request.birthDate);

        Seller seller = this.sellerFactory.create(new SellerCreationParameters(request.name, request.bio, sellerBirthDate));
        this.sellerRepository.add(seller);

        URI uri = URI.create(String.format("%s/sellers/%s", FloppaConfiguration.getApiHostUrl(), seller.getId()));

        return Response.created(uri).build();
    }
}
