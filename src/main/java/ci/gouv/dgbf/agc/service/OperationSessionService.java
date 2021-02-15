package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
public class OperationSessionService implements Serializable {

    @Getter @Setter
    private List<Operation> operationOrigineList = new ArrayList<>();
    @Getter @Setter
    private List<Operation> operationDestinationList = new ArrayList<>();

    public void reset(){
        operationOrigineList = new ArrayList<>();
        operationDestinationList = new ArrayList<>();
    }
}
