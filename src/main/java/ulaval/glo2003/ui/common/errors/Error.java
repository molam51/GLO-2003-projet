package ulaval.glo2003.ui.common.errors;

public class Error {

    public static final String INVALID_PARAM = "INVALID_PARAMETER";
    public static final String MISSING_PARAM = "MISSING_PARAMETER";
    public static final String ITEM_NOT_FOUND = "ITEM_NOT_FOUND";

    private final String code;
    private final String description;

    public Error(final String code) {
        this(code, "");
    }

    public Error(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
