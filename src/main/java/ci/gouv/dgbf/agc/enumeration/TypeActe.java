package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum TypeActe {
    ACTE_REGLEMENT("AR", "Acte de Règlement"),
    ACTE_LEGISLATIF("AL", "Acte de Législatif");

    @Getter @Setter
    private String code;
    @Getter @Setter
    private String libelle;

    TypeActe(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
    }
}
