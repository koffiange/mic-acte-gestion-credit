package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum NatureActe {
    LOI("1","Loi", "Acte Législatif (AL)"),
    ORDONNANCE("2", "Ordonnance", "Acte Règlementaire (AR)"),
    DECRET("3", "Décret","Acte Législatif (AL)"),
    ARRETE("4", "Arrêté", "Acte Règlementaire (AR)"),
    DECISION("5", "Décision","Acte Règlementaire (AR)");

    @Getter @Setter
    private String numero;
    @Getter @Setter
    private String libelle;
    @Getter @Setter
    private String typeActe;

    NatureActe(String numero, String libelle, String typeActe) {
        this.numero = numero;
        this.libelle = libelle;
        this.typeActe = typeActe;
    }
}
