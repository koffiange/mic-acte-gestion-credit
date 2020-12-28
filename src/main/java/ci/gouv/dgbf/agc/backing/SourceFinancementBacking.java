package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Activite;
import ci.gouv.dgbf.agc.dto.LigneDepense;
import ci.gouv.dgbf.agc.dto.NatureEconomique;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.service.*;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named(value = "srcFinancementBacking")
@ViewScoped
public class SourceFinancementBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActiviteService activiteService;
    @Inject
    NatureEconomiqueService natureEconomiqueService;
    @Inject
    LigneDepenseService ligneDepenseService;
    @Inject
    OperationService operationService;

    /*
    @Getter @Setter
    private List<Activite> activiteList;
    @Getter @Setter
    private List<NatureEconomique> natureEconomiqueList;
     */

    @Getter @Setter
    private List<LigneDepense> ligneDepenseList;
    @Getter @Setter
    private List<Operation> operationList;
    @Getter @Setter
    private Activite selectedActivite;
    @Getter @Setter
    private NatureEconomique selectedNatureEconomique;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String natureEconomiqueCode;

    @PostConstruct
    public void init(){
        // activiteList = activiteService.findAll();
        // natureEconomiqueList = natureEconomiqueService.findAll();
    }

    public void rechercher(){
        ligneDepenseList = ligneDepenseService.findByCritere(natureEconomiqueCode, activiteCode, null);
        operationList = operationService.buildOperationListFromLigneDepenseList(ligneDepenseList);
        selectedActivite = activiteService.findByCode(activiteCode);
        selectedNatureEconomique = natureEconomiqueService.findByCode(natureEconomiqueCode);
        activiteCode = "";
        natureEconomiqueCode = "";
    }

}
