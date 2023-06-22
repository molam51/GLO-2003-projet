package ulaval.glo2003.ui.seller.responses;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import ulaval.glo2003.domain.product.ProductCategory;
import ulaval.glo2003.ui.common.responses.components.DetailedOffersData;
import ulaval.glo2003.ui.common.serializers.AmountSerializer;
import ulaval.glo2003.ui.common.serializers.OffsetDateTimeISO8601Serializer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JsonbPropertyOrder({"id", "createdAt", "title", "description", "suggestedPrice", "categories", "offers"})
public class GetCurrentSellerResponseProduct {
    public UUID id;
    @JsonbTypeSerializer(OffsetDateTimeISO8601Serializer.class)
    public OffsetDateTime createdAt;
    public String title;
    public String description;
    @JsonbTypeSerializer(AmountSerializer.class)
    public double suggestedPrice;
    public List<ProductCategory> categories;
    public DetailedOffersData offers;
}
