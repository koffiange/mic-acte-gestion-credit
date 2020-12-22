package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

public class Visa {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private String code;
    @Getter @Setter
    private String intitule;

    public Visa() {
    }
}
