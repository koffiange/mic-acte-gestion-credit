package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.CategorieActe;
import ci.gouv.dgbf.agc.enumeration.NatureTransaction;
import ci.gouv.dgbf.agc.enumeration.StatutActe;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import ci.gouv.dgbf.agc.exception.CreditInsuffisantException;
import ci.gouv.dgbf.agc.exception.ReferenceAlreadyExistException;
import ci.gouv.dgbf.agc.service.*;
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


public class ActeMouvementCreditCreateBacking_old extends BaseBacking {
    /*

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    private ActeService acteService;

    @Inject
    private SectionService sectionService;

    @Inject
    private OperationSessionService operationSessionService;

    @Inject
    private LigneOperationService ligneOperationService;

    @Getter @Setter
    private List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    private List<LigneOperation> ligneOperationList = new ArrayList<>();

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
    private final BigDecimal zero = BigDecimal.ZERO;

    @Getter @Setter
    private String corpus;

    @Getter @Setter
    private boolean appliquerActe = false;

    @Getter @Setter
    private boolean addSignataireLock = false;

    @Getter @Setter
    private boolean destinationBtnStatus = true;

    @PostConstruct
    public void init(){
        acte = new Acte();
        signataire = new Signataire();
        acteDto = new ActeDto();
        sectionList = sectionService.list();
    }

    @PreDestroy
    public void destroy(){
        operationSessionService.reset();
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
        // acte.setCategorieActe(CategorieActe.ACTE_MOUVEMENT);
        acte.setStatutActe(StatutActe.EN_ATTENTE);
    }

    private void buildActeDto(){
        this.buildActe();
        acte.setCumulRetranchementAE(cumulRetranchementAE);
        acte.setCumulRetranchementCP(cumulRetranchementCP);
        acte.setCumulAjoutAE(cumulAjoutAE);
        acte.setCumulAjoutCP(cumulAjoutCP);
        acteDto.setActe(acte);
        ligneOperationList.addAll(operationBagOrigine.getLigneOperationList());
        ligneOperationList.addAll(operationBagDestination.getLigneOperationList());
        acteDto.setLigneOperationList(ligneOperationList);
    }

    private void treatOperationBagBeforePersisting(OperationBag operationBag){
        try {
            LOG.info("=> OperationBag : "+operationBag.toString());
            // Négation des montans prélevé
            operationBag.getLigneOperationList().forEach(operation -> {
                if(operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                    operation.setMontantOperationAE(operation.getMontantOperationAE().negate());
            });
            LOG.info("=> Négation des montans prélevé [ok]");
            // Montant AE egal Montant CP
            operationBag.getLigneOperationList().forEach(operation -> operation.setMontantOperationCP(operation.getMontantOperationAE()));
            operationBag.getLigneOperationList().forEach(operation -> operation.setDisponibleRestantCP(operation.getDisponibleRestantAE()));
            LOG.info("=> Montant AE egal Montant CP [ok]");

        } catch (Exception exception){
            LOG.info(exception.getMessage());
        }
    }

    public void handleReturn(SelectEvent event){
        OperationBag operationBag = (OperationBag) event.getObject();
        // this.completeOperationList(operationBag.getTypeOperation(), operationBag.getLigneOperationList());
        this.completeImputationDTOList(operationBag);
        this.cumules();
        destinationBtnStatus = operationBagOrigine.getLigneOperationList().isEmpty();
        showSuccess();
    }


    public void persist(boolean appliquerActe){
        try{
            this.majOperationAvantVerification();
            this.verifierDisponibilite();
            LOG.info("Verification de la disponibilité des crédits [ok]");
            this.verifierReference(acte.getReference());
            LOG.info("Verification de la reférence [ok]");
            this.treatOperationBagBeforePersisting(operationBagOrigine);
            this.treatOperationBagBeforePersisting(operationBagDestination);
            LOG.info("Traitement des opérations avant persist [ok]");
            this.buildActeDto();
            LOG.info("Construction de ACTE DTO [ok]");
            Acte actePersisted = acteService.persist(acteDto);
            LOG.info("Sauvegarde [ok]");
            if (appliquerActe)
                acteService.appliquer(actePersisted.getUuid());
            LOG.info("Application [ok]");
            closeSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    private void majOperationAvantVerification(){
        operationBagOrigine.setLigneOperationList(ligneOperationService.operationListDisponibiliteSetter(operationBagOrigine.getLigneOperationList()));
        // this.truncateDisponible();
        LOG.info("Mise a jour des opération avant verification");
    }

    private void truncateDisponible(){
        operationBagOrigine.getLigneOperationList().forEach(operation -> operation.setMontantDisponibleAE(BigDecimal.TEN));
        operationBagOrigine.getLigneOperationList().forEach(operation -> operation.setMontantDisponibleCP(BigDecimal.TEN));
    }

    private void verifierDisponibilite() throws CreditInsuffisantException {
        StringBuilder msg = new StringBuilder("Crédits insufisants sur les lignes :");
        boolean hasCreditInsuffisant = false;
        for (int i = 0; i < operationBagOrigine.getLigneOperationList().size(); i ++){
            LigneOperation ligneOperation = operationBagOrigine.getLigneOperationList().get(i);
            if (ligneOperation.getMontantDisponibleAE().compareTo(ligneOperation.getMontantOperationAE()) < 0){
                msg.append(" #").append(i+1);
                hasCreditInsuffisant = true;
            }
        }
        if (hasCreditInsuffisant)
            throw new CreditInsuffisantException(msg.toString());
    }


    public void verifierReference(FacesContext facesContext, UIComponent uiComponent, Object o) {
        LOG.info("Verifier reference");
        String reference = (String) o;
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur de saisie", "La reférence "+reference+" est déjà associée à un acte existant");
        if (acteService.checkReferenceAlreadyExist(reference))
            throw new ValidatorException(message);
    }


    public void verifierReference(String reference) throws ReferenceAlreadyExistException{
        if (acteService.checkReferenceAlreadyExist(reference))
            throw new ReferenceAlreadyExistException("La reférence "+reference+" est déjà associée à un acte existant");
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

    private String retrieveSectionCodeToSend(){
        return (acte.getNatureTransaction().equals(NatureTransaction.VIREMENT) && !operationBagOrigine.getLigneOperationList().isEmpty()) ?
                operationBagOrigine.getLigneOperationList().get(0).getSectionCode() : "";
    }

    public void openNouvelleLigneDialog(String typeImputation){
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

        PrimeFaces.current().dialog().openDynamic("creer-imputation-dlg", options, params);
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

    private void completeSectionCodeList(TypeOperation typeOperation, List<String> sectionCodeList){
        sectionCodeList.forEach(code -> {
            if (typeOperation.equals(TypeOperation.ORIGINE) && !operationBagOrigine.getSectionCodeList().contains(code))
                operationBagOrigine.getSectionCodeList().add(code);

            if (typeOperation.equals(TypeOperation.DESTINATION) && !operationBagDestination.getSectionCodeList().contains(code))
                operationBagDestination.getSectionCodeList().add(code);
        });
    }

    private void completeImputationDTOList(OperationBag operationBag){
        if(operationBag.getTypeOperation().equals(TypeOperation.DESTINATION))
            acteDto.getImputationDtoList().addAll(operationBag.getImputationDtoList());
    }


    private void completeOperationList(TypeOperation typeOperation, List<LigneOperation> ligneOperationList){
        ligneOperationList.forEach(operation -> {
            if (typeOperation.equals(TypeOperation.ORIGINE) && !operationBagOrigine.getLigneOperationList().contains(operation)) {
                // operation.setSectionCode(operationBagOrigine.getSectionCodeList().get(1));
                operationBagOrigine.getLigneOperationList().add(operation);
            }

            if (typeOperation.equals(TypeOperation.DESTINATION) && !operationBagDestination.getLigneOperationList().contains(operation))
                operationBagDestination.getLigneOperationList().add(operation);
        });
    }

    public void close(){
        closeSuccess();
    }

    public void corpusHandleReturn(SelectEvent event){
        acte.setCorpus(event.getObject().toString());
    }




    public void deleteOperation(String location, LigneOperation ligneOperation){
        TypeOperation typeOperation = TypeOperation.valueOf(location);

        if (typeOperation.equals(TypeOperation.ORIGINE)){
            operationBagOrigine.getLigneOperationList().remove(ligneOperation);
            destinationBtnStatus = operationBagOrigine.getLigneOperationList().isEmpty();
        } else{
            operationBagDestination.getLigneOperationList().remove(ligneOperation);
        }
        this.cumules();
    }

    public void cumules(){
        operationBagOrigine.getLigneOperationList().stream().map(LigneOperation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementAE);
        operationBagOrigine.getLigneOperationList().stream().map(LigneOperation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementCP);
        operationBagDestination.getLigneOperationList().stream().map(LigneOperation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutAE);
        operationBagDestination.getLigneOperationList().stream().map(LigneOperation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutCP);
        this.handleDisponibleRestant();
    }

    private void handleDisponibleRestant(){
        operationBagOrigine.getLigneOperationList().forEach(operation -> {
            operation.setDisponibleRestantAE(operation.getMontantDisponibleAE().subtract(operation.getMontantOperationAE()));
            operation.setDisponibleRestantCP(operation.getMontantDisponibleCP().subtract(operation.getMontantOperationCP()));
        });
        operationBagDestination.getLigneOperationList().forEach(operation -> {
            operation.setDisponibleRestantAE(operation.getMontantDisponibleAE().add(operation.getMontantOperationAE()));
            operation.setDisponibleRestantCP(operation.getMontantDisponibleCP().add(operation.getMontantOperationCP()));
        });
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

     */
}
