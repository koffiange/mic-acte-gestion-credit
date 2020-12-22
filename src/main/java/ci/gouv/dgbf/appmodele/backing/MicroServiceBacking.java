package ci.gouv.dgbf.appmodele.backing;


import ci.gouv.dgbf.appmodele.dto.Portail;
import ci.gouv.dgbf.appmodele.dto.User;
import ci.gouv.dgbf.appmodele.service.PortailService;
import ci.gouv.dgbf.appmodele.service.UserAccountService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.representations.AccessToken;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("micBacking")
@SessionScoped
public class MicroServiceBacking implements Serializable {

    private static final Logger LOG = Logger.getLogger("MicroServiceBacking");
    private String requestAdresse;
    
    @Inject
    @ConfigProperty(name = "application.home")
    private String accueil;
    
    private String portailUri;

    private User user;
    
    @Inject
    PortailService portailService;

    @Inject
    UserAccountService userAccountService;

    @Inject
    HttpServletRequest httpServletRequest;
    
    @PostConstruct
    public void init(){
        requestAdresse = getRequestAddress();
        Optional<Portail> oPortail = portailService.retrievePortailAdresse();
        portailUri = oPortail.map(p -> this.composeUri(p)).orElse("#");
        if (httpServletRequest.getUserPrincipal() != null)
            user = getUserInfo(httpServletRequest);
    }
    
    private String composeUri(Portail p){
        return getRequestAddress()+p.getAddress();
    }

    private void reloadPage() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest)ec.getRequest()).getRequestURI());
    }

    public String getAppDisplayName() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String appname = ctx.getExternalContext().getInitParameter("sib.mic.DISPLAY_NAME");
        return appname;
    }
    
    private String getRequestAddress(){
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return origRequest.getScheme()+"://"+origRequest.getServerName();
    }

    public void logout() throws IOException {
        HttpServletRequest req = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        try {
            req.logout();
        } catch (ServletException ex) {
            LOG.log(Level.SEVERE, "Error ", ex);
        }

        // FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8300/sib/app-model");
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://siibtest.dgbf.ci");
    }

    public String getPortailUri() {
        return portailUri;
    }

    public void setPortailUri(String portailUri) {
        this.portailUri = portailUri;
    }

    public String getRequestAdresse() {
        return requestAdresse;
    }

    public void setRequestAdresse(String requestAdresse) {
        this.requestAdresse = requestAdresse;
    }

    public String getAccueil() {
        return accueil;
    }

    public void setAccueil(String accueil) {
        this.accueil = accueil;
    }

    public AccessToken getToken(HttpServletRequest request){
        return userAccountService.getToken(request);
    }

    public String getAccountUri(){
        return "https://siib.dgbf.ci/sib/acteur/mon_compte";
    }

    public String getOtherAttribute(HttpServletRequest request, String attribute_key){
        return userAccountService.getOtherAttribute(request, attribute_key);
    }

    private User getUserInfo(HttpServletRequest request){
        return userAccountService.getUserInfo(request);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
