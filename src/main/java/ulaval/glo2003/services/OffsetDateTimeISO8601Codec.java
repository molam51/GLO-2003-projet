package ulaval.glo2003.services;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeISO8601Codec implements Codec<OffsetDateTime> {

    @Override
    public OffsetDateTime decode(final BsonReader bsonReader, final DecoderContext decoderContext) {
        String str = bsonReader.readString();

        return OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME).withOffsetSameInstant(ZoneOffset.UTC);
    }

    @Override
    public void encode(final BsonWriter bsonWriter,
                       final OffsetDateTime offsetDateTime,
                       final EncoderContext encoderContext) {
        String str = offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        bsonWriter.writeString(str);
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
