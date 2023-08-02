package signiel.heartsigniel.model.rating.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SignalResultRequest {

    private String signalType;
    private int[][] signalBoard = new int[6][6];

}