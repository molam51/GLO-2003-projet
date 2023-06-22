package ulaval.glo2003.ui.product.requests;

import jakarta.ws.rs.QueryParam;

import java.util.List;

public class GetProductsByFiltersRequest {

    @QueryParam("sellerId")
    public String sellerId;

    @QueryParam("title")
    public String title;

    @QueryParam("categories")
    public List<String> categories;

    @QueryParam("minPrice")
    public String minPrice;

    @QueryParam("maxPrice")
    public String maxPrice;
}
