package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.CategorieActe;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.StatutActe;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.exception.CreditInsuffisantException;
import ci.gouv.dgbf.agc.exception.ReferenceAlreadyExistException;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.OperationService;
import ci.gouv.dgbf.agc.service.OperationSessionService;
import ci.gouv.dgbf.agc.service.SectionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.el.functions.Dates;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "amcUpdateBacking")
@ViewScoped
public class ActeMouvementCreditUpdateBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    private ActeService acteService;

    @Inject
    private SectionService sectionService;

    @Inject
    private OperationSessionService operationSessionService;

    @Inject
    private OperationService operationService;

    @Getter @Setter
    private List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    private List<Operation> operationList = new ArrayList<>();

    @Getter @Setter
    private OperationBag operationBagOrigine ;

    @Getter @Setter
    private OperationBag operationBagDestination ;

    @Getter @Setter
    private List<Section> sectionList;

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
    private final BigDecimal zero = BigDecimal.ZERO;

    @Getter @Setter
    private boolean appliquerActe = false;

    @Getter @Setter
    private boolean addSignataireLock = false;

    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            acteDto = acteService.findActeDtoById(params.get("uuid"));

            acte = acteDto.getActe();
            date = convertIntoDate(acte.getDateSignature());
            signataire = new Signataire();
            signataireList = acteDto.getSignataireList();

            // Construction des Bags
            List<Operation> operationOrigine = acteDto.getOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                                                      .peek(operation -> operation.setMontantOperationAE(operation.getMontantOperationAE().negate())).collect(Collectors.toList());
            List<Operation> operationDestination = acteDto.getOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.DESTINATION)).collect(Collectors.toList());

            operationBagOrigine = new OperationBag(TypeOperation.ORIGINE,operationOrigine);
            operationBagDestination = new OperationBag(TypeOperation.DESTINATION,operationDestination);
            operationBagOrigine.setOperationList(operationService.operationListDisponibiliteSetter(operationBagOrigine.getOperationList()));
            operationBagDestination.setOperationList(operationService.operationListDisponibiliteSetter(operationBagDestination.getOperationList()));

            operationSessionService.setOperationDestinationList(operationOrigine);
            operationSessionService.setOperationDestinationList(operationDestination);

            // Comule
            this.cumules();
        }
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

    public void update(){
        try{
            this.majOperationAvantVerification();
            this.verifierDisponibilite();
            LOG.info("Verification de la disponibilité des crédits [ok]");
            // this.verifierReference(acte.getReference());
            // LOG.info("Verification de la reférence [ok]");
            this.treatOperationBagBeforePersisting(operationBagOrigine);
            this.treatOperationBagBeforePersisting(operationBagDestination);
            LOG.info("Traitement des opérations avant persist [ok]");
            this.buildActeDto();
            LOG.info("Construction de ACTEDTO [ok]");
            acteService.update(appliquerActe, acteDto);
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

        LOG.info("section : "+operationBagOrigine.getSectionCodeList().size());

        if (TypeOperation.valueOf(typeImputation).equals(TypeOperation.DESTINATION))
            params.put("sectionCode", operationBagOrigine.getSectionCodeList());

        List<String> natureTransactionList = new ArrayList<>();
        natureTransactionList.add(acte.getNatureTransaction().toString());
        params.put("natureTransaction", natureTransactionList);

        List<String> typeImputationList = new ArrayList<>();
        typeImputationList.add(typeImputation);
        params.put("typeImputation", typeImputationList);

        PrimeFaces.current().dialog().openDynamic("rechercher-source-financement-dlg", options, params);
    }

    public void cumules(){
        operationBagOrigine.getOperationList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementAE);
        // operationBagOrigine.getOperationList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementCP);
        operationBagDestination.getOperationList().stream().map(Operation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutAE);
        // operationBagDestination.getOperationList().stream().map(Operation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutCP);
    }

    public void handleReturn(SelectEvent event){
        OperationBag operationBag = (OperationBag) event.getObject();
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
        closeCancel();
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

    public String equilibreAE(){
        String msg = "EQUILIBRE";
        if(cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) > 0)
            msg = "[RESTE A VENTILER]";
        if(cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) < 0)
            msg = "[VENTILE AU DELA DU DISPONIBLE]";
        return msg;
    }

    public String equilibreCP(){
        String msg = "EQUILIBRE";
        if(cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) > 0)
            msg = "[RESTE A VENTILER]";
        if(cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) < 0)
            msg = "[VENTILE AU DELA DU DISPONIBLE]";
        return msg;
    }

    public boolean displayEnregisterButton(){
        return (cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) != 0 || cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) != 0);
    }

    private void majOperationAvantVerification(){
        operationBagOrigine.setOperationList(operationService.operationListDisponibiliteSetter(operationBagOrigine.getOperationList()));
        // this.truncateDisponible();
    }

    private void verifierDisponibilite() throws CreditInsuffisantException {
        StringBuilder msg = new StringBuilder("Crédits insufisants sur les lignes :");
        boolean hasCreditInsuffisant = false;
        for (int i = 0; i < operationBagOrigine.getOperationList().size(); i ++){
            Operation operation = operationBagOrigine.getOperationList().get(i);
            if (operation.getMontantDisponibleAE().compareTo(operation.getMontantOperationAE()) < 0){
                msg.append(" #").append(i+1);
                hasCreditInsuffisant = true;
            }
        }
        if (hasCreditInsuffisant)
            throw new CreditInsuffisantException(msg.toString());
    }

    public void verifierReference(String reference) throws ReferenceAlreadyExistException {
        if (acteService.checkReferenceAlreadyExist(reference))
            throw new ReferenceAlreadyExistException("La reférence "+reference+" est déjà associée à un acte existant");
    }

    private void treatOperationBagBeforePersisting(OperationBag operationBag){
        LOG.info("=> OperationBag : "+operationBag.toString());
        // Négation des montans prélevé
        operationBag.getOperationList().forEach(operation -> {
            if(operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                operation.setMontantOperationAE(operation.getMontantOperationAE().negate());
        });
        LOG.info("=> Négation des montans prélevé [ok]");
        // Montant AE egal Montant CP
        operationBag.getOperationList().forEach(operation -> operation.setMontantOperationCP(operation.getMontantOperationAE()));
        LOG.info("=> Montant AE egal Montant CP [ok]");

    }
}
