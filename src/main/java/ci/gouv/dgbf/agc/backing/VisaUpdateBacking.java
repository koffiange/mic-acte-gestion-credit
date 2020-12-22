package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Visa;
import ci.gouv.dgbf.agc.service.VisaService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
@ViewScoped
public class VisaUpdateBacking extends BaseBacking {
    @Inject
    VisaService visaService;

    @Getter @Setter
    private Visa visa;

    @Getter @Setter
    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            visa = visaService.findById(params.get("uuid"));
        } else{
            visa = new Visa();
        }
    }

    public void persist(){
        try{
            visaService.update(visa);
            closeSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }
    }
}
