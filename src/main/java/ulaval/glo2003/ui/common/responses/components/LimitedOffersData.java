package ulaval.glo2003.ui.common.responses.components;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.ui.common.serializers.AmountSerializer;

import java.util.List;

@JsonbPropertyOrder({"mean", "count"})
public class LimitedOffersData {

    public int count;
    @JsonbTypeSerializer(AmountSerializer.class)
    public Double mean;

    public LimitedOffersData() {

    }

    public LimitedOffersData(final List<Offer> offers) {
        count = offers.size();

        if (count > 0) {
            mean = offers.stream().mapToDouble(Offer::getAmount).average().orElse(0.0);
        }
    }
}
