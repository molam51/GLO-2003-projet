package ulaval.glo2003.ui.common.responses.components;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import ulaval.glo2003.ui.common.serializers.AmountSerializer;
import ulaval.glo2003.ui.common.serializers.OffsetDateTimeISO8601Serializer;

import java.time.OffsetDateTime;
import java.util.UUID;

@JsonbPropertyOrder({"id", "createdAt", "amount", "message", "buyer"})
public class DetailedOffersDataItem {
    public UUID id;
    @JsonbTypeSerializer(OffsetDateTimeISO8601Serializer.class)
    public OffsetDateTime createdAt;
    @JsonbTypeSerializer(AmountSerializer.class)
    public Double amount;
    public String message;
    public DetailedOffersDataItemBuyer buyer;
}
