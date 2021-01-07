package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.CategorieActe;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.StatutActe;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.ModeleVisaService;
import ci.gouv.dgbf.agc.service.SectionService;
import ci.gouv.dgbf.agc.service.operationSessionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
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
    private ActeService acteService;

    @Inject
    private SectionService sectionService;

    @Inject
    private operationSessionService operationSessionService;

    @Inject
    private ModeleVisaService modeleVisaService;

    @Getter @Setter
    private List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    private List<Operation> operationList = new ArrayList<>();

    @Getter @Setter
    private OperationBag operationBagOrigine = new OperationBag();

    @Getter @Setter
    private OperationBag operationBagDestination = new OperationBag();

    @Getter @Setter
    private List<Section> sectionList;

    @Getter @Setter
    private List<ModeleVisa> modeleVisaList;

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

    @Getter @Setter
    private boolean appliquerActe = false;

    @Getter @Setter
    private boolean addSignataireLock = false;

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
        this.changeAddBtnSignataireAbility();
    }

    public void deleteSignataire(Signataire signataire){
        signataireList.remove(signataire);
        this.changeAddBtnSignataireAbility();
    }

    public void onNatureTransactionSelected(){
        this.changeAddBtnSignataireAbility();
    }

    public void changeAddBtnSignataireAbility(){
        if (acte.getNatureTransaction()!=null &&
            acte.getNatureTransaction().equals(NatureTransaction.VIREMENT) &&
            signataireList.size() >= 1){
            addSignataireLock = true;
        } else {
            addSignataireLock = false;
        }
        LOG.info("Nature transaction : "+ acte.getNatureTransaction());
        LOG.info("addSignataireLock value : "+ addSignataireLock);
    }



    public void addSignataire(String s){
        signataireList.remove(s);
    }

    private void buildActe(){
        acte.setDateSignature(convertIntoLocaleDate(date));
        acte.setCategorieActe(CategorieActe.ACTE_MOUVEMENT);
        acte.setStatutActe(StatutActe.EN_ATTENTE);
    }

    private void buildActeDto(){
        this.buildActe();
        acteDto.setActe(acte);
        acteDto.setSignataireList(signataireList);
        operationList.addAll(operationBagOrigine.getOperationList());
        operationList.addAll(operationBagDestination.getOperationList());
        acteDto.setOperationList(operationList);
    }

    public void persist(){
        try{
            this.buildActeDto();
            acteService.persist(appliquerActe, acteDto);
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
        OperationBag operationBag = (OperationBag) event.getObject();
        LOG.info("Operation Bag : "+operationBag.toString());
        this.completeSectionCodeList(operationBag.getTypeOperation(), operationBag.getSectionCodeList());
        this.completeOperationList(operationBag.getTypeOperation(), operationBag.getOperationList());
        this.cumules();
        showSuccess();
    }

    private void completeSectionCodeList(TypeOperation typeOperation, List<String> sectionCodeList){
        sectionCodeList.forEach(code -> {
            if (typeOperation.equals(TypeOperation.ORIGINE) && !operationBagOrigine.getSectionCodeList().contains(code))
                operationBagOrigine.getSectionCodeList().add(code);

            if (typeOperation.equals(TypeOperation.DESTINATION) && !operationBagDestination.getSectionCodeList().contains(code))
                operationBagDestination.getSectionCodeList().add(code);
        });
    }

    private void completeOperationList(TypeOperation typeOperation, List<Operation> operationList){
        operationList.forEach(operation -> {
            if (typeOperation.equals(TypeOperation.ORIGINE) && !operationBagOrigine.getOperationList().contains(operation))
                operationBagOrigine.getOperationList().add(operation);

            if (typeOperation.equals(TypeOperation.DESTINATION) && !operationBagDestination.getOperationList().contains(operation))
                operationBagDestination.getOperationList().add(operation);
        });
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
        TypeOperation typeOperation = TypeOperation.valueOf(location);

        if (typeOperation.equals(TypeOperation.ORIGINE)){
            operationBagOrigine.getOperationList().remove(operation);
        } else{
            operationBagDestination.getOperationList().remove(operation);
        }
        this.cumules();
    }

    private void cumules(){
        operationBagOrigine.getOperationList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementAE);
        operationBagOrigine.getOperationList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementCP);
        operationBagDestination.getOperationList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutAE);
        operationBagDestination.getOperationList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutCP);
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
