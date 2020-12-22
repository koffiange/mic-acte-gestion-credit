package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ModeleVisaDto {
    @Getter @Setter
    private ModeleVisa modeleVisa;
    @Getter @Setter
    private List<VisaDto> visaDtoList;

    public ModeleVisaDto(ModeleVisa modeleVisa, List<VisaDto> visaDtoList) {
        this.modeleVisa = modeleVisa;
        this.visaDtoList = visaDtoList;
    }
}
