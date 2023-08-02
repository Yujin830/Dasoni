package signiel.heartsigniel.model.question.code;

import signiel.heartsigniel.common.code.ResponseCode;

public enum QuestionCode implements ResponseCode {
    SUCCESSFUL_QUESTIONS_SENT(6000, "질문 세 개를 성공적으로 보냈습니다."),
    INVALID_REQUEST(6001, "올바르지 않은 요청입니다."),
    QUESTION_NOT_FOUND(6002, "질문을 찾을 수 없습니다."),
    DUPLICATE_QUESTION(6003, "중복된 질문이 있습니다."),
    QUESTION_LIMIT_REACHED(6004, "질문 제한에 도달했습니다.");

    private final int code;
    private final String message;

    QuestionCode(int code, String message) {
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
