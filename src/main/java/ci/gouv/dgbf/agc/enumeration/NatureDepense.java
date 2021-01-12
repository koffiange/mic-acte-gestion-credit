package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum NatureDepense {
    PERSONNEL("PERSONNEL"),
    BIEN_ET_SERVICE("BIEN ET SERVICE"),
    TRANSFERT("TRANSFERT"),
    INVESTISSEMENT("INVESTISSEMENT");

    @Getter @Setter
    private String libelle;

    NatureDepense(String libelle) {
        this.libelle = libelle;
    }
}
