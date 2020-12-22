package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.CompositionClient;
import ci.gouv.dgbf.agc.client.VisaClient;
import ci.gouv.dgbf.agc.dto.Composition;
import ci.gouv.dgbf.agc.dto.ModeleVisa;
import ci.gouv.dgbf.agc.dto.Visa;
import ci.gouv.dgbf.agc.dto.VisaDto;
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
public class CompositionService implements CompositionClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    CompositionClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(CompositionClient.class);
    }


    @Override
    public List<Composition> findByModele(String uuid) {
        return client.findByModele(uuid);
    }

    @Override
    public void persist(ModeleVisa modeleVisa, List<VisaDto> visaList) {
        client.persist(modeleVisa, visaList);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }
}
