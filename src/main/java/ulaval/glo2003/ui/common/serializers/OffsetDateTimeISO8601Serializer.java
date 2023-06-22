package ulaval.glo2003.ui.common.serializers;

import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeISO8601Serializer implements JsonbSerializer<OffsetDateTime> {

    @Override
    public void serialize(final OffsetDateTime dateTime,
                          final JsonGenerator jsonGenerator,
                          final SerializationContext serializationContext) {
        if (dateTime != null) {
            String json = dateTime.withOffsetSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            jsonGenerator.write(json);
        }
    }
}
