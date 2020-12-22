package ci.gouv.dgbf.appmodele.service;

import ci.gouv.dgbf.appmodele.client.UserClient;
import ci.gouv.dgbf.appmodele.dto.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.representations.AccessToken;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class UserAccountService {

    private static final Logger LOG = Logger.getLogger(PortailService.class.getName());

    @Inject
    @ConfigProperty(name = "application.user.api.uri")
    private String uri;

    private URI userUri;
    private UserClient userClient;

    @PostConstruct
    public void init(){
        try {
            userUri = new URI(uri);
        } catch (URISyntaxException e) {
            LOG.log(Level.INFO, "ERREUR : {0}", e.getMessage());
        }
        userClient = RestClientBuilder.newBuilder().baseUri(userUri).build(UserClient.class);
    }

    public User getUserInfo(HttpServletRequest request){
        return userClient.getUserInfo(this.getToken(request).getPreferredUsername());
    }

    public String getOtherAttribute(HttpServletRequest request, String attribute_key){
        Map<String, Object> otherClaims = this.getToken(request).getOtherClaims();

        if (otherClaims.containsKey(attribute_key)) {
            return String.valueOf(otherClaims.get(attribute_key));
        }

        return "";
    }

    public String getAccountUri(HttpServletRequest req) {
        KeycloakSecurityContext session = getSession(req);
        String baseUrl = getAuthServerBaseUrl(req);
        String realm = session.getRealm();
        return KeycloakUriBuilder.fromUri(baseUrl).path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH)
                .queryParam("referrer", "app-profile-jsp")
                .queryParam("referrer_uri", getReferrerUri(req)).build(realm).toString();
    }

    private String getReferrerUri(HttpServletRequest req) {
        StringBuffer uri = req.getRequestURL();
        String q = req.getQueryString();
        if (q != null) {
            uri.append("?").append(q);
        }
        return uri.toString();
    }

    private String getAuthServerBaseUrl(HttpServletRequest req) {
        AdapterDeploymentContext deploymentContext = (AdapterDeploymentContext) req.getServletContext().getAttribute(AdapterDeploymentContext.class.getName());
        KeycloakDeployment deployment = deploymentContext.resolveDeployment(null);
        return deployment.getAuthServerBaseUrl();
    }

    public AccessToken getToken(HttpServletRequest req) {
        return getSession(req).getToken();
    }

    private KeycloakSecurityContext getSession(HttpServletRequest req) {
        return (KeycloakSecurityContext) req.getAttribute(KeycloakSecurityContext.class.getName());
    }
}
