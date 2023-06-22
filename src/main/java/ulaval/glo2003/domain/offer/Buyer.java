package ulaval.glo2003.domain.offer;

public class Buyer {

    private final String name;
    private final String email;
    private final String phoneNumber;

    public Buyer(final String name, final String email, final String phoneNumber) {
        super();

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
