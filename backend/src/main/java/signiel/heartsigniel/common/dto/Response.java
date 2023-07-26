package signiel.heartsigniel.common.dto;

import lombok.Getter;
import lombok.Setter;
import signiel.heartsigniel.common.code.ResponseCode;

@Setter
@Getter
public class Response<T> {
    private Status status;

    private T content;

    @Getter
    @Setter
    private static class Status {
        private int code;
        private String message;
    }

    /**
     * 응답 코드와 내용으로 응답 객체를 생성한다.
     *
     * @param responseCode 응답코드, 메시지를 가지는 ResponseCode 구현체
     * @param content      본문 내용
     * @return response 본문 전체 객체
     */
    public static <T> Response<T> of(ResponseCode responseCode, T content) {
        Response<T> response = new Response<>();

        Status status = new Status();
        status.setCode(responseCode.getCode());
        status.setMessage(responseCode.getMessage());

        response.setStatus(status);
        response.setContent(content);

        return response;

    }
}
