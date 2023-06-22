package ulaval.glo2003.ui.seller.responses;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import ulaval.glo2003.ui.common.serializers.OffsetDateTimeISO8601Serializer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JsonbPropertyOrder({"id", "createdAt", "name", "bio", "products"})
public class GetSellerByIdResponse {
    public UUID id;
    @JsonbTypeSerializer(OffsetDateTimeISO8601Serializer.class)
    public OffsetDateTime createdAt;
    public String name;
    public String bio;
    public List<GetSellerByIdResponseProduct> products;
}
