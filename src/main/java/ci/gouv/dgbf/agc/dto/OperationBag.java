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
    private List<Operation> operationList = new ArrayList<>();
    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();

    public OperationBag() {
    }

    public OperationBag(TypeOperation typeOperation, List<Operation> operationList, List<ImputationDto> imputationDtoList) {
        this.typeOperation = typeOperation;
        this.operationList = operationList;
        this.imputationDtoList = imputationDtoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationBag that = (OperationBag) o;
        return typeOperation == that.typeOperation && Objects.equals(operationList, that.operationList) && Objects.equals(imputationDtoList, that.imputationDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOperation, operationList, imputationDtoList);
    }

    @Override
    public String toString() {
        return "OperationBag{" +
                "typeOperation=" + typeOperation +
                ", operationList=" + operationList +
                ", imputationDtoList=" + imputationDtoList +
                '}';
    }
}
