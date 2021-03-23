package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.dto.OperationBag;
import ci.gouv.dgbf.agc.enumeration.StatutOperation;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.OperationService;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ActeBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ActeService acteService;

    @Inject
    OperationService operationService;

    private List<OperationBag> operationBagList;

    @Getter @Setter
    private List<Acte> filteredActeList;

    @Getter @Setter
    private List<Acte> selectedActeList;

    @Getter @Setter
    private boolean displayApplicationMultipleBtn = true;

    @PostConstruct
    public void init(){
        operationBagList = operationService.listAll();
    }

    public List<OperationBag> findByStatut(String s){
        StatutOperation statut = StatutOperation.valueOf(s);
        return operationBagList.stream()
                .filter(operationBag -> operationBag.getOperation().getStatutOperation().equals(statut))
                .sorted(Comparator.comparing(OperationBag::getOperation, (o1, o2) -> {
                    return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                }))
                .collect(Collectors.toList());
    }

    public void handleReturn(SelectEvent event){
        LOG.info("Handled!");
        this.init();
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
            operationService.delete(uuid);
            this.init();
            showSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    public void appliquer(String uuid){
        try {
            AtomicReference<OperationBag> operationBag = null;
            operationBagList.stream().filter(ob -> ob.getOperation().getUuid().equals(uuid)).findFirst().ifPresent(ob1 -> operationBag.set(ob1));
            operationService.appliquer(operationBag.get());
            this.init();
            showSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }


    public void appliquerPlusieur(){
        /*
        List<String> uuidList = selectedActeList.stream().map(Acte::getUuid).collect(Collectors.toList());
        acteService.appliquerPlusieur(uuidList);
        acteList = acteService.listAll();
        showSuccess();
        */
    }


}
