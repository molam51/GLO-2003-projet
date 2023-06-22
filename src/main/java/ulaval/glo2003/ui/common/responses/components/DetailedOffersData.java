package ulaval.glo2003.ui.common.responses.components;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import ulaval.glo2003.domain.offer.Offer;
import ulaval.glo2003.ui.common.responses.DetailedOffersDataItemAssembler;
import ulaval.glo2003.ui.common.serializers.AmountSerializer;

import java.util.ArrayList;
import java.util.List;

@JsonbPropertyOrder({"min", "max", "items"})
public class DetailedOffersData extends LimitedOffersData {

    @JsonbTypeSerializer(AmountSerializer.class)
    public Double min;
    @JsonbTypeSerializer(AmountSerializer.class)
    public Double max;
    public List<DetailedOffersDataItem> items;

    public DetailedOffersData() {

    }

    public DetailedOffersData(final List<Offer> offers) {
        super(offers);

        DetailedOffersDataItemAssembler detailedOffersDataItemAssembler = new DetailedOffersDataItemAssembler();
        items = new ArrayList<>();

        if (offers.size() > 0) {
            min = offers.stream().mapToDouble(Offer::getAmount).min().orElse(0.0);
            max = offers.stream().mapToDouble(Offer::getAmount).max().orElse(0.0);

            for (Offer offer : offers) {
                items.add(detailedOffersDataItemAssembler.toResponse(offer));
            }
        }
    }
}
