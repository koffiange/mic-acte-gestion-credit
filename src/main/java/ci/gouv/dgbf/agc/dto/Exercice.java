package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;


public class Exercice {
    @Getter @Setter
    private String annee;

    public Exercice() {
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "annee='" + annee + '\'' +
                '}';
    }
}
