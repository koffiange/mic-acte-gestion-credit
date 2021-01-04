package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum StatutActe {
    EN_ATTENTE("En attente d'application"),
    APPLIQUE("Acte appliqué"),
    APPLICATION_ECHOUE("Application échoué pour défaut de crédit");

    @Getter @Setter
    private String libelle;

    StatutActe(String libelle) {
        this.libelle = libelle;
    }
}
