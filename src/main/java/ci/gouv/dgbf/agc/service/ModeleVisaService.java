package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.ModeleVisaClient;
import ci.gouv.dgbf.agc.client.VisaClient;
import ci.gouv.dgbf.agc.dto.ModeleVisa;
import ci.gouv.dgbf.agc.dto.ModeleVisaDto;
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
public class ModeleVisaService implements ModeleVisaClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    ModeleVisaClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(ModeleVisaClient.class);
    }

    @Override
    public List<ModeleVisa> listAll() {
        return client.listAll();
    }

    @Override
    public ModeleVisa findById(String uuid) {
        return client.findById(uuid);
    }

    @Override
    public void persist(ModeleVisa modeleVisa) {
        client.persist(modeleVisa);
    }

    @Override
    public void persist(ModeleVisaDto modeleVisaDto) {
        client.persist(modeleVisaDto);
    }

    @Override
    public void update(ModeleVisaDto modeleVisaDto) {
        client.update(modeleVisaDto);
    }

    @Override
    public void delete(String uuid) {
        client.delete(uuid);
    }
}
