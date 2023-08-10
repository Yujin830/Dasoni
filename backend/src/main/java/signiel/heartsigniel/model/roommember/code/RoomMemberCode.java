package signiel.heartsigniel.model.roommember.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum RoomMemberCode implements ResponseCode {
    CREATE_SUCCESS(3000, "룸 멤버 생성 성공"),
    CREATE_FAIL(3001, "룸 멤버 생성 실패"),
    READ_SUCCESS(3002, "룸 멤버 조회 성공"),
    READ_FAIL(3003, "룸 멤버 조회 실패"),
    UPDATE_SUCCESS(3004, "룸 멤버 수정 성공"),
    UPDATE_FAIL(3005, "룸 멤버 수정 실패"),
    DELETE_SUCCESS(3006, "룸 멤버 삭제 성공"),
    DELETE_FAIL(3007, "룸 멤버 삭제 실패"),
    NOT_ROOM_LEADER(3008, "방장 권한이 없습니다."),
    FETCH_MEETING_RESULT_SUCCESS(3009, "룸 멤버의 미팅 결과 조회 성공"),
    FETCH_MEETING_RESULT_FAIL(3010, "룸 멤버의 미팅 결과 조회 실패");

    private int code;
    private String message;

    RoomMemberCode(int code, String message) {
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