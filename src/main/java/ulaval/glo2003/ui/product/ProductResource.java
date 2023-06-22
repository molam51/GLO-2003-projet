package ulaval.glo2003.ui.product;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ulaval.glo2003.application.FloppaConfiguration;
import ulaval.glo2003.domain.EntityFactory;
import ulaval.glo2003.domain.exceptions.MissingParamException;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.offer.OfferCreationParameters;
import ulaval.glo2003.domain.offer.OfferRepository;
import ulaval.glo2003.domain.product.*;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.domain.seller.SellerRepository;
import ulaval.glo2003.ui.offer.OfferUtilities;
import ulaval.glo2003.ui.product.requests.GetProductsByFiltersRequest;
import ulaval.glo2003.ui.product.requests.PostOfferToProductRequest;
import ulaval.glo2003.ui.product.requests.PostProductRequest;
import ulaval.glo2003.ui.product.responses.*;
import ulaval.glo2003.ui.seller.SellerUtilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/products")
public class ProductResource {

    private final EntityFactory<Product> productFactory;
    private final EntityFactory<Offer> offerFactory;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final OfferRepository offerRepository;

    public ProductResource(final EntityFactory<Product> productFactory,
                           final EntityFactory<Offer> offerFactory,
                           final ProductRepository productRepository,
                           final SellerRepository sellerRepository,
                           final OfferRepository offerRepository) {
        this.productFactory = productFactory;
        this.offerFactory = offerFactory;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.offerRepository = offerRepository;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterProducts(@BeanParam final GetProductsByFiltersRequest request) {
        UUID parsedSellerId = SellerUtilities.parseStringToSellerUUID(request.sellerId);
        List<ProductCategory> parsedCategories = ProductUtilities.parseStringsToProductCategories(request.categories);
        Double parsedMinPrice = ProductUtilities.parseStringToMinPrice(request.minPrice);
        Double parsedMaxPrice = ProductUtilities.parseStringToMaxPrice(request.maxPrice);

        Seller seller = null;

        if (parsedSellerId != null) {
            seller = sellerRepository.fetch(parsedSellerId);
        }

        List<Product> products = productRepository.getAll();

        ProductFilter productFilter = new ProductFilter(
                seller,
                request.title,
                parsedCategories,
                parsedMinPrice,
                parsedMaxPrice);
        List<Product> filteredProducts = productFilter.filterEntities(products);

        FilterProductsResponseAssembler assembler = new FilterProductsResponseAssembler();
        List<FilterProductsResponseAssemblerArgsEntry> assemblerArgsEntries = productsToAssemblerArgsEntries(filteredProducts);
        FilterProductsResponse filterProductsResponse = assembler.toResponse(assemblerArgsEntries);

        return Response.ok(filterProductsResponse).build();
    }

    private List<FilterProductsResponseAssemblerArgsEntry> productsToAssemblerArgsEntries(
            final List<Product> products) {
        List<FilterProductsResponseAssemblerArgsEntry> assemblerArgsEntries = new ArrayList<>();

        for (Product product : products) {
            Seller productSeller = sellerRepository.fetchByProduct(product.getId());

            FilterProductsResponseAssemblerArgsEntry assemblerArgsEntry = new FilterProductsResponseAssemblerArgsEntry(
                    product,
                    productSeller);

            assemblerArgsEntries.add(assemblerArgsEntry);
        }

        return assemblerArgsEntries;
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") final String id) {
        UUID parsedProductId = ProductUtilities.parseStringToProductUUID(id);

        Product product = productRepository.fetch(parsedProductId);
        Seller productSeller = sellerRepository.fetchByProduct(parsedProductId);

        GetProductByIdResponseAssembler assembler = new GetProductByIdResponseAssembler();
        GetProductByIdResponseAssemblerArgs args = new GetProductByIdResponseAssemblerArgs(product, productSeller);

        return Response.ok(assembler.toResponse(args)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postProduct(final @HeaderParam("X-Seller-Id") String sellerId, final PostProductRequest request) {
        UUID parsedSellerId = SellerUtilities.parseStringToSellerUUID(sellerId);

        if (sellerId == null) {
            throw new MissingParamException("The seller ID is missing");
        }

        Double parsedSuggestedPrice = ProductUtilities.parseStringToSuggestedPrice(request.suggestedPrice);
        List<ProductCategory> parsedCategories = ProductUtilities.parseStringsToProductCategories(request.categories);

        Seller seller = sellerRepository.fetch(parsedSellerId);

        ProductCreationParameters productParams = new ProductCreationParameters(
                request.title,
                request.description,
                parsedSuggestedPrice,
                parsedCategories);
        Product product = productFactory.create(productParams);
        productRepository.add(product);

        seller.addProduct(product);
        sellerRepository.update(seller);

        URI uri = URI.create(String.format("%s/products/%s", FloppaConfiguration.getApiHostUrl(), product.getId()));

        return Response.created(uri).build();
    }

    @POST
    @Path("/{id}/offers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postOfferToProduct(final @PathParam("id") String productId, final PostOfferToProductRequest request) {
        UUID parsedProductId = ProductUtilities.parseStringToProductUUID(productId);
        Double parsedAmount = OfferUtilities.parseStringToAmount(request.amount);

        OfferCreationParameters offerParams = new OfferCreationParameters(
                parsedAmount,
                request.message,
                request.name,
                request.email,
                request.phoneNumber);
        Offer offer = offerFactory.create(offerParams);
        offerRepository.add(offer);

        Product product = productRepository.fetch(parsedProductId);
        product.addOffer(offer);
        productRepository.update(product);

        return Response.ok().build();
    }

    @POST
    @Path("/{id}/views")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postViewsToProduct(final @PathParam("id") String productId) {
        UUID parsedProductId = ProductUtilities.parseStringToProductUUID(productId);

        Product product = productRepository.fetch(parsedProductId);
        product.incrementViews();
        productRepository.update(product);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/views")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductViews(@PathParam("id") final String id) {
        UUID parsedProductId = ProductUtilities.parseStringToProductUUID(id);

        Product product = productRepository.fetch(parsedProductId);

        GetProductViewsResponseAssembler assembler = new GetProductViewsResponseAssembler();

        return Response.ok(assembler.toResponse(product)).build();
    }
}
