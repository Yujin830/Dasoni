package signiel.heartsigniel.model.guide.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideDto {

    private Long guideId;

    private String content;

    private Long visibleTime;
}
