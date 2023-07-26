package signiel.heartsigniel.common.code;

public enum CommonCode implements ResponseCode{
    GOOD_REQUEST(5000, "올바른 요청입니다."),
    VALIDATION_FAIL(1001, "입력값 검증이 실패하였습니다."),
    BAD_REQUEST(1000, "잘못된 요청입니다."),
    ILLEGAL_REQUEST(1002, "잘못된 데이터가 포함된 요청입니다.");
    private final int code;
    private final String message;

    CommonCode(int code, String message) {
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
