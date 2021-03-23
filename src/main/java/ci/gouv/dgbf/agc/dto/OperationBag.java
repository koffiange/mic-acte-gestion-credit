package ci.gouv.dgbf.agc.dto;

import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperationBag {

    @Getter @Setter
    private Acte acte;
    @Getter @Setter
    private List<Signataire> signataireList;
    @Getter @Setter
    private Operation operation;
    @Getter @Setter
    private List<LigneOperation> ligneOperationList;
    @Getter @Setter
    private List<ImputationDto> imputationDtoList;

}
