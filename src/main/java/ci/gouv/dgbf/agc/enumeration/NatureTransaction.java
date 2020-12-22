package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum NatureTransaction {
    VIREMENT("Virement"),
    TRANSFERT("Transfert");

    @Getter @Setter
    private String value;

    NatureTransaction(String value) {
        this.value = value;
    }
}
