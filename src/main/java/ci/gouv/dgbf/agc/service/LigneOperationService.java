package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.OperationClient;
import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.enumeration.DisponibiliteCreditOperation;
import ci.gouv.dgbf.agc.enumeration.OrigineImputation;
import ci.gouv.dgbf.agc.enumeration.TypeOperation;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class LigneOperationService implements OperationClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    OperationClient client;

    @Inject
    LigneDepenseService ligneDepenseService;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(OperationClient.class);
    }


    @Override
    public List<OperationBag> listAll() {
        return client.listAll();
    }

    @Override
    public Operation findById(String uuid) {
        return client.findById(uuid);
    }

    @Override
    public OperationBag findBagById(String uuid) {
        return client.findBagById(uuid);
    }

    @Override
    public OperationBag persist(OperationBag operationBag) {
        return client.persist(operationBag);
    }

    @Override
    public OperationBag update(OperationBag operationBag) {
        return client.update(operationBag);
    }

    @Override
    public void appliquer(OperationBag operationBag) {
        client.appliquer(operationBag);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }

    public List<LigneOperation> buildLigneOperationListFromLigneDepenseList(List<LigneDepense> ligneDepenseList, TypeOperation typeOperation){
        List<LigneOperation> ligneOperationList = new ArrayList<>();
        ligneDepenseList.forEach(ligneDepense -> ligneOperationList.add(this.convertLigneDepenseIntoLigneOperation(ligneDepense, typeOperation)));
        return ligneOperationList;
    }

    public List<LigneOperation> buildLigneOperationListFromImputationList(List<ImputationDto> imputationDtoList){
        List<LigneOperation> ligneOperationList = new ArrayList<>();
        imputationDtoList.forEach(imputation -> ligneOperationList.add(this.convertImputationIntoLigneOperation(imputation)));
        return ligneOperationList;
    }

    public LigneOperation convertLigneDepenseIntoLigneOperation(LigneDepense ligneDepense, TypeOperation typeOperation){
        LigneOperation ligneOperation = new LigneOperation();


        ligneOperation.setFinancementId(ligneDepense.getFinancementId());
        ligneOperation.setPlafonId(ligneDepense.getPlafonId());
        ligneOperation.setUsbCode(ligneDepense.getUsbCode());
        ligneOperation.setUsbLibelle(ligneDepense.getUsbLibelle());
        ligneOperation.setActiviteId(ligneDepense.getActiviteId());
        ligneOperation.setActiviteCode(ligneDepense.getActiviteCode());
        ligneOperation.setActiviteLibelle(ligneDepense.getActiviteLibelle());
        ligneOperation.setActionId(ligneDepense.getActiviteId());
        ligneOperation.setActionCode(ligneDepense.getActiviteCode());
        ligneOperation.setActionLibelle(ligneDepense.getActiviteLibelle());
        ligneOperation.setSourceFinancementId(ligneDepense.getSourceFinancementId());
        ligneOperation.setBudgetActuelAE(ligneDepense.getMontantAe());
        ligneOperation.setBudgetActuelCP(ligneDepense.getMontantCp());
        ligneOperation.setMontantDisponibleAE(ligneDepense.getMontantDisponibleAE());
        ligneOperation.setMontantDisponibleCP(ligneDepense.getMontantDisponibleCP());
        ligneOperation.setDisponibleRestantAE(ligneDepense.getMontantDisponibleAE());
        ligneOperation.setDisponibleRestantCP(ligneDepense.getMontantDisponibleCP());
        ligneOperation.setExercice(ligneDepense.getExercice());
        ligneOperation.setLigneDepenseUuid(ligneDepense.getLigneDepenseId());
        ligneOperation.setSourceFinancementCode(ligneDepense.getSourceFinancementCode());
        ligneOperation.setSourceFinancementLibelle(ligneDepense.getSourceFinancementLibelle());
        ligneOperation.setBailleurId(ligneDepense.getBailleurId());
        ligneOperation.setBailleurLibelle(ligneDepense.getBailleurLibelle());
        ligneOperation.setNatureEconomiqueCode(ligneDepense.getNatureEconomiqueCode());
        ligneOperation.setNatureEconomiqueId(ligneDepense.getNatureEconomiqueId());
        ligneOperation.setNatureEconomiqueLibelle(ligneDepense.getNatureEconomiqueLibelle());
        ligneOperation.setSectionId(ligneDepense.getSectionId());
        ligneOperation.setSectionCode(ligneDepense.getSectionCode());
        ligneOperation.setSectionLibelle(ligneDepense.getSectionLibelle());
        ligneOperation.setOrigineImputation(OrigineImputation.BUDGET);
        ligneOperation.setTypeOperation(typeOperation);

        return ligneOperation;
    }

    public LigneOperation convertImputationIntoLigneOperation(ImputationDto imputationDto){
        LigneOperation ligneOperation = new LigneOperation();

        ligneOperation.setActiviteCode(imputationDto.getActiviteDeService().getAdsCode());
        ligneOperation.setActiviteLibelle(imputationDto.getActiviteDeService().getAdsLibelle());
        ligneOperation.setLigneDepenseUuid(imputationDto.getUuid());
        ligneOperation.setSourceFinancementId(imputationDto.getSourceFinancement().getId());
        ligneOperation.setSourceFinancementCode(imputationDto.getSourceFinancement().getCode());
        ligneOperation.setSourceFinancementLibelle(imputationDto.getSourceFinancement().getLibelle());
        ligneOperation.setBudgetActuelAE(BigDecimal.ZERO);
        ligneOperation.setBudgetActuelCP(BigDecimal.ZERO);
        ligneOperation.setMontantDisponibleAE(BigDecimal.ZERO);
        ligneOperation.setMontantDisponibleCP(BigDecimal.ZERO);
        ligneOperation.setDisponibleRestantAE(BigDecimal.ZERO);
        ligneOperation.setDisponibleRestantCP(BigDecimal.ZERO);
        ligneOperation.setExercice(imputationDto.getExercice());
        ligneOperation.setBailleurId(imputationDto.getBailleur().getId());
        ligneOperation.setBailleurLibelle(imputationDto.getBailleur().getDesignation());
        ligneOperation.setNatureEconomiqueCode(imputationDto.getNatureEconomique().getCode());
        ligneOperation.setNatureEconomiqueLibelle(imputationDto.getNatureEconomique().getLibelleLong());
        ligneOperation.setSectionCode(imputationDto.getSection().getCode());
        ligneOperation.setSectionLibelle(imputationDto.getSection().getLibelle());
        ligneOperation.setTypeOperation(TypeOperation.DESTINATION);
        ligneOperation.setOrigineImputation(OrigineImputation.NOUVELLE_LIGNE);

        return ligneOperation;
    }

    public List<LigneOperation> operationListDisponibiliteSetter(List<LigneOperation> ligneOperationListToSet){
        List<LigneDepense> ligneDepenseList = ligneDepenseService.findByOperation(ligneOperationListToSet);
        return ligneOperationListToSet.stream().map(operation -> this.disponibiliteSetter(operation, ligneDepenseList)).collect(Collectors.toList());
    }

    private LigneOperation disponibiliteSetter(LigneOperation ligneOperation, List<LigneDepense> ligneDepenseList){
        ligneDepenseList.stream().filter(l -> l.getLigneDepenseId().equals(ligneOperation.getLigneDepenseUuid()))
                        .findFirst()
                        .ifPresent(ligneDepense -> {
                            this.disponibiliteMontantSetter(ligneOperation, ligneDepense);
                            this.disponibiliteStatutSetter(ligneOperation, ligneDepense);
                        });
        return ligneOperation;
    }

    private void disponibiliteStatutSetter(LigneOperation ligneOperation, LigneDepense ligneDepense){
        if (ligneDepense.getMontantAe().compareTo(ligneOperation.getMontantOperationAE()) > 0){
            ligneOperation.setDisponibiliteCredit(DisponibiliteCreditOperation.CREDIT_DISPONIBLES);
        } else {
            ligneOperation.setDisponibiliteCredit(DisponibiliteCreditOperation.CREDIT_INSUFFISANTS);
        }
    }

    private void disponibiliteMontantSetter(LigneOperation ligneOperation, LigneDepense ligneDepense){
        ligneOperation.setMontantDisponibleAE(ligneDepense.getMontantDisponibleAE());
        ligneOperation.setMontantDisponibleCP(ligneDepense.getMontantDisponibleCP());
    }


}
