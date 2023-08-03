package signiel.heartsigniel.model.matching.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum MatchingCode implements ResponseCode {
    MATCHING_STARTED(2100, "매칭이 시작되었습니다."),
    MATCHING_SUCCESS(2101, "매칭이 성공적으로 완료되었습니다."),
    MATCHING_FAILED(2102, "매칭에 실패하였습니다."),
    MATCHING_PENDING(2103, "매칭 대기 중입니다."),
    USER_ADDED_TO_MATCH(2104, "사용자가 매칭에 추가되었습니다."),
    USER_REMOVED_FROM_MATCH(2105, "사용자가 매칭에서 제거되었습니다."),
    ALREADY_IN_MATCHING_QUEUE(2106, "이미 매칭 대기열에 등록되어 있습니다.");

    private final int code;
    private final String message;

    MatchingCode(int code, String message) {
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