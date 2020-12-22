package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

public class Composition {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private Visa visa;
    @Getter @Setter
    private ModeleVisa modeleVisa;

    public Composition() {
    }

    public Composition(Visa visa, ModeleVisa modeleVisa) {
        this.visa = visa;
        this.modeleVisa = modeleVisa;
    }

    @Override
    public String toString() {
        return "Composition{" +
                "uuid='" + uuid + '\'' +
                ", visa=" + visa +
                ", modeleVisa=" + modeleVisa +
                '}';
    }
}
