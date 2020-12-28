package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.ActeClient;
import ci.gouv.dgbf.agc.client.ActiviteClient;
import ci.gouv.dgbf.agc.dto.Activite;
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
public class ActiviteService implements ActiviteClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    ActiviteClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(ActiviteClient.class);
    }

    @Override
    public List<Activite> findAll() {
        return client.findAll();
    }

    @Override
    public Activite findByCode(String code) {
        return client.findByCode(code);
    }
}
