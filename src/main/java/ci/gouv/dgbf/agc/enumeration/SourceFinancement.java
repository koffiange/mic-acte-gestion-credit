package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum SourceFinancement {
    DON("Don"),
    EMPRUNT("Emprunt"),
    RESOURCE_PROPRE("Ressources Propres"),
    TRESOR("Trésor");

    @Getter @Setter
    private String libelle;

    SourceFinancement(String libelle) {
        this.libelle = libelle;
    }
}
