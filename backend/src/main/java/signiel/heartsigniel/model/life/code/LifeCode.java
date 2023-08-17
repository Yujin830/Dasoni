package signiel.heartsigniel.model.life.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum LifeCode implements ResponseCode {

    USE_LIFE(8000, "하트 1개를 사용하셨습니다."),
    LACK_OF_LIFE(8001, "하트가 부족합니다."),
    FAIL_USING_LIFE(8002, "하트 사용에 실패하였습니다.")
    ;

    private final int code;
    private final String message;

    LifeCode(int code, String message) {
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
