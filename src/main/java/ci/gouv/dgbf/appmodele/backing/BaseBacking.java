/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci.gouv.dgbf.appmodele.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.appmodele.util.Messages;
import org.primefaces.PrimeFaces;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author florent
 */
@Dependent
public class BaseBacking implements Serializable {

    private static final Logger LOG = Logger.getLogger(BaseBacking.class.getName());

    protected static final String SUCCESS_MESSAGE = "Opération bien effectuée";
    protected static final String ERROR_MESSAGE = "Une erreur s'est produite";

    public void fictiveAction(){}

    public FacesContext getFacesContext(){
        return FacesContext.getCurrentInstance();
    }
    
    public ExternalContext getExternalContext(){
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public void addGlobalWarningMessage(String message){
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message ,"");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void addGlobalMessage(FacesMessage msg){
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void addMessage(String clientId,FacesMessage msg){
        FacesContext.getCurrentInstance().addMessage(clientId, msg);
    }
    
    public void getMessage(String severite, String titre, String detail){
        FacesContext context = this.getFacesContext();
        FacesMessage message = null;
        switch(severite){
            case "info": 
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, titre, detail);
                break;
            case "warn":
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, titre, detail);
                break;
            case "error":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, titre, detail);
                break;
            case "success":
                message = new FacesMessage(titre, detail);
                break;
        }
        context.addMessage(null, message);
    }
    
    public void putToFlash(String key,Object value){
        Flash flash = getExternalContext().getFlash();
        if(flash != null){
            getExternalContext().getFlash().put(key, value);
        }
        
    }
    
    public Object getFromFlash(String key){
        Flash flash = getExternalContext().getFlash();
        return (flash != null) ? getExternalContext().getFlash().get(key): null;
    }
    
     protected String getViewId(){
     return  getFacesContext().getViewRoot().getViewId();
   }

    protected Map<String,Object> getCutomDialogOptions(String width, String height){
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("width", "75vw");
        options.put("height", "65vh");
        options.put("contentWidth", "100%");
        options.put("contentHeight", "95%");

        return options;
    }

    protected Map<String,Object> getLevelOneDialogOptions(){
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("width", "75vw");
        options.put("height", "65vh");
        options.put("contentWidth", "100%");
        options.put("contentHeight", "95%");

        return options;
   }
    
   protected Map<String,Object> getLevelTwoDialogOptions(){
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("width", "65vw");
        options.put("height", "55vh");
        options.put("contentWidth", "100%");
        options.put("contentHeight", "95%");
        return options;
   }
   
   protected Map<String,Object> getLevelThreeDialogOptions(){
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("width", "55vw");
        options.put("height", "45vh");
        options.put("contentWidth", "100%");
        options.put("contentHeight", "95%");
        
        return options;
   }
   
   protected Map<String,Object> getLevelFourDialogOptions(){
       
        Map<String,Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("width", "45vw");
        options.put("height", "45vh");
        options.put("contentWidth", "100%");
        options.put("contentHeight", "95%");
        
        return options;
   }
   
   public void addGlobalErrorMessage(String message){
       FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message ,"");
       FacesContext.getCurrentInstance().addMessage(null, msg);
   }
   
   protected FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }
   
   protected Map getRequestMap() {
        return getContext().getExternalContext().getRequestMap();
   }

    protected Map<String, String> getRequestParameterMap() {
        return getContext().getExternalContext().getRequestParameterMap();
    }
    
   protected HttpServletRequest getRequest() {
        return (HttpServletRequest) getContext().getExternalContext().getRequest();
   }
    
   protected HttpSession getSession() {
        return (HttpSession) getContext().getExternalContext().getSession(false);
    }
    
   protected String getAuthenticatedUser(){
        if(getContext().getExternalContext().getUserPrincipal().getName() != null){
            return getContext().getExternalContext().getUserPrincipal().getName();
        } else{
            return "ANONYMOUS_USER";
        }
   }

   public String getDeleteMessage(String code) {
        return Messages.getString("messages", "confirm_delete_message", new Object[]{code});
   }
   public void closeSuccess(String message){
        PrimeFaces.current().dialog().closeDynamic(message);
    }
   public void closeSuccess(){
        PrimeFaces.current().dialog().closeDynamic(SUCCESS_MESSAGE);
    }
   public void closeError(String errorMessage){
        PrimeFaces.current().dialog().closeDynamic(errorMessage);
    }   
   public void closeCancel(){
        PrimeFaces.current().dialog().closeDynamic("");
    }
   public void getFailedMessage(String errorMessage){
        LOG.log(Level.SEVERE, "--->> Error : {0}", errorMessage);
    }
   public void showSuccess(String message){
        getMessage("info", message, "");
    }public void showSuccess(){
        getMessage("info", SUCCESS_MESSAGE, "");
    }
   public void showError(String errorMessage){
        getMessage("error", ERROR_MESSAGE, errorMessage);
   }
   public void showError(){
        getMessage("error", ERROR_MESSAGE, "");
   }
   public void throwExistError(String errorMessage){
        LOG.log(Level.INFO, "--- errorMessage : {0}", errorMessage);
        getMessage("error", errorMessage, "");
   }


   // DATE UTIL
    public String displayFormatedDate(Date date){
        LocalDate ld = convertIntoLocaleDate(date);
        String pattern = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern, Locale.FRENCH);
        return dtf.format(ld);
    }

    public String displayFormatedDate(LocalDate date){
        String pattern = "dd-MM-yyyy";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern, Locale.FRENCH);
        return (date != null) ? dtf.format(date) : "";
    }

    public String displayFormatedDateTime(LocalDateTime date){
        String pattern = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern, Locale.FRENCH);
        return (date != null) ? dtf.format(date) : "";
    }

   public LocalDate convertIntoLocaleDate(Date d){
       Instant instant = d.toInstant();
       ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
       return zonedDateTime.toLocalDate();
   }

    public Date convertIntoDate(LocalDate localDate){
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date convertStringIntoDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date d = format.parse(date.replace('/', '-'));
        return d;
    }



    // MONNEY UTIL
    public static String displayBigDecimalThousandSeparator(BigDecimal value){
        /*
        String pattern = "###  ###.###";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
         */
        if (value == null)
            return "";

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRENCH);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(value);
    }

    public String handleMaxValue(BigDecimal montant){
        return montant.toString();
    }

    public static String displayLongThousandSeparator(Long value){
        if (value == null)
            return "";
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.FRENCH);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(value);
    }

    public String concateCodeLibelle(Object object){
        String strConcate = "";
        if (object instanceof Section){
            Section section = (Section) object;
            strConcate = section.getCode()+" - "+section.getLibelle();
        }

        if (object instanceof ActiviteDeService){
            ActiviteDeService activiteDeService = (ActiviteDeService) object;
            strConcate = activiteDeService.getAdsCode()+" - "+ activiteDeService.getAdsLibelle();
        }

        if (object instanceof NatureEconomique){
            NatureEconomique natureEconomique = (NatureEconomique) object;
            strConcate = natureEconomique.getCode()+" - "+natureEconomique.getLibelleLong();
        }

        if (object instanceof Bailleur){
            Bailleur bailleur = (Bailleur) object;
            strConcate = bailleur.getCode()+" - "+bailleur.getDesignation();
        }

        if (object instanceof SourceFinancement){
            SourceFinancement sourceFinancement = (SourceFinancement) object;
            strConcate = sourceFinancement.getCode()+" - "+sourceFinancement.getLibelle();
        }
        return strConcate;
    }
}
