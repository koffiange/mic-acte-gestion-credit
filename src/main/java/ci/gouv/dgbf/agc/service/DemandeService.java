package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.DemandeClient;
import ci.gouv.dgbf.agc.client.ModeleVisaClient;
import ci.gouv.dgbf.agc.dto.Demande;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DemandeService implements DemandeClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    DemandeClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(DemandeClient.class);
    }


    @Override
    public List<Demande> listAll() {
        return client.listAll();
    }

    @Override
    public Demande findById(String uuid) {
        return client.findById(uuid);
    }

    @Override
    public void persist(Demande demande) {
        client.persist(demande);
    }

    @Override
    public void update(Demande demande) {
        client.update(demande);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }
}
