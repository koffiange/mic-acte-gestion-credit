package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class SourceFinancement {
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String code;
    @Getter @Setter
    private String libelle;

    public SourceFinancement() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SourceFinancement that = (SourceFinancement) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(libelle, that.libelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, libelle);
    }

    @Override
    public String toString() {
        return "SourceFinancement{" +
                "sourceFinancementId='" + id + '\'' +
                ", sourceFinancementCode='" + code + '\'' +
                ", sourceFinancementLibelle='" + libelle + '\'' +
                '}';
    }
}
