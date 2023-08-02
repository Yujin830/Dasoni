package signiel.heartsigniel.model.alarm.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum AlarmCode implements ResponseCode {
    ALARM_SUBSCRIBED(3000, "알림 구독이 완료되었습니다."),
    ALARM_UNSUBSCRIBED(3001, "알림 구독이 취소되었습니다."),
    ALARM_SEND_FAIL(3002, "알림 전송에 실패하였습니다."),
    ALARM_SSE_TIMEOUT(3003, "SSE 연결 시간초과");

    private final int code;
    private final String message;

    AlarmCode(int code, String message) {
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
