package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Exercice;
import ci.gouv.dgbf.agc.service.ExerciceService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ExerciceBacking extends BaseBacking {

    private final Logger LOG = Logger.getLogger(this.getClass().getName());

    @Inject
    ExerciceService exerciceService;

    @Getter @Setter
    private Exercice exercice;

    @Getter @Setter
    private String annee;

    @PostConstruct
    public void init(){
        exercice = exerciceService.findCurrent();
    }

    public void persist(){
        exercice.setAnnee(annee);
        LOG.info(exercice.toString());
        exerciceService.persist(exercice);
        exercice = exerciceService.findCurrent();
        annee = "";
    }
}
