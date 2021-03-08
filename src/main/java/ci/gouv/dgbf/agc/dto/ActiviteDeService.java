package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class ActiviteDeService {
    @Getter @Setter
    public String adsUuid;
    @Getter @Setter
    public String adsCode;
    @Getter @Setter
    public String adsLibelle;
    @Getter @Setter
    public String usbCode;
    @Getter @Setter
    public String usbLibelle;
    @Getter @Setter
    public String sectionCode;
    @Getter @Setter
    public String sectionLibelle;

    public ActiviteDeService() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiviteDeService that = (ActiviteDeService) o;
        return Objects.equals(adsUuid, that.adsUuid) && Objects.equals(adsCode, that.adsCode) && Objects.equals(adsLibelle, that.adsLibelle) && Objects.equals(usbCode, that.usbCode) && Objects.equals(usbLibelle, that.usbLibelle) && Objects.equals(sectionCode, that.sectionCode) && Objects.equals(sectionLibelle, that.sectionLibelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adsUuid, adsCode, adsLibelle, usbCode, usbLibelle, sectionCode, sectionLibelle);
    }

    @Override
    public String toString() {
        return "ActiviteDeService{" +
                "adsUuid='" + adsUuid + '\'' +
                ", adsCode='" + adsCode + '\'' +
                ", adsLibelle='" + adsLibelle + '\'' +
                ", usbCode='" + usbCode + '\'' +
                ", usbLibelle='" + usbLibelle + '\'' +
                ", sectionCode='" + sectionCode + '\'' +
                ", sectionLibelle='" + sectionLibelle + '\'' +
                '}';
    }
}
