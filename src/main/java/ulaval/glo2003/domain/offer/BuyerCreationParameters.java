package ulaval.glo2003.domain.offer;

public class BuyerCreationParameters {

    private final String name;
    private final String email;
    private final String phoneNumber;

    public BuyerCreationParameters(final String name, final String email, final String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public final String getName() {
        return name;
    }

    public final String getEmail() {
        return email;
    }

    public final String getPhoneNumber() {
        return phoneNumber;
    }
}
