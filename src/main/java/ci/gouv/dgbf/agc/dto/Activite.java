package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Activite {
    @Getter @Setter
    private String code;
    @Getter @Setter
    private String libelleCourt;
    @Getter @Setter
    private String libelleLong;

    public Activite() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activite activite = (Activite) o;
        return Objects.equals(code, activite.code) &&
                Objects.equals(libelleCourt, activite.libelleCourt) &&
                Objects.equals(libelleLong, activite.libelleLong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, libelleCourt, libelleLong);
    }

    @Override
    public String toString() {
        return "Activite{" +
                "code='" + code + '\'' +
                ", libelleCourt='" + libelleCourt + '\'' +
                ", libelleLong='" + libelleLong + '\'' +
                '}';
    }
}
