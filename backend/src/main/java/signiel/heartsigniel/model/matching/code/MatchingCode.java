package signiel.heartsigniel.model.matching.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum MatchingCode implements ResponseCode {
    NO_MATCH_FOUND(1300, "해당하는 매치가 존재하지 않습니다."),
    FULL_MATCH(1301, "매치가 가득 찼습니다."),
    TIMEOUT_MATCH(1302, "매치의 대기 시간이 지났습니다."),
    USER_OUT_FROM_MATCH(1303, "매치에서 성공적으로 나갔습니다."),
    MATCH_SUCCESS(1304, "매치가 성공적으로 이루어졌습니다."),
    ENTER_USER_TO_MATCH(1305, "사용자 1명이 매치에 참가했습니다."),
    MATCH_INFO_UPDATE(1306, "매치 정보가 업데이트 되었습니다."),
    SUCCESS_JOIN_MATCH(1307, "매치에 참가 완료했습니다."),
    NOT_PARTICIPATE_MATCH(1308, "참가하지 않은 매치입니다."),
    ABLE_TO_PARTICIPATE(1309, "해당 매치에 참가할 수 있습니다."),
    ALREADY_PARTICIPATING(1310, "이미 참여하고 있는 매치입니다."),
    NO_MATCH_PERMISSION(1311, "해당 기능을 수행할 권한이 없습니다.");

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

