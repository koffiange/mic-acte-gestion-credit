package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.NatureEconomiqueClient;
import ci.gouv.dgbf.agc.dto.NatureEconomique;
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
public class NatureEconomiqueService implements NatureEconomiqueClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    NatureEconomiqueClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(NatureEconomiqueClient.class);
    }

    @Override
    public List<NatureEconomique> findAll() {
        return client.findAll();
    }

    @Override
    public NatureEconomique findByCode(String code) {
        return client.findByCode(code);
    }

    @Override
    public List<NatureEconomique> findByUuid(String uuid) {
        return client.findByUuid(uuid);
    }
}
