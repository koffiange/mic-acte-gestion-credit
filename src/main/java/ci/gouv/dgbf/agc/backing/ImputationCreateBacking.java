package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.bag.ImputationHandleReturnBag;
import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.service.*;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private ImputationHandleReturnBag imputationHandleReturnBag = new ImputationHandleReturnBag();
    @Getter @Setter
    private String exercice;
    @Getter @Setter
    private NatureTransaction natureTransaction;

    @PostConstruct
    public void init(){
        bailleurList = bailleurService.listAll();
        natureEconomiqueList = natureEconomiqueService.findAll();
        sourceFinancementList = sourceFinancementService.listAll();
        sectionList = sectionService.list();

        this.initImputationDto();
        exercice = String.valueOf(LocalDate.now().getYear());

        imputationHandleReturnBag = new ImputationHandleReturnBag();
        imputationHandleReturnBag.setTypeOperation(TypeOperation.DESTINATION);
        params = getRequestParameterMap();

        if(params.containsKey("natureTransaction")){
            natureTransaction = NatureTransaction.valueOf(params.get("natureTransaction"));
        }

        if(natureTransaction.equals(NatureTransaction.VIREMENT) && params.containsKey("sectionCode") && !sectionList.isEmpty()){
            sectionList.stream().filter(section -> section.getCode().equals(params.get("sectionCode"))).findFirst().ifPresent(this::setSelectedSection);
            imputationDto.setSection(selectedSection);
            activiteDeServiceList = activiteService.findBySectionCode(imputationDto.getSection().getCode());
        }


    }

    private void initImputationDto(){
        imputationDto = new ImputationDto();
        this.initBailleur();
        this.initSourceFinancement();
    }

    private void initBailleur(){
        if (!bailleurList.isEmpty())
            bailleurList.stream().filter(bailleur -> bailleur.getCode().equals("ET")).findFirst().ifPresent(bailleur -> imputationDto.setBailleur(bailleur));
    }

    private void initSourceFinancement(){
        if (!sourceFinancementList.isEmpty())
            sourceFinancementList.stream().filter(sourceFinancement -> sourceFinancement.getCode().equals("1"))
                                 .findFirst().ifPresent(sourceFinancement -> imputationDto.setSourceFinancement(sourceFinancement));
        List<SourceFinancement> toRemoveList = sourceFinancementList.stream().filter(sourceFinancement -> !sourceFinancement.getCode().equals("1")).collect(Collectors.toList());
        sourceFinancementList.removeAll(toRemoveList);
        LOG.info("sourceFinancementList size : "+sourceFinancementList.size());
    }

    public boolean disableSourceFinancementItem(){
        return imputationDto.getBailleur().getCode().equals("ET");
    }

    public void onBailleurSelected(SelectEvent event){
        Bailleur bailleur = (Bailleur) event.getObject();
        imputationDto.setBailleur(bailleur);
        if (!bailleur.getCode().equals("ET")){
            sourceFinancementList = sourceFinancementService.listAll();
            // Suppression de la source de financement Trésor
            sourceFinancementList.stream().filter(sourceFinancement -> sourceFinancement.getCode().equals("1"))
                    .findFirst().ifPresent(sourceFinancement -> sourceFinancementList.remove(sourceFinancement));
        } else {
            sourceFinancementList = sourceFinancementService.listAll();
            initSourceFinancement();
        }
    }

    public boolean disableSectionField(){
        return natureTransaction.equals(NatureTransaction.VIREMENT);
    }

    public void onSectionSelected(){
        if(imputationDto.getSection()!=null)
            activiteDeServiceList = activiteService.findBySectionCode(imputationDto.getSection().getCode());
    }

    public void creer(){
        imputationHandleReturnBag.getImputationDtoList().add(imputationDto);
        bailleurList = bailleurService.listAll();
        sourceFinancementList = sourceFinancementService.listAll();
        this.initImputationDto();
        imputationDto.setSection(selectedSection);
    }

    public void supprimer(ImputationDto imputationDto){
        imputationHandleReturnBag.getImputationDtoList().remove(imputationDto);
    }

    public void ajouter(){

        imputationHandleReturnBag.setLigneOperationList(operationService.buildLigneOperationListFromImputationList(imputationHandleReturnBag.getImputationDtoList()));
        PrimeFaces.current().dialog().closeDynamic(imputationHandleReturnBag);
    }
}
