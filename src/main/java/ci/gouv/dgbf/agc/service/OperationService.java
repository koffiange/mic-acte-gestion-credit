package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.OperationClient;
import ci.gouv.dgbf.agc.dto.LigneDepense;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.enumeration.DisponibiliteCreditOperation;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class OperationService implements OperationClient {

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
    public List<Operation> listAll() {
        return client.listAll();
    }

    @Override
    public Operation findById(String uuid) {
        return client.findById(uuid);
    }

    @Override
    public void persist(Operation operation) {
        client.persist(operation);
    }

    @Override
    public void update(Operation operation) {
        client.update(operation);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }

    public List<Operation> buildOperationListFromLigneDepenseList(List<LigneDepense> ligneDepenseList){
        List<Operation> operationList = new ArrayList<>();
        ligneDepenseList.forEach(ligneDepense -> operationList.add(this.convertLigneDepenseIntoOperation(ligneDepense)));
        return operationList;
    }

    public Operation convertLigneDepenseIntoOperation(LigneDepense ligneDepense){
        Operation operation = new Operation();

        operation.setActiviteCode(ligneDepense.getActiviteCode());
        operation.setActiviteLibelle(ligneDepense.getActiviteLibelle());
        operation.setSourceFinancementId(ligneDepense.getSourceFinancementId());
        operation.setBudgetActuelAE(ligneDepense.getMontantAe());
        operation.setBudgetActuelCP(ligneDepense.getMontantCp());
        operation.setMontantDisponibleAE(ligneDepense.getMontantDisponibleAE());
        operation.setMontantDisponibleCP(ligneDepense.getMontantDisponibleCP());
        operation.setExercice(ligneDepense.getExercice());
        operation.setLigneDepenseUuid(ligneDepense.getLigneDepenseId());
        operation.setSourceFinancementCode(ligneDepense.getSourceFinancementCode());
        operation.setSourceFinancementLibelle(ligneDepense.getSourceFinancementLibelle());
        operation.setNatureEconomiqueLibelle(ligneDepense.getNatureEconomiqueLibelle());
        operation.setNatureEconomiqueCode(ligneDepense.getNatureEconomiqueCode());
        operation.setSectionCode(ligneDepense.getSectionCode());
        operation.setSectionLibelle(ligneDepense.getSectionLibelle());

        return operation;
    }

    public List<Operation> operationListDisponibiliteSetter(List<Operation> operationListToSet){
        List<LigneDepense> ligneDepenseList = ligneDepenseService.findByOperation(operationListToSet);
        return operationListToSet.stream().map(operation -> this.disponibiliteSetter(operation, ligneDepenseList)).collect(Collectors.toList());
    }

    private Operation disponibiliteSetter(Operation operation, List<LigneDepense> ligneDepenseList){
        ligneDepenseList.stream().filter(l -> l.getLigneDepenseId().equals(operation.getLigneDepenseUuid()))
                        .findFirst()
                        .ifPresent(ligneDepense -> {
                            this.disponibiliteMontantSetter(operation, ligneDepense);
                            this.disponibiliteStatutSetter(operation, ligneDepense);
                        });
        return operation;
    }

    private void disponibiliteStatutSetter(Operation operation, LigneDepense ligneDepense){
        if (ligneDepense.getMontantAe().compareTo(operation.getMontantOperationAE()) > 0){
            operation.setDisponibiliteCredit(DisponibiliteCreditOperation.CREDIT_DISPONIBLES);
        } else {
            operation.setDisponibiliteCredit(DisponibiliteCreditOperation.CREDIT_INSUFFISANTS);
        }
    }

    private void disponibiliteMontantSetter(Operation operation, LigneDepense ligneDepense){
        operation.setMontantDisponibleAE(ligneDepense.getMontantDisponibleAE());
        operation.setMontantDisponibleCP(ligneDepense.getMontantDisponibleCP());
    }
}
