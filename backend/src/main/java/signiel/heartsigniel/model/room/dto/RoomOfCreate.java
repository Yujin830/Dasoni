package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 방 생성을 위한 Dto
 */


@Getter
@Builder
public class RoomOfCreate {
    @NotNull
    @Size(min = 1, max = 20, message = "최소 1글자 및 최대 20글자의 제목을 입력해야합니다.")
    @NotBlank
    private String roomTitle;

    @NotNull(message = "메기 여부를 선택해야 합니다.")
    @NotBlank
    private boolean megiAcceptable;

    @NotNull(message = "랭크 제한을 설정해야 합니다.")
    @NotBlank
    private Long ratingLimit;

    @NotNull
    private boolean roomType;

}
