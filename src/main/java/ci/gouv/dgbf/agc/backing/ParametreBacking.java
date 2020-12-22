package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.enumeration.CategorieActe;
import ci.gouv.dgbf.agc.enumeration.NatureActe;
import ci.gouv.dgbf.agc.enumeration.TypeActe;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class ParametreBacking extends BaseBacking {
    @Getter @Setter
    private List<TypeActe> typeActeList;
    @Getter @Setter
    private List<CategorieActe> categorieActeList;
    @Getter @Setter
    private List<NatureActe> natureActeList;

    @PostConstruct
    public void init(){
        typeActeList = Arrays.asList(TypeActe.values());
        categorieActeList = Arrays.asList(CategorieActe.values());
        natureActeList = Arrays.asList(NatureActe.values());
    }
}
