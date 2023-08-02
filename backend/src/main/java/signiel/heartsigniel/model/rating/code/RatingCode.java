package signiel.heartsigniel.model.rating.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum RatingCode implements ResponseCode {
    CALCULATION_SUCCESS(7000, "Result calculation successful"),
    CALCULATION_FAILURE(7001, "Result calculation failed"),
    INVALID_INPUT(7002, "Invalid input provided"),
    ;

    private final int code;
    private final String message;

    RatingCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
