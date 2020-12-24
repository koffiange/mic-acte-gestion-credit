package ci.gouv.dgbf.agc.service;


import ci.gouv.dgbf.agc.client.ActeClient;
import ci.gouv.dgbf.agc.dto.Acte;
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
public class ActeService implements ActeClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    ActeClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(ActeClient.class);
    }

    @Override
    public List<Acte> listAll() {
        return client.listAll();
    }

    @Override
    public Acte findById(String uuid) {
        return client.findById(uuid);
    }

    @Override
    public void persist(Acte acte) {
        client.persist(acte);
    }

    @Override
    public void update(Acte acte) {
        client.update(acte);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }
}
