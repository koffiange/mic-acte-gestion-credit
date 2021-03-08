package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.ActiviteDeServiceClient;
import ci.gouv.dgbf.agc.dto.ActiviteDeService;
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
public class ActiviteDeServiceService implements ActiviteDeServiceClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    ActiviteDeServiceClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(ActiviteDeServiceClient.class);
    }

    @Override
    public List<ActiviteDeService> findAll() {
        return client.findAll();
    }

    @Override
    public ActiviteDeService findByCode(String code) {
        return client.findByCode(code);
    }

    @Override
    public List<ActiviteDeService> findBySectionCode(String sectionCode) {
        return client.findBySectionCode(sectionCode);
    }
}
