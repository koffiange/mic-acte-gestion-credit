package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class VisaDto extends BaseDto{
    @Getter @Setter
    private Visa visa;
    @Getter @Setter
    private Integer numeroOrdre;

    public VisaDto(Visa visa, Integer numeroOrdre) {
        this.visa = visa;
        this.numeroOrdre = numeroOrdre;
    }
}
