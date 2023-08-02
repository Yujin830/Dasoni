package signiel.heartsigniel.model.partymember.code;

public enum PartyMemberCode {
    CREATE_SUCCESS("3000", "파티 멤버 생성 성공"),
    CREATE_FAIL("3001", "파티 멤버 생성 실패"),
    READ_SUCCESS("3002", "파티 멤버 조회 성공"),
    READ_FAIL("3003", "파티 멤버 조회 실패"),
    UPDATE_SUCCESS("3004", "파티 멤버 수정 성공"),
    UPDATE_FAIL("3005", "파티 멤버 수정 실패"),
    DELETE_SUCCESS("3006", "파티 멤버 삭제 성공"),
    DELETE_FAIL("3007", "파티 멤버 삭제 실패");

    private String code;
    private String description;

    PartyMemberCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}