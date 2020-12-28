package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

public class Signataire{
    @Getter @Setter
    private String fonction;
    @Getter @Setter
    private String nom;
    @Getter @Setter
    public Acte acte;

    public Signataire() {
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    @Override
    public String toString() {
        return "Signataire{" +
                "fonction='" + fonction + '\'' +
                ", nom='" + nom + '\'' +
                ", acte=" + acte +
                '}';
    }
}
