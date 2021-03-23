package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.dto.ImputationDto;
import ci.gouv.dgbf.agc.dto.LigneOperation;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
public class OperationSessionService implements Serializable {

    @Getter @Setter
    private List<LigneOperation> ligneOperationOrigineList = new ArrayList<>();
    @Getter @Setter
    private List<LigneOperation> ligneOperationDestinationList = new ArrayList<>();
    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();

    public void reset(){
        ligneOperationOrigineList = new ArrayList<>();
        ligneOperationDestinationList = new ArrayList<>();
        imputationDtoList = new ArrayList<>();
    }
}
