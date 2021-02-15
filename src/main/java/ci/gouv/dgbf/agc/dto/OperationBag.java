package ci.gouv.dgbf.agc.dto;

import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperationBag {
    @Getter @Setter
    private TypeOperation typeOperation;
    @Getter @Setter
    private List<String> sectionCodeList = new ArrayList<>();
    @Getter @Setter
    private List<Operation> operationList = new ArrayList<>();

    public OperationBag() {
    }

    public OperationBag(TypeOperation typeOperation, List<Operation> operationList) {
        this.typeOperation = typeOperation;
        this.operationList = operationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationBag that = (OperationBag) o;
        return typeOperation == that.typeOperation &&
                Objects.equals(sectionCodeList, that.sectionCodeList) &&
                Objects.equals(operationList, that.operationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOperation, sectionCodeList, operationList);
    }

    @Override
    public String toString() {
        return "OperationBag{" +
                "typeOperation=" + typeOperation +
                ", sectionCodeList=" + sectionCodeList +
                ", operationList=" + operationList +
                '}';
    }
}
