package ulaval.glo2003.ui.common.serializers;

import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import org.apache.commons.math3.util.Precision;

public class AmountSerializer implements JsonbSerializer<Double> {

    private static final int DECIMAL_PLACES = 2;

    @Override
    public void serialize(final Double amount,
                          final JsonGenerator jsonGenerator,
                          final SerializationContext serializationContext) {
        if (amount != null) {
            double json = Precision.round(amount, DECIMAL_PLACES);

            jsonGenerator.write(json);
        }
    }
}
