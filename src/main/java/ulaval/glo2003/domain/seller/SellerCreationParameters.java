package ulaval.glo2003.domain.seller;

import java.time.LocalDate;

public class SellerCreationParameters {

    private final String name;
    private final String bio;
    private final LocalDate birthDate;

    public SellerCreationParameters(final String name, final String bio, final LocalDate birthDate) {
        this.name = name;
        this.bio = bio;
        this.birthDate = birthDate;
    }

    public final String getName() {
        return name;
    }

    public final String getBio() {
        return bio;
    }

    public final LocalDate getBirthDate() {
        return birthDate;
    }
}
