package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.ActeDto;
import ci.gouv.dgbf.agc.dto.Composition;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.CompositionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.plaf.ButtonUI;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "cmcBacking")
@ViewScoped
public class ConsultationMouvementCreditBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActeService acteService;
    @Inject
    CompositionService compositionService;

    @Getter @Setter
    private ActeDto acteDto;
    @Getter @Setter
    private List<Composition> compositionList;

    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            acteDto = acteService.findActeDtoById(params.get("uuid"));
            if (acteDto.getActe().getModeleVisa() != null) {
                compositionList = compositionService.findByModele(acteDto.getActe().getModeleVisa().getUuid());
            }
        }
    }

    public List<Operation> findByTypeOperation(String s){
        TypeOperation typeOperation = TypeOperation.valueOf(s);
        return acteDto.getOperationList().stream()
                .filter(operation -> operation.getTypeOperation().equals(typeOperation))
                .collect(Collectors.toList());
    }

    public String goBack(){
        return "/protected/user/pages/acte/index.xhtml";
    }
}
