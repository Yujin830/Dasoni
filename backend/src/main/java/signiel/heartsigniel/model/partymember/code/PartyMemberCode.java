package signiel.heartsigniel.model.partymember.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum PartyMemberCode implements ResponseCode {
    CREATE_SUCCESS(3000, "파티 멤버 생성 성공"),
    CREATE_FAIL(3001, "파티 멤버 생성 실패"),
    READ_SUCCESS(3002, "파티 멤버 조회 성공"),
    READ_FAIL(3003, "파티 멤버 조회 실패"),
    UPDATE_SUCCESS(3004, "파티 멤버 수정 성공"),
    UPDATE_FAIL(3005, "파티 멤버 수정 실패"),
    DELETE_SUCCESS(3006, "파티 멤버 삭제 성공"),
    DELETE_FAIL(3007, "파티 멤버 삭제 실패"),
    NOT_ROOM_LEADER(3008, "방장 권한이 없습니다.");

    private int code;
    private String message;

    PartyMemberCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}