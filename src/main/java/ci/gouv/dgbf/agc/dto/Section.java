package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Section {
    @Getter @Setter
    String uuid;
    @Getter @Setter
    String code;
    @Getter @Setter
    String sigle;
    @Getter @Setter
    String libelle;

    public Section() {
    }

    public Section(String uuid, String code, String sigle, String libelle) {
        this.uuid = uuid;
        this.code = code;
        this.sigle = sigle;
        this.libelle = libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section that = (Section) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(sigle, that.sigle) &&
                Objects.equals(libelle, that.libelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, sigle, libelle);
    }

    @Override
    public String toString() {
        return "Section{" +
                "uuid='" + uuid + '\'' +
                ", code='" + sigle + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
