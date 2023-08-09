package signiel.heartsigniel.model.meeting.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum SignalCode implements ResponseCode {
    SIGNAL_TRANSMISSION_SUCCESS(7011, "Signal transmission successful"),
    SIGNAL_TRANSMISSION_FAILURE(7012, "Signal transmission failed"),
    ALL_SIGNALS_RETRIEVED(7013, "All signals retrieved successfully"),
    ;

    private final int code;
    private final String message;

    SignalCode(int code, String message) {
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
