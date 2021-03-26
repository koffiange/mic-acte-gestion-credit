package ci.gouv.dgbf.agc.dto;

import ci.gouv.dgbf.agc.enumeration.StatutOperation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Operation {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private String codeOperation;
    @Getter @Setter
    private BigDecimal variationAE;
    @Getter @Setter
    private BigDecimal variationCP;
    @Getter @Setter
    private StatutOperation statutOperation;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private LocalDateTime createdDate;
    @Getter @Setter
    private LocalDateTime updatedDate;
    @Getter @Setter
    private String createdBy;
    @Getter @Setter
    private String updatedBy;

    public Operation() {
    }

    public Operation(String codeOperation, BigDecimal variationAE, BigDecimal variationCP, StatutOperation statutOperation, String exercice) {
        this.codeOperation = codeOperation;
        this.variationAE = variationAE;
        this.variationCP = variationCP;
        this.statutOperation = statutOperation;
        this.exercice = exercice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(codeOperation, operation.codeOperation) && Objects.equals(variationAE, operation.variationAE) && Objects.equals(variationCP, operation.variationCP) && statutOperation == operation.statutOperation && Objects.equals(exercice, operation.exercice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeOperation, variationAE, variationCP, statutOperation, exercice);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "uuid='" + uuid + '\'' +
                ", codeOperation='" + codeOperation + '\'' +
                ", variationAE=" + variationAE +
                ", variationCP=" + variationCP +
                ", statutOperation=" + statutOperation +
                ", exercice='" + exercice + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
