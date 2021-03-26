package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum ActeRole {
    PAR_DEFAUT("par défaut"),
    NON_SPECIFIE("non par défaut");

    @Getter @Setter
    private String libelle;

    ActeRole(String libelle) {
        this.libelle = libelle;
    }
}
