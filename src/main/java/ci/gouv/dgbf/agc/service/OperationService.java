package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.OperationClient;
import ci.gouv.dgbf.agc.dto.LigneDepense;
import ci.gouv.dgbf.agc.dto.Operation;
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

@ApplicationScoped
public class OperationService implements OperationClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    OperationClient client;

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
        operation.setSourceFinancement(ligneDepense.getSourceFinancement());
        operation.setBudgetActuelAE(ligneDepense.getMontantAe());
        operation.setBudgetActuelCP(ligneDepense.getMontantCp());
        operation.setExercice(ligneDepense.getExercice());
        operation.setLigneDepenseUuid(ligneDepense.getLigneDepenseId());
        operation.setNatureEconomiqueLibelle(ligneDepense.getNatureEconomiqueLibelle());
        operation.setNatureEconomiqueCode(ligneDepense.getNatureEconomiqueCode());

        return operation;
    }
}
