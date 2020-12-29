package ci.gouv.dgbf.agc.dto;


import ci.gouv.dgbf.agc.enumeration.DisponibiliteCreditOperation;
import ci.gouv.dgbf.agc.enumeration.EffetOperation;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;

public class Operation{
    @Getter @Setter
    private String ligneDepenseUuid;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String activiteLibelle;
    @Getter @Setter
    private String natureEconomiqueCode;
    @Getter @Setter
    private String natureEconomiqueId;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private String sourceFinancement;
    @Getter @Setter
    private BigDecimal budgetActuelAE;
    @Getter @Setter
    private BigDecimal budgetActuelCP;
    @Getter @Setter
    private BigDecimal montantOperationAE;
    @Getter @Setter
    private BigDecimal montantOperationCP;
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
                Objects.equals(natureEconomiqueId, operation.natureEconomiqueId) &&
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
        return Objects.hash(ligneDepenseUuid, activiteCode, activiteLibelle, natureEconomiqueCode, natureEconomiqueId, exercice, budgetActuelAE, budgetActuelCP, montantOperationAE, montantOperationCP, effetOperation, disponibiliteCredit);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "ligneDepenseUuid='" + ligneDepenseUuid + '\'' +
                ", activiteCode='" + activiteCode + '\'' +
                ", activiteLibelle='" + activiteLibelle + '\'' +
                ", natureEconomiqueCode='" + natureEconomiqueCode + '\'' +
                ", natureEconomique='" + natureEconomiqueId + '\'' +
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
