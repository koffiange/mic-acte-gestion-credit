package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ImputationDto {

    @Getter @Setter
    private String uuid = UUID.randomUUID().toString();
    @Getter @Setter
    private String exercice = String.valueOf(LocalDate.now().getYear());
    @Getter @Setter
    private Section section;
    @Getter @Setter
    private ActiviteDeService activiteDeService;
    @Getter @Setter
    private NatureEconomique natureEcnomique;
    @Getter @Setter
    private SourceFinancement sourceFinancement;
    @Getter @Setter
    private Bailleur bailleur;

    public ImputationDto() {
    }

    @Override
    public String toString() {
        return "ImputationDto{" +
                "activiteDeService=" + activiteDeService +
                ", natureEcnomique=" + natureEcnomique +
                ", sourceFinancement=" + sourceFinancement +
                ", bailleur=" + bailleur +
                '}';
    }
}
