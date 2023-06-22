package ulaval.glo2003.application;

import java.net.URI;

public final class FloppaConfiguration {

    private static final String PORT_KEY = "PORT";

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_MONGO_PORT = 27017;

    private static final String FLOPPA_BASE_URL_KEY = "FLOPPA_BASE_URL";

    private static final String DEFAULT_SERVER_HOST_ADDRESS = "0.0.0.0";
    private static final String DEFAULT_API_HOST_ADDRESS = "localhost";

    private static final String MONGO_CLUSTER_URL_KEY = "MONGO_CLUSTER_URL";
    private static final String MONGO_CLUSTER_USERNAME_KEY = "MONGO_CLUSTER_USERNAME";
    private static final String MONGO_CLUSTER_PASSWORD_KEY = "MONGO_CLUSTER_PASSWORD";
    private static final String MONGO_DATABASE_NAME = "MONGO_DATABASE_NAME";

    private static final String MONGO_DEVELOPMENT_DATABASE_NAME = "floppa-api-dev";

    private FloppaConfiguration() {

    }

    public static int getServerPort() {
        if (System.getenv().containsKey(PORT_KEY)) {
            String portString = System.getenv(PORT_KEY);

            return Integer.parseInt(portString);
        }

        return DEFAULT_PORT;
    }

    public static URI getServerHostUrl() {
        String serverHostUrlString = String.format("http://%s:%d",
                DEFAULT_SERVER_HOST_ADDRESS,
                getServerPort());

        return URI.create(serverHostUrlString);
    }

    public static URI getApiHostUrl() {
        if (System.getenv().containsKey(FLOPPA_BASE_URL_KEY)) {
            String apiHostUrlString = System.getenv(FLOPPA_BASE_URL_KEY);

            return URI.create(apiHostUrlString);
        }

        return getLocalApiHostUrl();
    }

    public static URI getLocalApiHostUrl() {
        String defaultApiHostUrlString = String.format("http://%s:%d",
                DEFAULT_API_HOST_ADDRESS,
                getServerPort());

        return URI.create(defaultApiHostUrlString);
    }

    public static String getMongoConnectionString() {
        if (!System.getenv().containsKey(MONGO_CLUSTER_URL_KEY)
                || !System.getenv().containsKey(MONGO_CLUSTER_USERNAME_KEY)
                || !System.getenv().containsKey(MONGO_CLUSTER_PASSWORD_KEY)) {
            return getLocalMongoConnectionString();
        }

        String clusterUrl = System.getenv(MONGO_CLUSTER_URL_KEY);
        String databaseUsername = System.getenv(MONGO_CLUSTER_USERNAME_KEY);
        String databasePassword = System.getenv(MONGO_CLUSTER_PASSWORD_KEY);

        return String.format("mongodb+srv://%s:%s@%s", databaseUsername, databasePassword, clusterUrl);
    }

    public static String getLocalMongoConnectionString() {
        return String.format("mongodb://%s:%d", DEFAULT_API_HOST_ADDRESS, DEFAULT_MONGO_PORT);
    }

    public static String getMongoDatabaseName() {
        if (System.getenv().containsKey(MONGO_DATABASE_NAME)) {
            return System.getenv(MONGO_DATABASE_NAME);
        }

        return getMongoDevelopmentDatabaseName();
    }

    public static String getMongoDevelopmentDatabaseName() {
        return MONGO_DEVELOPMENT_DATABASE_NAME;
    }
}
