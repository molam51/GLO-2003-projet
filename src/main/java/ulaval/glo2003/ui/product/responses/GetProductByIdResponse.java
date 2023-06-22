package ulaval.glo2003.ui.product.responses;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.ui.common.responses.components.LimitedOffersData;
import ulaval.glo2003.ui.common.serializers.OffsetDateTimeISO8601Serializer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JsonbPropertyOrder({"id", "createdAt", "title", "description", "suggestedPrice", "categories", "seller", "offers"})
public class GetProductByIdResponse {
    public UUID id;
    @JsonbTypeSerializer(OffsetDateTimeISO8601Serializer.class)
    public OffsetDateTime createdAt;
    public String title;
    public String description;
    public double suggestedPrice;
    public List<ProductCategory> categories;
    public GetProductByIdResponseSeller seller;
    public LimitedOffersData offers;
}
