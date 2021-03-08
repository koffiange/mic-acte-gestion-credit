package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActeDto {
    @Getter @Setter
    private Acte acte;
    @Getter @Setter
    private List<Signataire> signataireList;
    @Getter @Setter
    private List<Operation> operationList;
    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();

    public ActeDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActeDto acteDto = (ActeDto) o;
        return Objects.equals(acte, acteDto.acte) &&
                Objects.equals(signataireList, acteDto.signataireList) &&
                Objects.equals(operationList, acteDto.operationList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acte, signataireList, operationList);
    }

    @Override
    public String toString() {
        return "ActeDto{" +
                "acte=" + acte +
                ", signataireList=" + signataireList +
                ", operationList=" + operationList +
                ", imputationDtoList=" + imputationDtoList +
                '}';
    }
}
