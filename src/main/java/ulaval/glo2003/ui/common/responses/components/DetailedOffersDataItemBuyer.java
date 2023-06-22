package ulaval.glo2003.ui.common.responses.components;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"name", "email", "phoneNumber"})
public class DetailedOffersDataItemBuyer {
    public String name;
    public String email;
    public String phoneNumber;
}
