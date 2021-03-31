package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.LigneOperationService;
import ci.gouv.dgbf.agc.service.OperationSessionService;
import ci.gouv.dgbf.agc.service.SectionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;


public class ActeMouvementCreditUpdateBacking_old extends BaseBacking {
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
    private OperationBag operationBagOrigine ;

    @Getter @Setter
    private OperationBag operationBagDestination ;

    @Getter @Setter
    private List<Section> sectionList;


    // private ActeDto acteDto;

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

    /*
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
            List<LigneOperation> ligneOperationOrigine = acteDto.getLigneOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                                                      .peek(operation -> operation.setMontantOperationAE(operation.getMontantOperationAE().negate())).collect(Collectors.toList());
            List<LigneOperation> ligneOperationDestination = acteDto.getLigneOperationList().stream().filter(operation -> operation.getTypeOperation().equals(TypeOperation.DESTINATION)).collect(Collectors.toList());

            operationBagOrigine = new OperationBag(TypeOperation.ORIGINE, ligneOperationOrigine, new ArrayList<>());
            operationBagDestination = new OperationBag(TypeOperation.DESTINATION, ligneOperationDestination, new ArrayList<>());
            operationBagOrigine.setLigneOperationList(ligneOperationService.operationListDisponibiliteSetter(operationBagOrigine.getLigneOperationList()));
            operationBagDestination.setLigneOperationList(ligneOperationService.operationListDisponibiliteSetter(operationBagDestination.getLigneOperationList()));

            operationSessionService.setLigneOperationDestinationList(ligneOperationOrigine);
            operationSessionService.setLigneOperationDestinationList(ligneOperationDestination);

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
        ligneOperationList.addAll(operationBagOrigine.getLigneOperationList());
        ligneOperationList.addAll(operationBagDestination.getLigneOperationList());
        acteDto.setLigneOperationList(ligneOperationList);
    }

    public void update(boolean appliquerActe){
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
            Acte actePersisted = acteService.update(appliquerActe, acteDto);
            LOG.info("Mise a jour [ok]");
            if (appliquerActe)
                acteService.appliquer(actePersisted.getUuid());
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

     */

    /**
     * Permet de retrouver le coe de la section à envoyé au dialog de recherche des lignes de dépense
     * ou de creation d'imputation.
     * @return
     */
    /*
    private String retrieveSectionCodeToSend(){
        return (acte.getNatureTransaction().equals(NatureTransaction.VIREMENT) && !operationBagOrigine.getLigneOperationList().isEmpty()) ?
                operationBagOrigine.getLigneOperationList().get(0).getSectionCode() : "";
    }

    public void cumules(){
        operationBagOrigine.getLigneOperationList().stream().map(LigneOperation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementAE);
        // operationBagOrigine.getLigneOperationList().stream().map(LigneOperation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulRetranchementCP);
        operationBagDestination.getLigneOperationList().stream().map(LigneOperation::getMontantOperationAE).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutAE);
        // operationBagDestination.getLigneOperationList().stream().map(LigneOperation::getMontantOperationCP).reduce(BigDecimal::add).ifPresent(this::setCumulAjoutCP);
        this.handleDisponibleRestant();
    }

    private void handleDisponibleRestant(){
        operationBagOrigine.getLigneOperationList().forEach(operation -> operation.setDisponibleRestantAE(operation.getMontantDisponibleAE().subtract(operation.getMontantOperationAE())));
        operationBagDestination.getLigneOperationList().forEach(operation -> operation.setDisponibleRestantAE(operation.getMontantDisponibleAE().subtract(operation.getMontantOperationAE())));
    }

    public void handleReturn(SelectEvent event){
        OperationBag operationBag = (OperationBag) event.getObject();
        // this.completeSectionCodeList(operationBag.getTypeOperation(), operationBag.getSectionCodeList());
        this.completeOperationList(operationBag.getTypeOperation(), operationBag.getLigneOperationList());
        this.cumules();
        showSuccess();
    }

    /*
    private void completeSectionCodeList(TypeOperation typeOperation, List<String> sectionCodeList){
        sectionCodeList.forEach(code -> {
            if (typeOperation.equals(TypeOperation.ORIGINE) && !operationBagOrigine.getSectionCodeList().contains(code))
                operationBagOrigine.getSectionCodeList().add(code);

            if (typeOperation.equals(TypeOperation.DESTINATION) && !operationBagDestination.getSectionCodeList().contains(code))
                operationBagDestination.getSectionCodeList().add(code);
        });
    }

     */

    /*
    private void completeOperationList(TypeOperation typeOperation, List<LigneOperation> ligneOperationList){
        ligneOperationList.forEach(operation -> {
            if (typeOperation.equals(TypeOperation.ORIGINE) && !operationBagOrigine.getLigneOperationList().contains(operation))
                operationBagOrigine.getLigneOperationList().add(operation);

            if (typeOperation.equals(TypeOperation.DESTINATION) && !operationBagDestination.getLigneOperationList().contains(operation))
                operationBagDestination.getLigneOperationList().add(operation);
        });
    }

    public void close(){
        closeCancel();
    }

    public void deleteOperation(String location, LigneOperation ligneOperation){
        TypeOperation typeOperation = TypeOperation.valueOf(location);

        if (typeOperation.equals(TypeOperation.ORIGINE)){
            operationBagOrigine.getLigneOperationList().remove(ligneOperation);
        } else{
            operationBagDestination.getLigneOperationList().remove(ligneOperation);
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
        operationBagOrigine.setLigneOperationList(ligneOperationService.operationListDisponibiliteSetter(operationBagOrigine.getLigneOperationList()));
        // this.truncateDisponible();
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

    public void verifierReference(String reference) throws ReferenceAlreadyExistException {
        if (acteService.checkReferenceAlreadyExist(reference))
            throw new ReferenceAlreadyExistException("La reférence "+reference+" est déjà associée à un acte existant");
    }

    private void treatOperationBagBeforePersisting(OperationBag operationBag){
        LOG.info("=> OperationBag : "+operationBag.toString());
        // Négation des montans prélevé
        operationBag.getLigneOperationList().forEach(operation -> {
            if(operation.getTypeOperation().equals(TypeOperation.ORIGINE))
                operation.setMontantOperationAE(operation.getMontantOperationAE().negate());
        });
        LOG.info("=> Négation des montans prélevé [ok]");
        // Montant AE egal Montant CP
        operationBag.getLigneOperationList().forEach(operation -> operation.setMontantOperationCP(operation.getMontantOperationAE()));
        LOG.info("=> Montant AE egal Montant CP [ok]");

    }
    
     */
}
