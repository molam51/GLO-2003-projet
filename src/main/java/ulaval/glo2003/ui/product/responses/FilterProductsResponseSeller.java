package ulaval.glo2003.ui.product.responses;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

import java.util.UUID;

@JsonbPropertyOrder({"id", "name"})
public class FilterProductsResponseSeller {
    public UUID id;
    public String name;
}
