package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.VisaClient;
import ci.gouv.dgbf.agc.dto.Visa;
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
public class VisaService implements VisaClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    VisaClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(VisaClient.class);
    }


    @Override
    public List<Visa> listAll() {
        return client.listAll();
    }

    @Override
    public Visa findById(String uuid) {
        return client.findById(uuid);
    }

    @Override
    public void persist(Visa visa) {
        client.persist(visa);
    }

    @Override
    public void update(Visa visa) {
        client.update(visa);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }

}
