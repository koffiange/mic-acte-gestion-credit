package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Activite;
import ci.gouv.dgbf.agc.dto.LigneDepense;
import ci.gouv.dgbf.agc.dto.NatureEconomique;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.service.*;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "rchSourceFinancementBacking")
@ViewScoped
public class RechercherSourceFinancementBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActiviteService activiteService;
    @Inject
    NatureEconomiqueService natureEconomiqueService;
    @Inject
    LigneDepenseService ligneDepenseService;
    @Inject
    OperationService operationService;
    @Inject
    operationSessionService operationSessionService;

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
    @Getter @Setter
    private BigDecimal cumuleRettranchementAE = BigDecimal.ZERO;
    @Getter @Setter
    private BigDecimal cumuleRettranchementCP = BigDecimal.ZERO;


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

        if(!typeImputation.equals("origine")) {
            operationSessionService.getOperationOrigineList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumuleRettranchementAE);
            operationSessionService.getOperationOrigineList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumuleRettranchementCP);
        }
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

    public boolean displaySectionField(){
        return (natureTransaction.equals(NatureTransaction.VIREMENT));
    }

    public void rechercher(){
        ligneDepenseList = ligneDepenseService.findByCritere(natureEconomiqueCode, activiteCode, sectionCode);
        operationList = operationService.buildOperationListFromLigneDepenseList(ligneDepenseList);
        selectedActivite = activiteService.findByCode(activiteCode);
        selectedNatureEconomique = natureEconomiqueService.findByCode(natureEconomiqueCode);
        activiteCode = "";
        natureEconomiqueCode = "";
    }

    Function<Operation, Operation> typeOperationSetter = operation -> {
        if (typeImputation.equals("origine")){
            operation.setTypeOperation(TypeOperation.ORIGINE);
        } else {
            operation.setTypeOperation(TypeOperation.DESTINATION);
        }
        return operation;
    };

    public void ajouter(){
        List<Operation> effeciveOperationList = operationList.stream()
                .filter(operation -> operation.getMontantOperationAE() != null && operation.getMontantOperationCP() != null)
                .map(typeOperationSetter)
                //.filter(operation -> operation.getMontantOperationAE().compareTo(BigDecimal.ZERO) > 0)
                //.filter(operation -> operation.getMontantOperationCP().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
        LOG.info("Operation "+typeImputation+" : "+effeciveOperationList.size());
        if (typeImputation.equals("origine")){
            operationSessionService.getOperationOrigineList().addAll(effeciveOperationList);
        } else{
            operationSessionService.getOperationDestinationList().addAll(effeciveOperationList);
        }
        closeSuccess();
    }
}
