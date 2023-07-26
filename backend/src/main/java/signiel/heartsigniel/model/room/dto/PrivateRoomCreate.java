package signiel.heartsigniel.model.room.dto;


import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
public class PrivateRoomCreate {


    private Long memberId;

    @NotNull
    @Size(min = 1, max = 20, message = "최소 1글자 및 최대 20글자의 제목을 입력해야합니다.")
    @NotBlank
    private String title;

    @NotNull(message = "제한 레이팅을 설정해주세요")
    @NotBlank
    private Long ratingLimit;

    @NotNull(message = "메기 여부를 설정해주세요")
    private boolean megiAcceptable;
}
