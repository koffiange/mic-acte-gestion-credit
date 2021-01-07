package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.enumeration.StatutActe;
import ci.gouv.dgbf.agc.service.ActeService;
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
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ActeBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActeService acteService;

    @Getter @Setter
    private List<Acte> acteList;

    @Getter @Setter
    private List<Acte> selectedActeList;

    @PostConstruct
    public void init(){
        acteList = acteService.listAll();
    }

    public List<Acte> findByStatut(String s){
        StatutActe statut = StatutActe.valueOf(s);
        return acteList.stream().filter(acte -> acte.getStatutActe().equals(statut)).collect(Collectors.toList());
    }

    public void handleReturn(SelectEvent event){
        LOG.info("Handled!");
        acteList = acteService.listAll();
        if (event != null)
            showSuccess();
    }

    public void onRowSelect(SelectEvent<Acte> event){
        LOG.info("Row selected! Uuid : "+event.getObject().getUuid());
    }

    public void openCreateDialog(String dlg){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.put("closable", false);
        options.replace("height", "95vh");
        options.replace("width", "95vw");
        PrimeFaces.current().dialog().openDynamic(dlg, options, null);
    }

    public void openUpdateDialog(String uuid){
        Map<String,Object> options = getLevelTwoDialogOptions();
        options.replace("height", "95vh");
        options.replace("width", "95vh");
        Map<String, List<String>> params = new HashMap<>();
        List<String> uuidList = new ArrayList<>();
        uuidList.add(uuid);
        params.put("uuid", uuidList);
        PrimeFaces.current().dialog().openDynamic("acte-update-dlg", options, params);
    }

    public void delete(String uuid){
        try{
            acteService.delete(uuid);
            acteList = acteService.listAll();
            showSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    public void appliquer(String uuid){
        acteService.appliquer(uuid);
        acteList = acteService.listAll();
        showSuccess();
    }

    public void appliquerPlusieur(){
        List<String> uuidList = selectedActeList.stream().map(Acte::getUuid).collect(Collectors.toList());
        acteService.appliquerPlusieur(uuidList);
        acteList = acteService.listAll();
        showSuccess();
    }

}
