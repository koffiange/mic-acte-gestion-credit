package ci.gouv.dgbf.appmodele.backing;

import ci.gouv.dgbf.sib.menu.generator.MenuGenerator;
import ci.gouv.dgbf.sib.menu.generator.domain.MenuTab;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.primefaces.model.menu.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Named("menuBacking")
@ViewScoped
public class MenuBacking implements Serializable{

    private static final String LOCAL_ENV_VALUE = "local";

    @ConfigProperty(name = "SIIB_ENVIRONMENT")
    String environment;

    @Inject
    MenuGenerator menuGenerator;

    List<MenuTab> listMenusTabs = new ArrayList<>();
    List<MenuTab> accountListMenu = new ArrayList<>();
    MenuModel accountMenuModel;
    
    @PostConstruct
    public void init(){
        System.out.println("env : " + environment);
        if(environment == null)
            return;

        listMenusTabs = menuGenerator.generateServiceMenu(getAppCode(),getUsername(), "/edition-dppd");
        accountListMenu = menuGenerator.generateAccountMenu();
        if(!accountListMenu.isEmpty()) 
            accountMenuModel = accountListMenu.get(0).getMenuModel();
    }

    private String getAppCode() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return ctx.getExternalContext().getInitParameter("sib.mic.CODE");
    }
    
    public List<MenuTab> getListMenusTabs() {
        return listMenusTabs;
    }

    public void setListMenusTabs(List<MenuTab> listMenusTabs) {
        this.listMenusTabs = listMenusTabs;
    }
    
    public String resolveMenuFile(){
        if(environment == null)
            return "static-menu.xhtml";
        return "generated-menu.xhtml";
    }
    
    public String resolveAccountMenuFile(){
        if(environment == null)
            return "static-account-menu.xhtml";
        return "generated-account-menu.xhtml";
    }

    public MenuModel getAccountMenuModel() {
        return accountMenuModel;
    }

    public void setAccountMenuModel(MenuModel accountMenuModel) {
        this.accountMenuModel = accountMenuModel;
    }

    public String getUsername(){
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        String username = principal.getName();
        if(principal instanceof KeycloakPrincipal) {
            username = ((KeycloakPrincipal<KeycloakSecurityContext>) principal).getKeycloakSecurityContext().getIdToken().getPreferredUsername();
        }
        return username;
    }
    
}
