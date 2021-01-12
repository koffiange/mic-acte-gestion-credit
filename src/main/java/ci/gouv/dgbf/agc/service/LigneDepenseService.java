package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.ActiviteClient;
import ci.gouv.dgbf.agc.client.LigneDepenseClient;
import ci.gouv.dgbf.agc.dto.LigneDepense;
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
public class LigneDepenseService implements LigneDepenseClient {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.api.uri", defaultValue = "http://mic-acte-gestion-credit-api/sib/api")
    String baseUri;
    URI apiUri;
    LigneDepenseClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(LigneDepenseClient.class);
    }


    @Override
    public List<LigneDepense> findAll() {
        return client.findAll();
    }

    @Override
    public List<LigneDepense> findByCritere(String nat_code) {
        return client.findByCritere(nat_code);
    }

    @Override
    public List<LigneDepense> findByActivite(String ads_code) {
        return client.findByActivite(ads_code);
    }

    @Override
    public List<LigneDepense> findBySection(String secb_code) {
        return client.findBySection(secb_code);
    }

    @Override
    public List<LigneDepense> findByCritere(String natureEconomiqueCode, String activiteCode, String sectionCode,
                                            String natureDepense, String programme, String action) {
        return client.findByCritere(natureEconomiqueCode, activiteCode, sectionCode, natureDepense, programme, action);
    }
}
