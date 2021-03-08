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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ImputationCreateBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    SectionService sectionService;
    @Inject
    ActiviteDeServiceService activiteService;
    @Inject
    BailleurService bailleurService;
    @Inject
    NatureEconomiqueService natureEconomiqueService;
    @Inject
    SourceFinancementService sourceFinancementService;
    @Inject
    OperationSessionService operationSessionService;
    @Inject
    OperationService operationService;

    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();
    @Getter @Setter
    private List<Section> sectionList;
    @Getter @Setter
    private List<ActiviteDeService> activiteDeServiceList = new ArrayList<>();
    @Getter @Setter
    private List<Bailleur> bailleurList;
    @Getter @Setter
    private List<NatureEconomique> natureEconomiqueList;
    @Getter @Setter
    private List<SourceFinancement> sourceFinancementList;
    @Getter @Setter
    private ImputationDto imputationDto;
    @Getter @Setter
    private Section selectedSection;
    @Getter @Setter
    private Map<String, String> params;
    @Getter @Setter
    private OperationBag operationBag;
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private NatureTransaction natureTransaction;

    @PostConstruct
    public void init(){
        imputationDto = new ImputationDto();
        exercice = String.valueOf(LocalDate.now().getYear());
        sectionList = sectionService.list();
        operationBag = new OperationBag();
        operationBag.setTypeOperation(TypeOperation.DESTINATION);
        params = getRequestParameterMap();

        if(params.containsKey("typeImputation")){
            operationBag.setTypeOperation(TypeOperation.valueOf(params.get("typeImputation")));
        }

        if(params.containsKey("natureTransaction")){
            natureTransaction = NatureTransaction.valueOf(params.get("natureTransaction"));
        }

        if(natureTransaction.equals(NatureTransaction.VIREMENT) && params.containsKey("sectionCode") && !sectionList.isEmpty()){
            sectionList.stream().filter(section -> section.getCode().equals(params.get("sectionCode"))).findFirst().ifPresent(this::setSelectedSection);
            imputationDto.setSection(selectedSection);
            activiteDeServiceList = activiteService.findBySectionCode(imputationDto.getSection().getCode());
        }

        bailleurList = bailleurService.listAll();
        natureEconomiqueList = natureEconomiqueService.findAll();
        sourceFinancementList = sourceFinancementService.listAll();
    }

    public boolean disableSectionField(){
        return natureTransaction.equals(NatureTransaction.VIREMENT);
    }

    public void onSectionSelected(){
        if(imputationDto.getSection()!=null)
            activiteDeServiceList = activiteService.findBySectionCode(imputationDto.getSection().getCode());
    }

    public void creer(){
        imputationDtoList.add(imputationDto);
        imputationDto = new ImputationDto();
        imputationDto.setSection(selectedSection);
    }

    public void supprimer(ImputationDto imputationDto){
        imputationDtoList.remove(imputationDto);
    }

    public void ajouter(){
        operationBag.setImputationDtoList(imputationDtoList);
        operationBag.setOperationList(operationService.buildOperationListFromImputationList(imputationDtoList));
        PrimeFaces.current().dialog().closeDynamic(operationBag);
    }
}
