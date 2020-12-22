package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum StatutDemande {
    REFUSE("Refusée"),
    DIFFERE("Diferée"),
    VALIDER("Validée");

    @Getter @Setter
    private String libelle;

    StatutDemande(String libelle) {
        this.libelle = libelle;
    }
}
