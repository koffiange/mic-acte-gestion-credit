package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.service.*;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

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
    OperationSessionService operationSessionService;

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
    private List<Operation> filteredOperationList;
    @Getter @Setter
    private List<Operation> selectedOperationList;
    @Getter @Setter
    private Section selectedSection;
    @Getter @Setter
    private Activite selectedActivite;
    @Getter @Setter
    private NatureEconomique selectedNatureEconomique;
    @Getter @Setter
    private String activiteCode;
    @Getter @Setter
    private String natureEconomiqueCode;
    @Getter @Setter
    private String natureDepense;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private String sourceFinancement;
    @Getter @Setter
    private String programme;
    @Getter @Setter
    private String action;
    @Getter @Setter
    private String bailleur;
    @Getter @Setter
    private TypeOperation typeImputation;
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
    @Getter @Setter
    private OperationBag operationBag;


    @PostConstruct
    public void init(){
        exercice = String.valueOf(LocalDate.now().getYear());
        operationBag = new OperationBag();
        params = getRequestParameterMap();

        if(params.containsKey("sectionCode")){
            sectionCode = params.get("sectionCode");
        }

        if(params.containsKey("natureTransaction")){
            natureTransaction = NatureTransaction.valueOf(params.get("natureTransaction"));
        }

        if(params.containsKey("typeImputation")){
            operationBag.setTypeOperation(TypeOperation.valueOf(params.get("typeImputation")));
        }
    }

    public String displayTitleText(){
        if (operationBag.getTypeOperation().equals(TypeOperation.ORIGINE)){
            return "Imputation d'Origine : rechercher des sources de financement.";
        } else {
            return "Imputation de Destinantion : rechercher de source de financement.";
        }
    }

    public boolean disableSectionField(){
        return natureTransaction.equals(NatureTransaction.VIREMENT);
    }

    public boolean filterBudget(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        final boolean b = ((Comparable) value).compareTo(BigDecimal.valueOf(Long.parseLong(filterText))) >= 0;
        return b;
    }

    public boolean filterDisponible(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        final boolean b = ((Comparable) value).compareTo(BigDecimal.valueOf(Long.parseLong(filterText))) >= 0;
        return b;
    }

    public void rechercher(){
        ligneDepenseList = ligneDepenseService.findByCritere(exercice, sourceFinancement, natureEconomiqueCode, activiteCode, bailleur, sectionCode, natureDepense, programme, action);
        operationList = operationService.buildOperationListFromLigneDepenseList(ligneDepenseList);
        this.initCritereRecherche();
    }

    private void initCritereRecherche(){
        activiteCode = "";
        natureEconomiqueCode = "";
    }

    public void ajouter(){
        LOG.info("Selected Opération : "+selectedOperationList.size());
        operationBag.getSectionCodeList().add(sectionCode);
        this.typeOperationSetter();
        operationBag.getOperationList().addAll(selectedOperationList);
        PrimeFaces.current().dialog().closeDynamic(operationBag);
    }

    private void typeOperationSetter(){
        selectedOperationList.forEach(operation -> {
            if (operationBag.getTypeOperation().equals(TypeOperation.ORIGINE)){
                operation.setTypeOperation(TypeOperation.ORIGINE);
            } else {
                operation.setTypeOperation(TypeOperation.DESTINATION);
            }
        });
    }
}
