package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import org.primefaces.PrimeFaces;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class DemandeBacking extends BaseBacking {
    /*
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
     */
}
