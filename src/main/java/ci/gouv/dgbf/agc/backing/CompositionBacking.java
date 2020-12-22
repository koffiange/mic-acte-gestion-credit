package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Composition;
import ci.gouv.dgbf.agc.dto.ModeleVisa;
import ci.gouv.dgbf.agc.dto.Visa;
import ci.gouv.dgbf.agc.service.CompositionService;
import ci.gouv.dgbf.agc.service.ModeleVisaService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class CompositionBacking extends BaseBacking {
    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ModeleVisaService modeleVisaService;
    @Inject
    CompositionService compositionService;

    @Getter @Setter
    private List<Visa> visaList;
    @Getter @Setter
    private ModeleVisa modeleVisa;
    @Getter @Setter
    private List<Composition> compositionList;
    @Getter @Setter
    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if(params.containsKey("uuid")){
            modeleVisa = modeleVisaService.findById(params.get("uuid"));
            compositionList = compositionService.findByModele(params.get("uuid"));

            LOG.info(compositionList.toString());
        }
    }

    public void delete(String uuid){
        compositionService.delete(uuid);
    }


    public void handleReturn(SelectEvent event){
        compositionList = compositionService.findByModele(params.get("uuid"));
        if (event != null)
            showSuccess();
    }

    /*
    public void openCreateDialog(){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "95vw");
        options.replace("height", "95vh");
        PrimeFaces.current().dialog().openDynamic("modele-visa-create-dlg", options, null);
    }

    public void openUpdateDialog(String uuid){
        Map<String,Object> options = getLevelTwoDialogOptions();
        options.replace("height", "50vh");
        Map<String, List<String>> params = new HashMap<>();
        List<String> uuidList = new ArrayList<>();
        uuidList.add(uuid);
        params.put("uuid", uuidList);
        PrimeFaces.current().dialog().openDynamic("modele-visa-update-dlg", options, params);
    }

    public void delete(String uuid){
        try{
            modeleVisaService.delete(uuid);
            modeleVisaList = modeleVisaService.listAll();
            showSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

     */

}
