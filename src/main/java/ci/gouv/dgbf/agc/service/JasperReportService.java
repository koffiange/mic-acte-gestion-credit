package ci.gouv.dgbf.agc.service;

import ci.gouv.dgbf.agc.client.JasperReportClient;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.QueryParam;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@ApplicationScoped
public class JasperReportService {
    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    @ConfigProperty(name = "application.agc.jasper.server.uri", defaultValue = "http://localhost:8400/jasperserver")
    String baseUri;

    @Inject @ConfigProperty(name = "jasper.username")
    String j_username;

    @Inject @ConfigProperty(name = "jasper.password")
    String j_password;

    @Getter @Setter
    private URI apiUri;

    JasperReportClient client;

    @PostConstruct
    public void init() {
        try {
            apiUri = new URI(baseUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(JasperReportClient.class);
    }

    public InputStream downloadFicheActe(@QueryParam("ACTE_ID") String acteId){
        return client.downloadFicheActe(acteId, j_username, j_password);
    }

    public InputStream downloadFicheListeActe(@QueryParam("STATUT_ACTE") String statutActe, @QueryParam("EXERCICE") String exercice){
        return client.downloadFicheListeActe(statutActe, exercice, j_username, j_password);
    }

}
