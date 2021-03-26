package ci.gouv.dgbf.agc.bag;

import ci.gouv.dgbf.agc.dto.ImputationDto;
import ci.gouv.dgbf.agc.dto.LigneOperation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ImputationHandleReturnBag {
    @Getter @Setter
    private TypeOperation typeOperation;

    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();

    @Getter @Setter
    private List<LigneOperation> ligneOperationList = new ArrayList<>();
}
