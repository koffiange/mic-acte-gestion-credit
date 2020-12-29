package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.ActeClient;
import ci.gouv.dgbf.agc.client.ExerciceClient;
import ci.gouv.dgbf.agc.dto.Exercice;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@ApplicationScoped
public class ExerciceService implements ExerciceClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    ExerciceClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(ExerciceClient.class);
    }

    @Override
    public Exercice findCurrent() {
        return client.findCurrent();
    }

    @Override
    public void persist(Exercice exercice) {
        client.persist(exercice);
    }

    @Override
    public void update(Exercice exercice) {
        client.update(exercice);
    }
}
