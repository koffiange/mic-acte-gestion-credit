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

@Named
@ViewScoped
public class VisaCreateBacking extends BaseBacking {
    @Inject
    VisaService visaService;

    @Getter @Setter
    private Visa visa;

    @PostConstruct
    public void init(){
        visa = new Visa();
    }

    public void persist(){
        try{
            visaService.persist(visa);
            closeSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }

    }

}
