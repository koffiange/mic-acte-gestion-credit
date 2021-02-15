package ci.gouv.dgbf.agc.dto;


import ci.gouv.dgbf.agc.enumeration.DisponibiliteCreditOperation;
import ci.gouv.dgbf.agc.enumeration.EffetOperation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Operation extends BaseDto{
    @Getter @Setter
    private String uuid = UUID.randomUUID().toString();
    @Getter @Setter
    private String ligneDepenseUuid;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String activiteLibelle;
    @Getter @Setter
    private String natureEconomiqueCode;
    @Getter @Setter
    private String natureEconomiqueLibelle;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private String sourceFinancementId;
    @Getter @Setter
    private String sourceFinancementCode;
    @Getter @Setter
    private String sourceFinancementLibelle;
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
    private TypeOperation typeOperation;
    @Getter @Setter
    private EffetOperation effetOperation;
    @Getter @Setter
    private DisponibiliteCreditOperation disponibiliteCredit;

    public Operation() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(ligneDepenseUuid, operation.ligneDepenseUuid) &&
                Objects.equals(activiteCode, operation.activiteCode) &&
                Objects.equals(activiteLibelle, operation.activiteLibelle) &&
                Objects.equals(natureEconomiqueCode, operation.natureEconomiqueCode) &&
                Objects.equals(natureEconomiqueLibelle, operation.natureEconomiqueLibelle) &&
                Objects.equals(exercice, operation.exercice) &&
                Objects.equals(budgetActuelAE, operation.budgetActuelAE) &&
                Objects.equals(budgetActuelCP, operation.budgetActuelCP) &&
                Objects.equals(montantOperationAE, operation.montantOperationAE) &&
                Objects.equals(montantOperationCP, operation.montantOperationCP) &&
                effetOperation == operation.effetOperation &&
                disponibiliteCredit == operation.disponibiliteCredit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ligneDepenseUuid, activiteCode, activiteLibelle, natureEconomiqueCode, natureEconomiqueLibelle, exercice, budgetActuelAE, budgetActuelCP, montantOperationAE, montantOperationCP, effetOperation, disponibiliteCredit);
    }

    @Override
    public String toString() {
        return "Operation{" +
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
