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
import java.util.logging.Logger;

@Named
@ViewScoped
public class ModeleVisaCreateBacking extends BaseBacking {

    Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ModeleVisaService modeleVisaService;

    @Inject
    VisaService visaService;

    @Getter @Setter
    private ModeleVisa modeleVisa;
    @Getter @Setter
    private List<Visa> visaList;
    @Getter @Setter
    private List<Visa> filteredVisaList;
    @Getter @Setter
    private List<Visa> selectedVisaList;

    @PostConstruct
    public void init(){
        modeleVisa = new ModeleVisa();
        visaList = visaService.listAll();
    }

    public void persist(){
        List<VisaDto> visaDtoList = new ArrayList<>();
        for (int i = 0; i < selectedVisaList.size(); i++){
            visaDtoList.add(new VisaDto(selectedVisaList.get(i), i));
        }

        try {
            modeleVisaService.persist(new ModeleVisaDto(modeleVisa, visaDtoList));
            closeSuccess();
        } catch (Exception e){
            LOG.info(e.getMessage());
            showError();
        }

    }
}
