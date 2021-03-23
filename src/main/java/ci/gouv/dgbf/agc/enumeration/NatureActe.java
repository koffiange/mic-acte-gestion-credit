package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum NatureActe {
    LOI("1","Loi", "Acte Législatif (AL)"),
    ORDONNANCE("2", "Ordonnance", "Acte Règlementaire (AR)"),
    DECRET("3", "Décret","Acte Législatif (AL)"),
    ARRETE_MINISTERIEL("4", "Arrêté Ministériel", "Acte Règlementaire (AR)"),
    ARRETE_INTERMINISTERIEL("5", "Arrêté Interministériel", "Acte Règlementaire (AR)"),
    DECISION("6", "Décision","Acte Règlementaire (AR)"),
    AUTRE_ACTE("7", "Décision","Acte Règlementaire (AR)");

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
