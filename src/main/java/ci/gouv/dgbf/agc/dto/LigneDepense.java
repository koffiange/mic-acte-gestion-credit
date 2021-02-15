package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class LigneDepense {
    @Getter @Setter
    private String ligneDepenseId;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private String activiteId;
    @Getter @Setter
    private String natureEconomiqueId;
    @Getter @Setter
    private String natureEconomiqueCode;
    @Getter @Setter
    private String natureEconomiqueLibelle;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String activiteLibelle;
    @Getter @Setter
    private String sourceFinancementId;
    @Getter @Setter
    private String sourceFinancementCode;
    @Getter @Setter
    private String sourceFinancementLibelle;
    @Getter @Setter
    private String usbId;
    @Getter @Setter
    private String sectionId;
    @Getter @Setter
    private String sectionLibelle;
    @Getter @Setter
    private String sectionCode;
    @Getter @Setter
    private String uaId;
    @Getter @Setter
    private String financementId;
    @Getter @Setter
    private BigDecimal montantAe;
    @Getter @Setter
    private BigDecimal montantCp;
    @Getter @Setter
    private BigDecimal montantDisponibleAE;
    @Getter @Setter
    private BigDecimal montantDisponibleCP;


    public LigneDepense() {
    }

    public LigneDepense(String ligneDepenseId, String exercice, String activiteId,
                        String natureEconomiqueId, String natureEconomiqueCode,
                        String natureEconomiqueLibelle, String activiteCode,
                        String activiteLibelle, String sourceFinancementId, String usbId, String sectionId,
                        String sectionLibelle, String sectionCode, String uaId,
                        String secbId, String financementId, BigDecimal montantAe,
                        BigDecimal montantCp) {
        this.ligneDepenseId = ligneDepenseId;
        this.exercice = exercice;
        this.activiteId = activiteId;
        this.natureEconomiqueId = natureEconomiqueId;
        this.natureEconomiqueCode = natureEconomiqueCode;
        this.natureEconomiqueLibelle = natureEconomiqueLibelle;
        this.activiteCode = activiteCode;
        this.activiteLibelle = activiteLibelle;
        this.sourceFinancementId = sourceFinancementId;
        this.usbId = usbId;
        this.sectionId = sectionId;
        this.sectionLibelle = sectionLibelle;
        this.sectionCode = sectionCode;
        this.uaId = uaId;
        this.sectionId = secbId;
        this.financementId = financementId;
        this.montantAe = montantAe;
        this.montantCp = montantCp;
    }

}
