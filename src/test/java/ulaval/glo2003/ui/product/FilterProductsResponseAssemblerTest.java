package ulaval.glo2003.ui.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.product.Product;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.ui.common.responses.components.LimitedOffersData;
import ulaval.glo2003.ui.product.responses.*;
import ulaval.glo2003.utils.TestProductBuilder;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FilterProductsResponseAssemblerTest {

    private static final Random RANDOM = new Random();
    private static final int MAX_ASSEMBLER_ARGS_ENTRIES_COUNT = 50;
    private static FilterProductsResponseAssembler assembler;
    private static List<FilterProductsResponseAssemblerArgsEntry> assemblerArgsEntries;

    @BeforeAll
    public static void setup() {
        assembler = new FilterProductsResponseAssembler();
        assemblerArgsEntries = generateAssemblerArgsEntries();
    }

    private static List<FilterProductsResponseAssemblerArgsEntry> generateAssemblerArgsEntries() {
        List<FilterProductsResponseAssemblerArgsEntry> assemblerArgsEntries = new ArrayList<>();
        int assemblerArgsEntriesCount = RANDOM.nextInt(MAX_ASSEMBLER_ARGS_ENTRIES_COUNT);
        for (int i = 0; i < assemblerArgsEntriesCount; i++) {
            assemblerArgsEntries.add(generateAssemblerArgsEntry());
        }

        return assemblerArgsEntries;
    }

    private static FilterProductsResponseAssemblerArgsEntry generateAssemblerArgsEntry() {
        Product product = new TestProductBuilder().build();
        Seller seller = new TestSellerBuilder().build();

        return new FilterProductsResponseAssemblerArgsEntry(product, seller);
    }

    @Test
    public void givenNullAssemblerArgs_whenToResponse_thenThrowsNullPointerException() {
        List<FilterProductsResponseAssemblerArgsEntry> assemblerArgsEntries = null;

        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(assemblerArgsEntries));
    }

    @Test
    public void givenAssemblerArgsWithNull_whenToResponse_thenThrowsNullPointerException() {
        List<FilterProductsResponseAssemblerArgsEntry> assemblerArgsEntries = new ArrayList<>();
        assemblerArgsEntries.add(null);

        Assertions.assertThrows(NullPointerException.class, () -> assembler.toResponse(assemblerArgsEntries));
    }

    @Test
    public void givenValidAssemblerArgs_whenToResponse_thenResponseProductsAreCorrect() {
        FilterProductsResponse response = assembler.toResponse(assemblerArgsEntries);

        assertEntriesEqualsResponseProducts(assemblerArgsEntries, response.products);
    }

    private void assertEntriesEqualsResponseProducts(
            final List<FilterProductsResponseAssemblerArgsEntry> entries,
            final List<FilterProductsResponseProduct> responseProducts) {
        Assertions.assertEquals(entries.size(), responseProducts.size());

        for (int i = 0; i < entries.size(); i++) {
            assertEntryEqualsResponseProduct(entries.get(i), responseProducts.get(i));
        }
    }

    private void assertEntryEqualsResponseProduct(
            final FilterProductsResponseAssemblerArgsEntry entry,
            final FilterProductsResponseProduct responseProduct) {
        assertEntryProductEqualsResponseProduct(
                entry.getProduct(),
                responseProduct);
        assertEntrySellerEqualsResponseProductSeller(
                entry.getProductSeller(),
                responseProduct.seller);
        assertEntryProductOffersEqualsResponseProductOffersMetadata(
                entry.getProduct().getOffers(),
                responseProduct.offers);
    }

    private void assertEntryProductEqualsResponseProduct(
            final Product entryProduct,
            final FilterProductsResponseProduct responseProduct) {
        Assertions.assertEquals(entryProduct.getId(), responseProduct.id);
        Assertions.assertEquals(entryProduct.getCreatedAt(), responseProduct.createdAt);
        Assertions.assertEquals(entryProduct.getTitle(), responseProduct.title);
        Assertions.assertEquals(entryProduct.getDescription(), responseProduct.description);
        Assertions.assertEquals(entryProduct.getSuggestedPrice(), responseProduct.suggestedPrice);
        Assertions.assertIterableEquals(entryProduct.getCategories(), responseProduct.categories);
    }

    private void assertEntrySellerEqualsResponseProductSeller(
            final Seller entrySeller,
            final FilterProductsResponseSeller responseSeller) {
        Assertions.assertEquals(entrySeller.getId(), responseSeller.id);
        Assertions.assertEquals(entrySeller.getName(), responseSeller.name);
    }

    private void assertEntryProductOffersEqualsResponseProductOffersMetadata(
            final List<Offer> entryProductOffers,
            final LimitedOffersData responseProductLimitedOffersData) {
        LimitedOffersData productLimitedOffersData = new LimitedOffersData(entryProductOffers);

        Assertions.assertEquals(productLimitedOffersData.mean, responseProductLimitedOffersData.mean);
        Assertions.assertEquals(productLimitedOffersData.count, responseProductLimitedOffersData.count);
    }
}
