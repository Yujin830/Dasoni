package signiel.heartsigniel.model.matching.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum RoomQueueCode implements ResponseCode {
    ENQUEUE_SUCCESS(2200, "대기열에 등록되었습니다."),
    DEQUEUE_SUCCESS(2201, "대기열에서 성공적으로 제외되었습니다."),
    DEQUEUE_FAILED(2202, "해당 방이 큐에 존재하지 않습니다."),
    MATCHING_SUCCESS(2203, "매칭에 성공하였습니다.")
    ;

    private final int code;
    private final String message;

    RoomQueueCode(int code, String message) {
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