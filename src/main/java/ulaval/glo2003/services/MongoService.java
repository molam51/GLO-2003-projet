package ulaval.glo2003.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.SocketSettings;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.concurrent.TimeUnit;

public class MongoService {

    private static final int TIMEOUT_IN_MILLISECONDS = 5000;

    private MongoService() {

    }

    public static Datastore createMorphiaDatastore(
            final String connectionString,
            final String databaseName,
            final String packageName) {
        Datastore datastore = Morphia.createDatastore(
                createMongoClient(connectionString),
                databaseName,
                getMorphiaMapperOptions());

        datastore.getMapper().mapPackage(packageName);

        return datastore;
    }

    private static MapperOptions getMorphiaMapperOptions() {
        return MapperOptions.builder()
                .storeEmpties(true)
                .build();
    }

    private static MongoClient createMongoClient(final String connectionString) {
        return MongoClients.create(createMongoClientSettings(connectionString));
    }

    private static MongoClientSettings createMongoClientSettings(final String connectionString) {
        return MongoClientSettings.builder()
                .applyToSocketSettings(MongoService::initMongoSocketSettings)
                .applyToClusterSettings(MongoService::initMongoClusterSettings)
                .applyConnectionString(new ConnectionString(connectionString))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(getMongoCodecRegistry())
                .build();
    }

    private static CodecRegistry getMongoCodecRegistry() {
        CodecRegistry defaultRegistry = MongoClientSettings.getDefaultCodecRegistry();
        CodecRegistry customRegistry = CodecRegistries.fromCodecs(
                new OffsetDateTimeISO8601Codec()
        );

        return CodecRegistries.fromRegistries(defaultRegistry, customRegistry);
    }

    private static void initMongoSocketSettings(final SocketSettings.Builder builder) {
        builder.connectTimeout(TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    private static void initMongoClusterSettings(final ClusterSettings.Builder builder) {
        builder.serverSelectionTimeout(TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
    }
}
