package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.ModeleVisa;
import ci.gouv.dgbf.agc.dto.ModeleVisaDto;
import ci.gouv.dgbf.agc.dto.Visa;
import ci.gouv.dgbf.agc.dto.VisaDto;
import ci.gouv.dgbf.agc.service.ModeleVisaService;
import ci.gouv.dgbf.agc.service.VisaService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ModeleVisaUpdateBacking extends BaseBacking {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ModeleVisaService modeleVisaService;

    @Inject
    VisaService visaService;

    private Map<String, String> params;

    @Getter @Setter
    private ModeleVisa modeleVisa;

    @Getter @Setter
    private List<Visa> visaList;

    @Getter @Setter
    private List<Visa> selectedVisaList = new ArrayList<>();

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("uuid")){
            modeleVisa = modeleVisaService.findById(params.get("uuid"));
        } else {
            modeleVisa = new ModeleVisa();
        }
    }

    public void persist(){
        List<VisaDto> visaDtoList = new ArrayList<>();
        for (int i = 0; i < visaList.size(); i++){
            visaDtoList.add(new VisaDto(visaList.get(i), i));
        }

        try {
            modeleVisaService.update(new ModeleVisaDto(modeleVisa, visaDtoList));
            closeSuccess();
        } catch (Exception e){
            LOG.info(e.getMessage());
            showError();
        }

    }
}
