package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.dto.OperationBag;
import ci.gouv.dgbf.agc.enumeration.CategorieActe;
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

    @Getter @Setter
    private CategorieActe newActeCatégorie;

    @Getter @Setter
    private List<CategorieActe> categorieActeList;

    @PostConstruct
    public void init(){
        operationBagList = operationService.listAll();
        categorieActeList = Arrays.asList(CategorieActe.values())
                            .stream().filter(categorieActe -> categorieActe.getUsageType().equals("GESTION"))
                            .collect(Collectors.toList());
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
        this.init();
        newActeCatégorie = null;
        showSuccess();
    }

    public void onRowSelect(SelectEvent<Acte> event){
        displayApplicationMultipleBtn = selectedActeList.isEmpty();
    }

    public void onRowUnSelect(UnselectEvent<Acte> event){
        displayApplicationMultipleBtn = selectedActeList.isEmpty();
    }

    public void openCreateDialog(){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.put("closable", false);
        options.replace("height", "95vh");
        options.replace("width", "95vw");

        Map<String, List<String>> params = new HashMap<>();
        List<String> categorieCodeList = new ArrayList<>();
        categorieCodeList.add(newActeCatégorie.name());
        params.put("categorieActeCode", categorieCodeList);
        PrimeFaces.current().dialog().openDynamic(determineCreateDialogName(newActeCatégorie), options, params);
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

        // String dlg = this.determineUpdateDialogName(acte);
        PrimeFaces.current().dialog().openDynamic(null, options, params);
    }

    public void openAppliquerDialog(String uuid){
        Map<String,Object> options = getLevelTwoDialogOptions();
        options.put("closable", true);

        Map<String, List<String>> params = new HashMap<>();
        List<String> operationUuidList = new ArrayList<>();
        operationUuidList.add(uuid);
        params.put("uuid", operationUuidList);
        PrimeFaces.current().dialog().openDynamic("acte-application-dlg", options, params);
    }

    private String determineCreateDialogName(CategorieActe ca){
        switch (ca) {
            case ORDONNANCE:
                return "acte-mouvement-credit-create-dlg";
            case DECRET_AVANCE:
                return "acte-mouvement-credit-create-dlg";
            case DECRET_PORTANT_REPORT_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case DECRET_MODIFIANT_REPARTITION_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case DECRET_PORTANT_GELE_DEGELE_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case DECRET_PORTANT_ANNULATION_RETABLISSEMENT_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case DECRET_PORTANT_INTEGRATION_RESSOURCES:
                return "acte-mouvement-credit-create-dlg";
            case DECRET:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_INTERMINISTERIEL_MODIFIANT_REPARTITION_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_INTERMINISTERIEL_PORTANT_REPORT_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_INTERMINISTERIEL_PORTANT_INTEGRATION_RESSOURCES:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_MODIFIANT_REPARTITION_CREDIT:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_PORTANT_INTEGRATION_RESSOURCES:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_PORTANT_REPORT_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_PORTANT_GELE_DEGELE_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE_PORTANT_ANNULATION_RETABLISSEMENT_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case ARRETE:
                return "acte-mouvement-credit-create-dlg";
            case DECISION_DGBF_MODIFIANT_REPARTITION_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case DECISION_RESPONSABLE_PROGRAMME_PORTANT_VIREMENT_CREDITS:
                return "acte-mouvement-credit-create-dlg";
            case ACTE_REGLEMENTAIRE_OUVERTURE_CREDIT:
                return "acte-mouvement-credit-create-dlg";
            case ACTE_REGLEMENTAIRE_REDUCTION_CREDIT:
                return "acte-mouvement-credit-create-dlg";
            case ACTE_OUVERTURE_AE_COMPLEMENTAIRE:
                return "acte-mouvement-credit-create-dlg";
            default:
                return "";
        }
    }

    private String determineUpdateDialogName(CategorieActe ca){
        switch (ca) {
            case ORDONNANCE:
                return "acte-mouvement-credit-update-dlg";
            case DECRET_AVANCE:
                return "acte-mouvement-credit-update-dlg";
            case DECRET_PORTANT_REPORT_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case DECRET_MODIFIANT_REPARTITION_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case DECRET_PORTANT_GELE_DEGELE_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case DECRET_PORTANT_ANNULATION_RETABLISSEMENT_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case DECRET_PORTANT_INTEGRATION_RESSOURCES:
                return "acte-mouvement-credit-update-dlg";
            case DECRET:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_INTERMINISTERIEL_MODIFIANT_REPARTITION_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_INTERMINISTERIEL_PORTANT_REPORT_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_INTERMINISTERIEL_PORTANT_INTEGRATION_RESSOURCES:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_MODIFIANT_REPARTITION_CREDIT:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_PORTANT_INTEGRATION_RESSOURCES:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_PORTANT_REPORT_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_PORTANT_GELE_DEGELE_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE_PORTANT_ANNULATION_RETABLISSEMENT_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case ARRETE:
                return "acte-mouvement-credit-update-dlg";
            case DECISION_DGBF_MODIFIANT_REPARTITION_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case DECISION_RESPONSABLE_PROGRAMME_PORTANT_VIREMENT_CREDITS:
                return "acte-mouvement-credit-update-dlg";
            case ACTE_REGLEMENTAIRE_OUVERTURE_CREDIT:
                return "acte-mouvement-credit-update-dlg";
            case ACTE_REGLEMENTAIRE_REDUCTION_CREDIT:
                return "acte-mouvement-credit-update-dlg";
            case ACTE_OUVERTURE_AE_COMPLEMENTAIRE:
                return "acte-mouvement-credit-update-dlg";
            default:
                return "";
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
