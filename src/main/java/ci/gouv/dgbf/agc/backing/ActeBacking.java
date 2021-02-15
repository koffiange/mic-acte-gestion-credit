package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.enumeration.StatutActe;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
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
    private List<Acte> filteredActeList;

    @Getter @Setter
    private List<Acte> selectedActeList;

    @Getter @Setter
    private boolean displayApplicationMultipleBtn = true;

    @PostConstruct
    public void init(){
        acteList = acteService.listAll();
    }

    public List<Acte> findByStatut(String s){
        StatutActe statut = StatutActe.valueOf(s);
        return acteList.stream()
                .filter(acte -> acte.getStatutActe().equals(statut))
                .sorted(Comparator.comparing(Acte::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    public void handleReturn(SelectEvent event){
        LOG.info("Handled!");
        acteList = acteService.listAll();
        if (event != null)
            showSuccess();
    }

    public void onRowSelect(SelectEvent<Acte> event){
        displayApplicationMultipleBtn = selectedActeList.isEmpty();
    }

    public void onRowUnSelect(UnselectEvent<Acte> event){
        displayApplicationMultipleBtn = selectedActeList.isEmpty();
    }

    public void openCreateDialog(String dlg){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.put("closable", false);
        options.replace("height", "95vh");
        options.replace("width", "95vw");
        PrimeFaces.current().dialog().openDynamic(dlg, options, null);
    }

    public void openUpdateDialog(Acte acte){
        Map<String,Object> options = getLevelTwoDialogOptions();
        options.put("closable", false);
        options.replace("height", "95vh");
        options.replace("width", "95vw");

        Map<String, List<String>> params = new HashMap<>();
        List<String> uuidList = new ArrayList<>();
        uuidList.add(acte.getUuid());
        params.put("uuid", uuidList);

        String dlg = this.determineUpdateDialogName(acte);
        PrimeFaces.current().dialog().openDynamic(dlg, options, params);
    }

    private String determineUpdateDialogName(Acte acte){
        switch (acte.getCategorieActe()){
            case ACTE_MOUVEMENT: return "acte-mouvement-credit-update-dlg";
            case ACTE_GELE_DEGELE: return "gele-credit-update-dlg";
            case ACTE_ANNULATION: return "annulation-credit-update-dlg";
            case ACTE_INTEGRATION_RESSOURCES: return "integration-ressource-update-dlg";
            case ACTE_REPORT: return "acte-report-credit-update-dlg";
            default: return "";
        }
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
