package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Activite;
import ci.gouv.dgbf.agc.dto.LigneDepense;
import ci.gouv.dgbf.agc.dto.NatureEconomique;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
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
    @Getter @Setter
    private String typeImputation;
    @Getter @Setter
    private String sectionCode;
    @Getter @Setter
    private NatureTransaction natureTransaction;
    @Getter @Setter
    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if(params.containsKey("sectionCode")){
            sectionCode = params.get("sectionCode");
        }

        if(params.containsKey("natureTransaction")){
            natureTransaction = NatureTransaction.valueOf(params.get("natureTransaction"));
        }

        if(params.containsKey("typeImputation")){
            typeImputation = params.get("typeImputation");
        }

        LOG.info("Section code : "+sectionCode);
        LOG.info("Nature Transaction : "+natureTransaction);
        LOG.info("Type Imputation : "+typeImputation);

        // activiteList = activiteService.findAll();
        // natureEconomiqueList = natureEconomiqueService.findAll();
    }

    public String displayTitleText(){
        if (typeImputation.equals("origine")){
            return "Imputation d'Origine : rechercher des sources de financement de la section "+sectionCode;
        } else {
            return "Imputation de Destinantion : rechercher de source de financement";
        }
    }

    public void rechercher(){
        ligneDepenseList = ligneDepenseService.findByCritere(natureEconomiqueCode, activiteCode, sectionCode);
        operationList = operationService.buildOperationListFromLigneDepenseList(ligneDepenseList);
        selectedActivite = activiteService.findByCode(activiteCode);
        selectedNatureEconomique = natureEconomiqueService.findByCode(natureEconomiqueCode);
        activiteCode = "";
        natureEconomiqueCode = "";
    }

}
