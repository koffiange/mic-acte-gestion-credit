package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.dto.OperationBag;
import ci.gouv.dgbf.agc.enumeration.ActeRole;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.LigneOperationService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ActeApplicationBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActeService acteService;

    @Inject
    LigneOperationService ligneOperationService;

    @Getter @Setter
    private List<Acte> acteList;

    @Getter @Setter
    private Operation operation;

    private Map<String, String> params;

    @Getter @Setter
    private Acte selectedActe;

    @Getter @Setter
    private OperationBag operationBag;

    @PostConstruct
    public void init(){
        operationBag = new OperationBag();
        params = getRequestParameterMap();
        if (params.containsKey("uuid")) {
            operation = ligneOperationService.findById(params.get("uuid"));
            acteList = acteService.findActeByOperation(params.get("uuid"));
            acteList.stream().filter(acte -> acte.getActeParDefaut().equals(ActeRole.PAR_DEFAUT)).findFirst().ifPresent(this::setSelectedActe);
        }
    }

    public void appliquer(){
        operationBag.setOperation(operation);
        operationBag.setActe(selectedActe);
        ligneOperationService.appliquer(operationBag);
        closeSuccess();
    }

    public void close(){
        closeSuccess();
    }

}
