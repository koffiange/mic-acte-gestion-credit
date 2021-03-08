package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.BailleurClient;
import ci.gouv.dgbf.agc.client.SourceFinancementClient;
import ci.gouv.dgbf.agc.dto.SourceFinancement;
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
public class SourceFinancementService implements SourceFinancementClient {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    SourceFinancementClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(SourceFinancementClient.class);
    }


    @Override
    public List<SourceFinancement> listAll() {
        return client.listAll();
    }
}
