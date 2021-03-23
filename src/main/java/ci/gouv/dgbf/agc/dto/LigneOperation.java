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
    private String uuid = UUID.randomUUID().toString();
    @Getter @Setter
    private String ligneDepenseUuid;
    @Getter @Setter
    private String usbCode;
    @Getter @Setter
    private String usbLibelle;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String activiteLibelle;
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
    private String sectionCode;
    @Getter @Setter
    private String sectionLibelle;
    @Getter @Setter
    private String bailleurId;
    @Getter @Setter
    private String bailleurLibelle;
    @Getter @Setter
    private OrigineImputation origineImputation;

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
                "ligneDepenseUuid='" + ligneDepenseUuid + '\'' +
                ", activiteCode='" + activiteCode + '\'' +
                ", activiteLibelle='" + activiteLibelle + '\'' +
                ", natureEconomiqueCode='" + natureEconomiqueCode + '\'' +
                ", natureEconomique='" + natureEconomiqueLibelle + '\'' +
                ", exercice='" + exercice + '\'' +
                ", budgetActuelAE=" + budgetActuelAE +
                ", budgetActuelCP=" + budgetActuelCP +
                ", montantOperationAE=" + montantOperationAE +
                ", montantOperationCP=" + montantOperationCP +
                ", effetOperation=" + effetOperation +
                ", disponibiliteCredit=" + disponibiliteCredit +
                '}';
    }
}
