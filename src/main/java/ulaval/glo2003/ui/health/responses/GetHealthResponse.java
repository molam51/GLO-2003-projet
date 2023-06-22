package ulaval.glo2003.ui.health.responses;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({"api", "db"})
public class GetHealthResponse {
    public boolean api;
    public boolean db;
}
