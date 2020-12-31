package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.ModeleVisaService;
import ci.gouv.dgbf.agc.service.SectionService;
import ci.gouv.dgbf.agc.service.operationSessionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

@Named(value = "amcCreateBacking")
@ViewScoped
public class ActeMouvementCreditCreateBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActeService acteService;

    @Inject
    SectionService sectionService;

    @Inject
    operationSessionService operationSessionService;

    @Inject
    ModeleVisaService modeleVisaService;

    @Getter @Setter
    List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    List<Operation> operationOrigineList = new ArrayList<>();

    @Getter @Setter
    List<Operation> operationDestinationList = new ArrayList<>();

    @Getter @Setter
    List<Section> sectionList;

    @Getter @Setter
    List<ModeleVisa> modeleVisaList;

    @Getter @Setter
    private ActeDto acteDto;

    @Getter @Setter
    private Acte acte;

    @Getter @Setter
    private Date date;

    @Getter @Setter
    private Signataire signataire;

    @Getter @Setter
    private Section selectedSection;

    @Getter @Setter
    private BigDecimal cumulRetranchementAE = BigDecimal.ZERO;

    @Getter @Setter
    private BigDecimal cumulRetranchementCP = BigDecimal.ZERO;

    @Getter @Setter
    private BigDecimal cumulAjoutAE = BigDecimal.ZERO;

    @Getter @Setter
    private BigDecimal cumulAjoutCP = BigDecimal.ZERO;

    @Getter @Setter
    private String corpus;

    @PostConstruct
    public void init(){
        acte = new Acte();
        signataire = new Signataire();
        acteDto = new ActeDto();
        sectionList = sectionService.list();
        modeleVisaList = modeleVisaService.listAll();
    }

    @PreDestroy
    public void destroy(){
        operationSessionService.reset();
    }

    public void addSignataire(){
        signataireList.add(signataire);
        signataire = new Signataire();
    }

    public void deleteSignataire(Signataire signataire){
        signataireList.remove(signataire);
    }

    public void addSignataire(String s){
        signataireList.remove(s);
    }

    private void buildActeDto(){
        acte.setDateSignature(convertIntoLocaleDate(date));
    }

    public void persist(){
        try{
            acteService.persist(acteDto);
            closeSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }

    }

    public void openLigneDepenseDialog(String typeImputation){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "90vw");
        options.replace("height", "90vh");
        Map<String, List<String>> params = new HashMap<>();

        List<String> sectionList = new ArrayList<>();
        sectionList.add(selectedSection.getCode());
        params.put("sectionCode", sectionList);

        List<String> natureTransactionList = new ArrayList<>();
        natureTransactionList.add(acte.getNatureTransaction().toString());
        params.put("natureTransaction", natureTransactionList);

        List<String> typeImputationList = new ArrayList<>();
        typeImputationList.add(typeImputation);
        params.put("typeImputation", typeImputationList);

        PrimeFaces.current().dialog().openDynamic("rechercher-source-financement-dlg", options, params);
    }

    public void openCorpusDialog(){
        Map<String, List<String>> params = new HashMap<>();
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "60vw");
        options.replace("height", "75vh");

        List<String> corpusList = new ArrayList<>();
        if(acte.getCorpus() == null) {
            corpusList.add("");
        } else {
            corpusList.add(acte.getCorpus());
        }
        params.put("corpus", corpusList);

        PrimeFaces.current().dialog().openDynamic("acte-corpus-dlg", options, params);
    }

    public void handleReturn(SelectEvent event){
        operationOrigineList = operationSessionService.getOperationOrigineList();
        operationDestinationList = operationSessionService.getOperationDestinationList();
        this.cumules();
        if (event != null)
            showSuccess();
    }

    public void close(){
        acte.setCorpus(corpus);
        LOG.info("Acte corpus: "+acte.getCorpus());
        closeSuccess();
    }

    public void corpusHandleReturn(SelectEvent event){
        acte.setCorpus(event.getObject().toString());
        LOG.info("Acte corpus output: "+acte.getCorpus());
    }




    public void deleteOperation(String location, Operation operation){
        if (location.equals("origine")){
            operationSessionService.getOperationOrigineList().remove(operation);
        } else{
            operationSessionService.getOperationDestinationList().remove(operation);
        }

        operationOrigineList = operationSessionService.getOperationOrigineList();
        operationDestinationList = operationSessionService.getOperationDestinationList();
        this.cumules();
    }

    private void cumules(){
        operationSessionService.getOperationOrigineList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementAE);
        operationSessionService.getOperationOrigineList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementCP);
        operationSessionService.getOperationDestinationList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutAE);
        operationSessionService.getOperationDestinationList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutCP);
    }

    public String equilibreAE(){
        String msg = "EQUILIBRE";
        if(cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) > 0)
            msg = "[SOLDE DEBITEUR]";
        if(cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) < 0)
            msg = "[SOLDE CREDITEUR]";
        return msg;
    }

    public String equilibreCP(){
        String msg = "EQUILIBRE";
        if(cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) > 0)
            msg = "[SOLDE DEBITEUR]";
        if(cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) < 0)
            msg = "[SOLDE CREDITEUR]";
        return msg;
    }

    public boolean displayEnregisterButton(){
        return (cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) != 0 || cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) != 0);
    }
}
