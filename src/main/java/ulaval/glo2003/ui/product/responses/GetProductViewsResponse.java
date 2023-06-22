package ulaval.glo2003.ui.product.responses;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"views"})
public class GetProductViewsResponse {
    public int views;
}
