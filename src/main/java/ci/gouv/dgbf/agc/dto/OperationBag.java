package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class OperationBag {
    @Getter @Setter
    private Acte acte;
    @Getter @Setter
    private List<Signataire> signataireList;
    @Getter @Setter
    private Operation operation;
    @Getter @Setter
    private List<LigneOperation> ligneOperationList = new ArrayList<>();
    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();
}
