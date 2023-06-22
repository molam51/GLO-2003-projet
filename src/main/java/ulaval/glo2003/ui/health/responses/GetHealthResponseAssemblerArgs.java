package ulaval.glo2003.ui.health.responses;

public class GetHealthResponseAssemblerArgs {

    private final boolean api;
    private final boolean db;

    public GetHealthResponseAssemblerArgs(final boolean api, final boolean db) {
        this.api = api;
        this.db = db;
    }

    public boolean getApi() {
        return this.api;
    }

    public boolean getDB() {
        return this.db;
    }
}
