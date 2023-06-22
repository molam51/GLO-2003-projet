package ulaval.glo2003.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.domain.exceptions.MissingParamException;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.domain.offer.OfferCreationParameters;
import ulaval.glo2003.domain.offer.OfferFactory;
import ulaval.glo2003.utils.TestOfferBuilder;

public class OfferFactoryTest {

    private final Offer validOffer = new TestOfferBuilder().build();

    private OfferCreationParameters parameters;
    private OfferFactory offerFactory;

    @BeforeEach
    public void setup() {
        offerFactory = new OfferFactory();

        parameters = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                validOffer.getBuyer().getName(),
                validOffer.getBuyer().getEmail(),
                validOffer.getBuyer().getPhoneNumber());
    }

    @Test
    public void givenValidParams_whenCreateProduct_thenCreatesValidProduct() {
        Offer offer = offerFactory.create(parameters);

        Assertions.assertNotNull(offer.getId());
        Assertions.assertNotNull(offer.getCreatedAt());
        Assertions.assertEquals(validOffer.getAmount(), offer.getAmount());
        Assertions.assertEquals(validOffer.getMessage(), offer.getMessage());
        Assertions.assertEquals(validOffer.getBuyer().getName(), offer.getBuyer().getName());
        Assertions.assertEquals(validOffer.getBuyer().getEmail(), offer.getBuyer().getEmail());
        Assertions.assertEquals(validOffer.getBuyer().getPhoneNumber(), offer.getBuyer().getPhoneNumber());
    }

    @Test
    public void givenNullAmount_whenCreateOffer_thenThrowsMissingParamException() {
        OfferCreationParameters params = new OfferCreationParameters(
                null,
                validOffer.getMessage(),
                validOffer.getBuyer().getName(),
                validOffer.getBuyer().getEmail(),
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(MissingParamException.class,
                () -> offerFactory.create(params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public void givenBlankMessage_whenCreateOffer_thenThrowsInvalidParamException(final String string) {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                string,
                validOffer.getBuyer().getName(),
                validOffer.getBuyer().getEmail(),
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(InvalidParamException.class,
                () -> offerFactory.create(params));
    }

    @Test
    public void givenNullMessage_whenCreateOffer_thenThrowsMissingParamException() {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                null,
                validOffer.getBuyer().getName(),
                validOffer.getBuyer().getEmail(),
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(MissingParamException.class,
                () -> offerFactory.create(params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public void givenBlankBuyerName_whenCreateOffer_thenThrowsInvalidParamException(final String string) {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                string,
                validOffer.getBuyer().getEmail(),
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(InvalidParamException.class,
                () -> offerFactory.create(params));
    }

    @Test
    public void givenNullBuyerName_whenCreateOffer_thenThrowsMissingParamException() {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                null,
                validOffer.getBuyer().getEmail(),
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(MissingParamException.class,
                () -> offerFactory.create(params));
    }

    @Test
    public void givenNullBuyerEmail_whenCreateOffer_thenThrowsMissingParamException() {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                validOffer.getBuyer().getName(),
                null,
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(MissingParamException.class,
                () -> offerFactory.create(params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t", "f", "d@m", "e.f", "@.", "@h.d", "d@.d", "d@d.", "d@d..d", "d@@d.d", "mon email est anthony@gmail.com"})
    public void givenInvalidBuyerEmail_whenCreateOffer_thenThrowsInvalidParamException(final String string) {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                validOffer.getBuyer().getName(),
                string,
                validOffer.getBuyer().getPhoneNumber());

        Assertions.assertThrows(InvalidParamException.class,
                () -> offerFactory.create(params));
    }

    @Test
    public void givenNullBuyerPhoneNumber_whenCreateOffer_thenThrowsMissingParamException() {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                validOffer.getBuyer().getName(),
                validOffer.getBuyer().getEmail(),
                null);

        Assertions.assertThrows(MissingParamException.class,
                () -> offerFactory.create(params));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t", "12", "123456798458", "a", "1234679845s", "123 345 56766", "123 wer 56766"})
    public void givenInvalidBuyerPhoneNumber_whenCreateOffer_thenThrowsInvalidParamException(final String string) {
        OfferCreationParameters params = new OfferCreationParameters(
                validOffer.getAmount(),
                validOffer.getMessage(),
                validOffer.getBuyer().getName(),
                validOffer.getBuyer().getEmail(),
                string);

        Assertions.assertThrows(InvalidParamException.class,
                () -> offerFactory.create(params));
    }
}
