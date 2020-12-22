package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Visa;
import ci.gouv.dgbf.agc.service.VisaService;
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

@Named
@ViewScoped
public class VisaBacking extends BaseBacking {
    @Inject
    VisaService visaService;

    @Getter @Setter
    private List<Visa> visaList;

    @PostConstruct
    public void init(){
        visaList = visaService.listAll();
    }

    public void handleReturn(SelectEvent event){
        visaList = visaService.listAll();
        if (event != null)
            showSuccess();
    }

    public void openCreateDialog(){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("height", "50vh");
        PrimeFaces.current().dialog().openDynamic("visa-create-dlg", options, null);
    }

    public void openUpdateDialog(String uuid){
        Map<String,Object> options = getLevelTwoDialogOptions();
        options.replace("height", "50vh");
        Map<String, List<String>> params = new HashMap<>();
        List<String> uuidList = new ArrayList<>();
        uuidList.add(uuid);
        params.put("uuid", uuidList);
        PrimeFaces.current().dialog().openDynamic("visa-update-dlg", options, params);
    }

    public void delete(String uuid){
        try{
            visaService.delete(uuid);
            visaList = visaService.listAll();
            showSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }
}
