package ci.gouv.dgbf.agc.dto;


import ci.gouv.dgbf.agc.enumeration.DisponibiliteCreditOperation;
import ci.gouv.dgbf.agc.enumeration.EffetOperation;
import ci.gouv.dgbf.agc.enumeration.OrigineImputation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class LigneOperation {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private String ligneDepenseUuid;
    @Getter @Setter
    private String financementId;
    @Getter @Setter
    private String plafonId;
    @Getter @Setter
    private String usbCode;
    @Getter @Setter
    private String usbLibelle;
    @Getter @Setter
    private String activiteId;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String activiteLibelle;
    @Getter @Setter
    private String actionId;
    @Getter @Setter
    private String actionCode;
    @Getter @Setter
    private String actionLibelle;
    @Getter @Setter
    private String natureEconomiqueId;
    @Getter @Setter
    private String natureEconomiqueCode;
    @Getter @Setter
    private String natureEconomiqueLibelle;
    @Getter @Setter
    private String sourceFinancementId;
    @Getter @Setter
    private String sourceFinancementCode;
    @Getter @Setter
    private String sourceFinancementLibelle;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private BigDecimal budgetActuelAE;
    @Getter @Setter
    private BigDecimal budgetActuelCP;
    @Getter @Setter
    private BigDecimal montantOperationAE = BigDecimal.ZERO;
    @Getter @Setter
    private BigDecimal montantOperationCP = BigDecimal.ZERO;
    @Getter @Setter
    private BigDecimal montantDisponibleAE;
    @Getter @Setter
    private BigDecimal montantDisponibleCP;
    @Getter @Setter
    private BigDecimal disponibleRestantAE;
    @Getter @Setter
    private BigDecimal disponibleRestantCP;
    @Getter @Setter
    private TypeOperation typeOperation;
    @Getter @Setter
    private EffetOperation effetOperation;
    @Getter @Setter
    private DisponibiliteCreditOperation disponibiliteCredit;
    @Getter @Setter
    private String sectionId;
    @Getter @Setter
    private String sectionCode;
    @Getter @Setter
    private String sectionLibelle;
    @Getter @Setter
    private String bailleurId;
    @Getter @Setter
    private String bailleurLibelle;
    @Getter @Setter
    private OrigineImputation origineImputation;
    @Getter @Setter
    private String natureDepense;

    public LigneOperation() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneOperation ligneOperation = (LigneOperation) o;
        return Objects.equals(ligneDepenseUuid, ligneOperation.ligneDepenseUuid) &&
                Objects.equals(activiteCode, ligneOperation.activiteCode) &&
                Objects.equals(activiteLibelle, ligneOperation.activiteLibelle) &&
                Objects.equals(natureEconomiqueCode, ligneOperation.natureEconomiqueCode) &&
                Objects.equals(natureEconomiqueLibelle, ligneOperation.natureEconomiqueLibelle) &&
                Objects.equals(exercice, ligneOperation.exercice) &&
                Objects.equals(budgetActuelAE, ligneOperation.budgetActuelAE) &&
                Objects.equals(budgetActuelCP, ligneOperation.budgetActuelCP) &&
                Objects.equals(montantOperationAE, ligneOperation.montantOperationAE) &&
                Objects.equals(montantOperationCP, ligneOperation.montantOperationCP) &&
                effetOperation == ligneOperation.effetOperation &&
                disponibiliteCredit == ligneOperation.disponibiliteCredit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ligneDepenseUuid, activiteCode, activiteLibelle, natureEconomiqueCode, natureEconomiqueLibelle, exercice, budgetActuelAE, budgetActuelCP, montantOperationAE, montantOperationCP, effetOperation, disponibiliteCredit);
    }

    @Override
    public String toString() {
        return "LigneOperation{" +
                "uuid='" + uuid + '\'' +
                ", ligneDepenseUuid='" + ligneDepenseUuid + '\'' +
                ", usbCode='" + usbCode + '\'' +
                ", usbLibelle='" + usbLibelle + '\'' +
                ", activiteCode='" + activiteCode + '\'' +
                ", activiteLibelle='" + activiteLibelle + '\'' +
                ", natureEconomiqueCode='" + natureEconomiqueCode + '\'' +
                ", natureEconomiqueLibelle='" + natureEconomiqueLibelle + '\'' +
                ", sourceFinancementId='" + sourceFinancementId + '\'' +
                ", sourceFinancementCode='" + sourceFinancementCode + '\'' +
                ", sourceFinancementLibelle='" + sourceFinancementLibelle + '\'' +
                ", exercice='" + exercice + '\'' +
                ", budgetActuelAE=" + budgetActuelAE +
                ", budgetActuelCP=" + budgetActuelCP +
                ", montantOperationAE=" + montantOperationAE +
                ", montantOperationCP=" + montantOperationCP +
                ", montantDisponibleAE=" + montantDisponibleAE +
                ", montantDisponibleCP=" + montantDisponibleCP +
                ", disponibleRestantAE=" + disponibleRestantAE +
                ", disponibleRestantCP=" + disponibleRestantCP +
                ", typeOperation=" + typeOperation +
                ", effetOperation=" + effetOperation +
                ", disponibiliteCredit=" + disponibiliteCredit +
                ", sectionCode='" + sectionCode + '\'' +
                ", sectionLibelle='" + sectionLibelle + '\'' +
                ", bailleurId='" + bailleurId + '\'' +
                ", bailleurLibelle='" + bailleurLibelle + '\'' +
                ", origineImputation=" + origineImputation +
                '}';
    }
}
