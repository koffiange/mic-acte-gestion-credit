package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum NatureTransaction {
    VIREMENT("Virement"),
    TRANSFERT("Transfert");

    private String libelle;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    NatureTransaction(String libelle) {
        this.libelle = libelle;
    }
}
