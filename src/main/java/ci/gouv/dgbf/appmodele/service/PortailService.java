/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci.gouv.dgbf.appmodele.service;

import ci.gouv.dgbf.appmodele.client.PortailClient;
import ci.gouv.dgbf.appmodele.dto.Portail;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author koffi
 */
@RequestScoped
public class PortailService {

    private static final Logger LOG = Logger.getLogger(PortailService.class.getName());
    
    @Inject
    @ConfigProperty(name = "application.portail.uri")
    private String uri;
    
    private URI portailUri;
    private PortailClient portailClient;
    
    @PostConstruct
    public void init(){
        try {
            portailUri = new URI(uri);
        } catch (URISyntaxException e) {
            LOG.log(Level.INFO, "ERREUR : {0}", e.getMessage());
        }
        portailClient = RestClientBuilder.newBuilder().baseUri(portailUri).build(PortailClient.class);
    }
    
    @Retry(maxRetries = 1, delay = 400L, maxDuration = 1200L)
    public Optional<Portail> retrievePortailAdresse(){
        Portail p;
        try{
            p = portailClient.portail();
        } catch(Exception e){
            p = null;
        }
        return Optional.ofNullable(p);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    
}
