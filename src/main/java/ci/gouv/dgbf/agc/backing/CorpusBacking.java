package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.Map;

@Named
@ViewScoped
public class CorpusBacking extends BaseBacking {

    @Getter @Setter
    private String corpus;

    private Map<String, String> params;

    @PostConstruct
    public void init(){
        params = getRequestParameterMap();
        if (params.containsKey("corpus")){
            corpus = params.get("corpus");
        }
    }

    public void persist(){
        closeSuccess(corpus);
    }
}
