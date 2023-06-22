package ulaval.glo2003.infrastructure.mongo.entities;

import dev.morphia.annotations.Entity;

@Entity("Buyers")
public class MongoBuyer {

    private String name;
    private String email;
    private String phoneNumber;

    public MongoBuyer() {

    }

    public MongoBuyer(final String name, final String email, final String phoneNumber) {
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
