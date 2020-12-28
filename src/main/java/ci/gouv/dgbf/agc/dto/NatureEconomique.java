package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class NatureEconomique {
    @Getter @Setter
    private String code;
    @Getter @Setter
    private String libelleLong;
    @Getter @Setter
    private String sensComptable;

    public NatureEconomique() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NatureEconomique that = (NatureEconomique) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(libelleLong, that.libelleLong) &&
                Objects.equals(sensComptable, that.sensComptable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, libelleLong, sensComptable);
    }

    @Override
    public String toString() {
        return "NatureEconomique{" +
                "code='" + code + '\'' +
                ", libelleLong='" + libelleLong + '\'' +
                ", sensComptable='" + sensComptable + '\'' +
                '}';
    }
}
