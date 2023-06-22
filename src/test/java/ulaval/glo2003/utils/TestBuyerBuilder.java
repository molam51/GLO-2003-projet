package ulaval.glo2003.utils;

import org.apache.commons.lang3.RandomStringUtils;
import ulaval.glo2003.domain.offer.Buyer;

public class TestBuyerBuilder implements TestBuilder<Buyer> {

    private final int minNameLength;
    private final int maxNameLength;
    private final int minEmailIdentifierLength;
    private final int maxEmailIdentifierLength;
    private final int minEmailDomainLength;
    private final int maxEmailDomainLength;
    private final int minEmailExtensionLength;
    private final int maxEmailExtensionLength;
    private final int phoneNumberLength;

    public TestBuyerBuilder() {
        minNameLength = 5;
        maxNameLength = 25;
        minEmailIdentifierLength = 5;
        maxEmailIdentifierLength = 25;
        minEmailDomainLength = 5;
        maxEmailDomainLength = 25;
        minEmailExtensionLength = 3;
        maxEmailExtensionLength = 5;
        phoneNumberLength = 11;
    }

    @Override
    public Buyer build() {
        return new Buyer(generateName(), generateEmail(), generatePhoneNumber());
    }

    private String generateEmail() {
        String emailIdentifier = RandomStringUtils.randomAlphanumeric(minEmailIdentifierLength, maxEmailIdentifierLength);
        String emailDomain = RandomStringUtils.randomAlphanumeric(minEmailDomainLength, maxEmailDomainLength);
        String emailExtension = RandomStringUtils.randomAlphanumeric(minEmailExtensionLength, maxEmailExtensionLength);

        return String.format("%s@%s.%s", emailIdentifier, emailDomain, emailExtension);
    }

    private String generateName() {
        return RandomStringUtils.randomAlphanumeric(minNameLength, maxNameLength);
    }

    private String generatePhoneNumber() {
        return RandomStringUtils.randomNumeric(phoneNumberLength);
    }
}
