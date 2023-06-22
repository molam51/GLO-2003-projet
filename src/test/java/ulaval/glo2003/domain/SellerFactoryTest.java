package ulaval.glo2003.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;
import ulaval.glo2003.domain.seller.Seller;
import ulaval.glo2003.domain.seller.SellerCreationParameters;
import ulaval.glo2003.domain.seller.SellerFactory;
import ulaval.glo2003.utils.TestSellerBuilder;

import java.time.LocalDate;
import java.util.stream.Stream;

public class SellerFactoryTest {

    private final Seller validSeller = new TestSellerBuilder().build();

    private static final int DELTA_YEARS = 17;

    private SellerFactory sellerFactory;

    private static Stream<LocalDate> invalidSellerBirthDateGenerator() {
        return Stream.of(
                LocalDate.now(),
                LocalDate.now().minusYears(DELTA_YEARS),
                LocalDate.now().plusYears(DELTA_YEARS));
    }

    @BeforeEach
    public void setup() {
        sellerFactory = new SellerFactory();
    }

    @Test
    public void givenValidParams_whenCreateSeller_thenCreatesValidSeller() {
        SellerCreationParameters params = new SellerCreationParameters(
                validSeller.getName(),
                validSeller.getBio(),
                validSeller.getBirthDate());

        Seller seller = sellerFactory.create(params);

        Assertions.assertNotNull(seller.getId());
        Assertions.assertNotNull(seller.getCreatedAt());
        Assertions.assertEquals(validSeller.getName(), seller.getName());
        Assertions.assertEquals(validSeller.getBio(), seller.getBio());
        Assertions.assertEquals(validSeller.getBirthDate(), seller.getBirthDate());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public void givenBlankName_whenCreateSeller_thenThrowsInvalidParamException(final String string) {
        SellerCreationParameters params = new SellerCreationParameters(
                string,
                validSeller.getBio(),
                validSeller.getBirthDate());

        Assertions.assertThrows(InvalidParamException.class, () -> sellerFactory.create(params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public void givenBlankBio_whenCreateSeller_thenThrowsInvalidParamException(final String string) {
        SellerCreationParameters params = new SellerCreationParameters(
                validSeller.getName(),
                string,
                validSeller.getBirthDate());

        Assertions.assertThrows(InvalidParamException.class, () -> sellerFactory.create(params));
    }

    @ParameterizedTest
    @MethodSource("invalidSellerBirthDateGenerator")
    public void givenInvalidDate_whenCreateSeller_thenThrowsInvalidParamException(final LocalDate birthDate) {
        SellerCreationParameters params = new SellerCreationParameters(
                validSeller.getName(),
                validSeller.getBio(),
                birthDate);

        Assertions.assertThrows(InvalidParamException.class, () -> sellerFactory.create(params));
    }

    @Test
    public void givenNullName_whenCreateSeller_thenThrowsMissingParamException() {
        SellerCreationParameters params = new SellerCreationParameters(
                null,
                validSeller.getBio(),
                validSeller.getBirthDate());

        Assertions.assertThrows(MissingParamException.class,
                () -> sellerFactory.create(params));
    }

    @Test
    public void givenNullBio_whenCreateSeller_thenThrowsMissingParamException() {
        SellerCreationParameters params = new SellerCreationParameters(
                validSeller.getName(),
                null,
                validSeller.getBirthDate());

        Assertions.assertThrows(MissingParamException.class,
                () -> sellerFactory.create(params));
    }

    @Test
    public void givenNullBirthDate_whenCreateSeller_thenThrowsMissingParamException() {
        SellerCreationParameters params = new SellerCreationParameters(
                validSeller.getName(),
                validSeller.getBio(),
                null);

        Assertions.assertThrows(MissingParamException.class,
                () -> sellerFactory.create(params));
    }
}
