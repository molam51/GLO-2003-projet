package ulaval.glo2003.application;

import com.mongodb.MongoException;
import dev.morphia.Datastore;
import ulaval.glo2003.infrastructure.mongo.entities.MongoHealthCheck;

public final class FloppaMongoUtilities {

    private FloppaMongoUtilities() {

    }

    public static boolean isDatastoreOperational(final Datastore datastore) {
        try {
            datastore.save(new MongoHealthCheck());

            return true;
        } catch (MongoException exception) {
            return false;
        }
    }
}
