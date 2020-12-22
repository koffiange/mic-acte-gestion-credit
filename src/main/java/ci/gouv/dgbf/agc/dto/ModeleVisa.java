package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

public class ModeleVisa {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private String code;
    @Getter @Setter
    private String libelle;
    @Getter @Setter
    private String description;
}
