package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.bag.ImputationHandleReturnBag;
import ci.gouv.dgbf.agc.bag.LigneDepenseHandleReturnBag;
import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.*;
import ci.gouv.dgbf.agc.exception.CreditInsuffisantException;
import ci.gouv.dgbf.agc.exception.ReferenceAlreadyExistException;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.LigneOperationService;
import ci.gouv.dgbf.agc.service.SectionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "amcUpdateBacking")
@ViewScoped
public class MouvementCreditUpdateBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    private ActeService acteService;

    @Inject
    private SectionService sectionService;

    @Inject
    private LigneOperationService ligneOperationService;

    @Getter
    @Setter
    private List<NatureTransaction> natureTransactionList = Arrays.asList(NatureTransaction.values());

    @Getter @Setter
    private List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    private List<LigneOperation> ligneOperationOrigineList;

    @Getter @Setter
    private List<LigneOperation> ligneOperationDestinationList;

    @Getter @Setter
    private List<ImputationDto> imputationDtoList = new ArrayList<>();

    private OperationBag operationBag;

    @Getter @Setter
    private Acte acte;

    @Getter @Setter
    private Operation operation;

    @Getter @Setter
    private Date date;

    @Getter @Setter
    private Signataire signataire;

    @Getter @Setter
    private BigDecimal cumulRetranchementAE;

    @Getter @Setter
    private BigDecimal cumulRetranchementCP;

    @Getter @Setter
    private BigDecimal cumulAjoutAE;

    @Getter @Setter
    private BigDecimal cumulAjoutCP;

    @Getter @Setter
    private final BigDecimal zero = BigDecimal.ZERO;

    @Getter @Setter
    private boolean appliquerActe = false;

    @Getter @Setter
    private boolean addSignataireLock = false;

    @Getter @Setter
    private boolean destinationBtnStatus = false;

    private Map<String, String> params;

    private CategorieActe categorieActe;


    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            operationBag = ligneOperationService.findBagById(params.get("uuid"));
            acte = operationBag.getActe();
            date = convertIntoDate(acte.getDateSignature());
            signataireList = operationBag.getSignataireList();
            categorieActe = operationBag.getActe().getCategorieActe();
            ligneOperationOrigineList = operationBag.getLigneOperationList().stream()
                                        .filter(ligneOperation -> ligneOperation.getTypeOperation().equals(TypeOperation.ORIGINE))
                                        .peek(ligneOperation -> ligneOperation.setMontantOperationAE(ligneOperation.getMontantOperationAE().negate()))
                                        .collect(Collectors.toList());
            ligneOperationDestinationList = operationBag.getLigneOperationList().stream()
                                        .filter(ligneOperation -> ligneOperation.getTypeOperation().equals(TypeOperation.DESTINATION))
                                        .collect(Collectors.toList());

            cumulRetranchementAE = operationBag.getActe().getCumulRetranchementAE();
            cumulRetranchementCP = operationBag.getActe().getCumulRetranchementCP();
            cumulAjoutAE = operationBag.getActe().getCumulAjoutAE();
            cumulAjoutCP = operationBag.getActe().getCumulAjoutCP();
        }
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

    public void addSignataire(){
        LOG.info("added!!");
        signataireList.add(signataire);
        signataire = new Signataire();
        this.changeAddBtnSignataireAbility();
    }

    public void deleteSignataire(Signataire signataire){
        signataireList.remove(signataire);
        this.changeAddBtnSignataireAbility();
    }

    public void openLigneDepenseDialog(String typeImputation){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "90vw");
        options.replace("height", "90vh");
        Map<String, List<String>> params = new HashMap<>();

        if (TypeOperation.valueOf(typeImputation).equals(TypeOperation.DESTINATION)) {
            List<String> sectionCodeList = new ArrayList<>();
            sectionCodeList.add(this.retrieveSectionCodeToSend());
            params.put("sectionCode", sectionCodeList);
        }

        List<String> natureTransactionList = new ArrayList<>();
        natureTransactionList.add(acte.getNatureTransaction().toString());
        params.put("natureTransaction", natureTransactionList);

        List<String> typeImputationList = new ArrayList<>();
        typeImputationList.add(typeImputation);
        params.put("typeImputation", typeImputationList);

        PrimeFaces.current().dialog().openDynamic("rechercher-source-financement-dlg", options, params);
    }


    public void openNouvelleImputationDialog(String typeImputation){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "90vw");
        options.replace("height", "90vh");
        Map<String, List<String>> params = new HashMap<>();

        if (TypeOperation.valueOf(typeImputation).equals(TypeOperation.DESTINATION)) {
            List<String> sectionCodeList = new ArrayList<>();
            sectionCodeList.add(this.retrieveSectionCodeToSend());
            params.put("sectionCode", sectionCodeList);
        }

        List<String> natureTransactionList = new ArrayList<>();
        natureTransactionList.add(acte.getNatureTransaction().name());
        params.put("natureTransaction", natureTransactionList);

        PrimeFaces.current().dialog().openDynamic("creer-imputation-dlg", options, params);
    }

    /**
     * Permet de retrouver le coe de la section à envoyé au dialog de recherche des lignes de dépense
     * ou de creation d'imputation.
     * @return
     */
    private String retrieveSectionCodeToSend(){
        return (acte.getNatureTransaction().equals(NatureTransaction.VIREMENT) && !ligneOperationOrigineList.isEmpty()) ?
                ligneOperationOrigineList.get(0).getSectionCode() : "";
    }

    public void ligneDepenseHandleReturn(SelectEvent event){
        LigneDepenseHandleReturnBag ligneDepenseHandleReturnBag = (LigneDepenseHandleReturnBag) event.getObject();
        if (!ligneDepenseHandleReturnBag.getLigneOperationList().isEmpty()){
            // Actualise les listes de lignes
            if (ligneDepenseHandleReturnBag.getTypeOperation().equals(TypeOperation.ORIGINE)){
                ligneOperationOrigineList.addAll(ligneDepenseHandleReturnBag.getLigneOperationList());
            } else {
                ligneOperationDestinationList.addAll(ligneDepenseHandleReturnBag.getLigneOperationList());
            }
            // Dévérouille les boutons des destinations
            destinationBtnStatus = ligneOperationOrigineList.isEmpty();
        }
    }

    public void imputationHandleReturn(SelectEvent event){
        ImputationHandleReturnBag imputationHandleReturnBag = (ImputationHandleReturnBag) event.getObject();
        if (!imputationHandleReturnBag.getLigneOperationList().isEmpty()){
            if (imputationHandleReturnBag.getTypeOperation().equals(TypeOperation.ORIGINE)){
                ligneOperationOrigineList.addAll(imputationHandleReturnBag.getLigneOperationList());
            } else {
                ligneOperationDestinationList.addAll(imputationHandleReturnBag.getLigneOperationList());
            }
            imputationDtoList.addAll(imputationHandleReturnBag.getImputationDtoList());
            // Dévérouille les bouton des destinations
            destinationBtnStatus = ligneOperationOrigineList.isEmpty();
        }
    }

    public void deleteLigneOperation(LigneOperation ligneOperation, String typeOperationValue){
        TypeOperation typeOperation = TypeOperation.valueOf(typeOperationValue);
        if (typeOperation.equals(TypeOperation.ORIGINE)){
            ligneOperationOrigineList.remove(ligneOperation);
            destinationBtnStatus = ligneOperationOrigineList.isEmpty();
        } else{
            ligneOperationDestinationList.remove(ligneOperation);
            this.removeImputation(ligneOperation);
        }
        this.cumules();
    }

    private void removeImputation(LigneOperation ligneOperation){
        AtomicReference<ImputationDto> imputationToRemove = new AtomicReference<>();
        imputationDtoList.stream().filter(imputation -> imputation.getUuid().equals(ligneOperation.getLigneDepenseUuid())).findFirst().ifPresent(imputationToRemove::set);
        imputationDtoList.remove(imputationToRemove.get());
    }

    public void cumules(){
        ligneOperationOrigineList.stream().map(LigneOperation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementAE);
        ligneOperationOrigineList.stream().map(LigneOperation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementCP);
        ligneOperationDestinationList.stream().map(LigneOperation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutAE);
        ligneOperationDestinationList.stream().map(LigneOperation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutCP);
        this.handleDisponibleRestant();
    }

    private void handleDisponibleRestant(){
        ligneOperationOrigineList.forEach(operation -> {
            operation.setDisponibleRestantAE(operation.getMontantDisponibleAE().subtract(operation.getMontantOperationAE()));
            operation.setDisponibleRestantCP(operation.getMontantDisponibleCP().subtract(operation.getMontantOperationCP()));
        });
        ligneOperationDestinationList.forEach(operation -> {
            operation.setDisponibleRestantAE(operation.getMontantDisponibleAE().add(operation.getMontantOperationAE()));
            operation.setDisponibleRestantCP(operation.getMontantDisponibleCP().add(operation.getMontantOperationCP()));
        });
    }

    public void update(boolean appliquerActe){
        try{
            this.majOperationAvantVerification();
            this.verifierDisponibilite();
            LOG.info("Verification de la disponibilité des crédits [ok]");
            // this.verifierReference(acte.getReference());
            // LOG.info("Verification de la reférence [ok]");
            this.treatOperationBagBeforePersisting(ligneOperationOrigineList);
            this.treatOperationBagBeforePersisting(ligneOperationDestinationList);
            LOG.info("Traitement des opérations avant persist [ok]");
            this.buildActe();
            LOG.info(acte.toString());
            LOG.info("Construction de ACTE [ok]");
            this.buildOperationBag();
            LOG.info("== OPERARATIONBAG BUILD ==");
            LOG.info(operationBag.toString());
            LOG.info("Construction de OPERARATIONBAG [ok]");
            OperationBag operationBagPersisted = ligneOperationService.update(operationBag);
            LOG.info("Sauvegarde [ok]");
            if (appliquerActe)
                ligneOperationService.appliquer(operationBagPersisted);
            LOG.info("Application [ok]");
            closeSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    private void majOperationAvantVerification(){
        ligneOperationOrigineList = ligneOperationService.operationListDisponibiliteSetter(ligneOperationOrigineList);
        // this.truncateDisponible();
    }

    private void truncateDisponible(){
        ligneOperationOrigineList.forEach(operation -> operation.setMontantDisponibleAE(BigDecimal.TEN));
        ligneOperationOrigineList.forEach(operation -> operation.setMontantDisponibleCP(BigDecimal.TEN));
    }

    private void verifierDisponibilite() throws CreditInsuffisantException {
        StringBuilder msg = new StringBuilder("Crédits insufisants sur les lignes :");
        boolean hasCreditInsuffisant = false;
        for (int i = 0; i < ligneOperationOrigineList.size(); i ++){
            LigneOperation ligneOperation = ligneOperationOrigineList.get(i);
            if (ligneOperation.getMontantDisponibleAE().compareTo(ligneOperation.getMontantOperationAE()) < 0){
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

    private void treatOperationBagBeforePersisting(List<LigneOperation> ligneOperationList){
        try {
            // Négation des montans prélevé
            ligneOperationList.forEach(operation -> {
                if(operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                    operation.setMontantOperationAE(operation.getMontantOperationAE().negate());
            });

            // Montant AE egal Montant CP
            ligneOperationList.forEach(operation -> operation.setMontantOperationCP(operation.getMontantOperationAE()));
            ligneOperationList.forEach(operation -> operation.setDisponibleRestantCP(operation.getDisponibleRestantAE()));

        } catch (Exception exception){
            LOG.info("Erreur Traitement avant persistence : "+exception.getMessage());
        }
    }

    private void buildActe(){
        acte.setCategorieActe(categorieActe);
        acte.setStatutActe(acte.getStatutActe());
        acte.setNatureActe(NatureActe.valueOf(categorieActe.getNatureActe()));
        acte.setCumulRetranchementAE(cumulRetranchementAE);
        acte.setCumulRetranchementCP(cumulRetranchementAE);
        acte.setCumulAjoutAE(cumulAjoutAE);
        acte.setCumulAjoutCP(cumulAjoutAE);
        acte.setDateSignature(convertIntoLocaleDate(date));
    }

    private void buildOperation(){
        operation.setStatutOperation(StatutOperation.EN_ATTENTE);
        operation.setExercice(String.valueOf(LocalDate.now().getYear()));
    }

    private void buildOperationBag(){
        operationBag.setActe(acte);
        operationBag.setSignataireList(signataireList);
        operationBag.getLigneOperationList().clear();
        operationBag.getLigneOperationList().addAll(ligneOperationOrigineList);
        operationBag.getLigneOperationList().addAll(ligneOperationDestinationList);
        operationBag.getImputationDtoList().clear();
        operationBag.getImputationDtoList().addAll(imputationDtoList);

    }

    public boolean displayEnregisterButton(){
        return (cumulRetranchementAE.subtract(cumulAjoutAE).compareTo(BigDecimal.ZERO) != 0 || cumulRetranchementCP.subtract(cumulAjoutCP).compareTo(BigDecimal.ZERO) != 0);
    }

    public void close(){
        closeSuccess();
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
}
