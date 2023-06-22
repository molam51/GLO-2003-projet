package ulaval.glo2003.domain.offer;

public class OfferCreationParameters {

    private final Double amount;
    private final String message;
    private final String name;
    private final String email;
    private final String phoneNumber;

    public OfferCreationParameters(final Double amount, final String message,
                                   final String name, final String email, final String phoneNumber) {
        this.amount = amount;
        this.message = message;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public final Double getAmount() {
        return amount;
    }

    public final String getMessage() {
        return message;
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
