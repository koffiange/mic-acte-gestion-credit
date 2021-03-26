package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.bag.LigneDepenseHandleReturnBag;
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
import java.util.*;
import java.util.logging.Logger;

@Named(value = "rchSourceFinancementBacking")
@ViewScoped
public class RechercherSourceFinancementBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    SectionService sectionService;
    @Inject
    ActiviteDeServiceService activiteService;
    @Inject
    NatureEconomiqueService natureEconomiqueService;
    @Inject
    LigneDepenseService ligneDepenseService;
    @Inject
    OperationService operationService;
    @Inject
    OperationSessionService operationSessionService;
    @Getter @Setter
    private List<Section> sectionList;
    @Getter @Setter
    private List<NatureEconomique> natureEconomiqueList = new ArrayList<>();
    /*
    @Getter @Setter
    private List<ActiviteDeService> activiteList;
     */


    @Getter @Setter
    private List<LigneDepense> ligneDepenseList;
    @Getter @Setter
    private List<LigneOperation> ligneOperationList;
    @Getter @Setter
    private List<LigneOperation> filteredLigneOperationList;
    @Getter @Setter
    private List<LigneOperation> selectedLigneOperationList;
    @Getter @Setter
    private Section selectedSection;
    @Getter @Setter
    private ActiviteDeService selectedActiviteDeService = new ActiviteDeService();
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
    LigneDepenseHandleReturnBag ligneDepenseHandleReturnBag;


    @PostConstruct
    public void init(){
        exercice = String.valueOf(LocalDate.now().getYear());
        ligneDepenseHandleReturnBag = new LigneDepenseHandleReturnBag();
        // operationBag = new OperationBag();
        params = getRequestParameterMap();
        sectionList = sectionService.list();
        natureEconomiqueList = natureEconomiqueService.findAll();
        selectedNatureEconomique = new NatureEconomique("", "", "");

        if(params.containsKey("sectionCode") && !sectionList.isEmpty()){
            sectionList.stream().filter(section -> section.getCode().equals(params.get("sectionCode"))).findFirst().ifPresent(this::setSelectedSection);
            sectionCode = params.get("sectionCode");
        } else {
            selectedSection = new Section("", "", "", "");
        }

        if(params.containsKey("natureTransaction")){
            natureTransaction = NatureTransaction.valueOf(params.get("natureTransaction"));
        }

        if(params.containsKey("typeImputation")){
            ligneDepenseHandleReturnBag.setTypeOperation(TypeOperation.valueOf(params.get("typeImputation")));
        }
    }

    public String displayTitleText(){
        /*
        if (operationBag.getTypeOperation().equals(TypeOperation.ORIGINE)){
            return "ImputationDto d'Origine : rechercher des sources de financement.";
        } else {
            return "ImputationDto de Destinantion : rechercher de source de financement.";
        }
         */
        return null;
    }

    public boolean disableSectionField(){
        // return natureTransaction.equals(NatureTransaction.VIREMENT) && operationBag.getTypeOperation().equals(TypeOperation.DESTINATION);
        return true;
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
        ligneOperationList = operationService.buildLigneOperationListFromLigneDepenseList(ligneDepenseList, ligneDepenseHandleReturnBag.getTypeOperation());
        this.initCritereRecherche();
    }

    private void initCritereRecherche(){
        activiteCode = null;
        natureEconomiqueCode = null;
        selectedNatureEconomique = new NatureEconomique("", "", "");
    }

    public void ajouter(){
        this.typeOperationSetter();
        ligneDepenseHandleReturnBag.getLigneOperationList().addAll(selectedLigneOperationList);
        PrimeFaces.current().dialog().closeDynamic(ligneDepenseHandleReturnBag);
    }

    private void typeOperationSetter(){
        /*
        selectedLigneOperationList.forEach(operation -> {
            if (operationBag.getTypeOperation().equals(TypeOperation.ORIGINE)){
                operation.setTypeOperation(TypeOperation.ORIGINE);
            } else {
                operation.setTypeOperation(TypeOperation.DESTINATION);
            }
        });

         */
    }

    public void onSectionSelect(){
        /*
        natureEconomiqueList.stream()
                .filter(natureEconomique -> natureEconomique.getCode().equals(natureEconomiqueCode))
                .findFirst().ifPresent(this::setSelectedNatureEconomique);
         */
        sectionCode = selectedSection.getCode();
    }

    public void onNatureEconomiqueSelect(){
        /*
        natureEconomiqueList.stream()
                .filter(natureEconomique -> natureEconomique.getCode().equals(natureEconomiqueCode))
                .findFirst().ifPresent(this::setSelectedNatureEconomique);
         */
        natureEconomiqueCode = selectedNatureEconomique.getCode();
    }
}
