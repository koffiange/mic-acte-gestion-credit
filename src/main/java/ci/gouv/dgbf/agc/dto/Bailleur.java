package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Bailleur {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String code;
    @Getter @Setter
    private String designation;

    public Bailleur() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bailleur bailleur = (Bailleur) o;
        return Objects.equals(id, bailleur.id) && Objects.equals(code, bailleur.code) && Objects.equals(designation, bailleur.designation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, designation);
    }

    @Override
    public String toString() {
        return "Bailleur{" +
                "bailleurId='" + id + '\'' +
                ", bailleurCode='" + code + '\'' +
                ", bailleurDesignation='" + designation + '\'' +
                '}';
    }
}
