package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.Acte;
import ci.gouv.dgbf.agc.dto.ActeDto;
import ci.gouv.dgbf.agc.dto.Operation;
import ci.gouv.dgbf.agc.dto.Signataire;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named(value = "amcCreateBacking")
@ViewScoped
public class ActeMouvementCreditCreateBacking extends BaseBacking {
    @Inject
    ActeService acteService;

    @Getter @Setter
    List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    List<Operation> operationList = new ArrayList<>();

    @Getter @Setter
    private ActeDto acteDto;

    @Getter @Setter
    private Acte acte;

    @Getter @Setter
    private Date date;

    @Getter @Setter
    private Signataire signataire;

    @PostConstruct
    public void init(){
        acte = new Acte();
        signataire = new Signataire();
        acteDto = new ActeDto();
    }

    public void addSignataire(){
        signataireList.add(signataire);
        signataire = new Signataire();
    }

    public void addSignataire(String s){
        signataireList.remove(s);
    }

    private void buildActeDto(){
        acte.setDateSignature(convertIntoLocaleDate(date));
    }

    public void persist(){
        try{
            acteService.persist(acteDto);
            closeSuccess();
        } catch (Exception e){
            showError(e.getMessage());
        }

    }

    public void openLigneDepenseDialog(){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "90vw");
        options.replace("height", "90vh");
        PrimeFaces.current().dialog().openDynamic("rechercher-source-financement-dlg", options, null);
    }
}
