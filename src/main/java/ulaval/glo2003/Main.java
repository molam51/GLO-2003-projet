package ulaval.glo2003;

import dev.morphia.Datastore;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.offer.OfferFactory;
import ulaval.glo2003.domain.offer.OfferRepository;
import ulaval.glo2003.domain.product.ProductFactory;
import ulaval.glo2003.domain.product.ProductRepository;
import ulaval.glo2003.domain.seller.SellerFactory;
import ulaval.glo2003.domain.seller.SellerRepository;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoBuyerAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoOfferAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoProductAssembler;
import ulaval.glo2003.infrastructure.mongo.assemblers.MongoSellerAssembler;
import ulaval.glo2003.infrastructure.mongo.repositories.MongoOfferRepository;
import ulaval.glo2003.infrastructure.mongo.repositories.MongoProductRepository;
import ulaval.glo2003.infrastructure.mongo.repositories.MongoSellerRepository;
import ulaval.glo2003.services.MongoService;
import ulaval.glo2003.ui.common.mappers.InvalidParamExceptionMapper;
import ulaval.glo2003.ui.common.mappers.ItemNotFoundExceptionMapper;
import ulaval.glo2003.ui.common.mappers.MissingParamExceptionMapper;
import ulaval.glo2003.ui.health.HealthResource;
import ulaval.glo2003.ui.product.ProductResource;
import ulaval.glo2003.ui.seller.SellerResource;

import java.io.IOException;
import java.net.URI;

public final class Main {

    private static final String MORPHIA_PACKAGE_NAME = "ulaval.glo2003.domain.mongo.entities";

    private static HttpServer server;

    private Main() {

    }

    public static void main(final String[] args) throws IOException {
        startServer();
    }

    public static void startServer() throws IOException {
        String mongoConnectionString = FloppaConfiguration.getMongoConnectionString();
        String mongoDatabaseName = FloppaConfiguration.getMongoDatabaseName();

        System.out.printf("Connecting to database %s...\n", mongoDatabaseName);

        Datastore datastore = MongoService.createMorphiaDatastore(
                mongoConnectionString,
                mongoDatabaseName,
                MORPHIA_PACKAGE_NAME);

        startServer(datastore, mongoDatabaseName);
    }

    public static void startServer(final Datastore datastore, final String mongoDatabaseName) throws IOException {
        MongoBuyerAssembler mongoBuyerAssembler = new MongoBuyerAssembler();
        MongoOfferAssembler mongoOfferAssembler = new MongoOfferAssembler(mongoBuyerAssembler);
        MongoProductAssembler mongoProductAssembler = new MongoProductAssembler(mongoOfferAssembler);
        MongoSellerAssembler mongoSellerAssembler = new MongoSellerAssembler(mongoProductAssembler);

        ProductRepository productRepository = new MongoProductRepository(datastore, mongoProductAssembler);
        SellerRepository sellerRepository = new MongoSellerRepository(datastore, mongoSellerAssembler);
        OfferRepository offerRepository = new MongoOfferRepository(datastore, mongoOfferAssembler);

        System.out.printf("Connection to database %s done.\n", mongoDatabaseName);

        ProductFactory productFactory = new ProductFactory();
        SellerFactory sellerFactory = new SellerFactory();
        OfferFactory offerFactory = new OfferFactory();

        ResourceConfig resourceConfig = new ResourceConfig()
                .register(JacksonJaxbJsonProvider.class)
                .register(new ItemNotFoundExceptionMapper())
                .register(new InvalidParamExceptionMapper())
                .register(new MissingParamExceptionMapper())
                .register(new HealthResource(datastore))
                .register(new SellerResource(sellerRepository, sellerFactory))
                .register(new ProductResource(productFactory,
                        offerFactory,
                        productRepository,
                        sellerRepository,
                        offerRepository));

        URI uri = FloppaConfiguration.getServerHostUrl();

        server = GrizzlyHttpServerFactory.createHttpServer(uri, resourceConfig);
        server.start();
    }

    public static void shutdownServerNow() {
        if (server != null) {
            server.shutdownNow();
        }
    }
}
