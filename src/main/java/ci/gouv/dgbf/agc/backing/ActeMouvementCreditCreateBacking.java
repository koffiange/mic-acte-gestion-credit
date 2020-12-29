package ci.gouv.dgbf.agc.backing;

import ci.gouv.dgbf.agc.dto.*;
import ci.gouv.dgbf.agc.service.ActeService;
import ci.gouv.dgbf.agc.service.SectionService;
import ci.gouv.dgbf.appmodele.backing.BaseBacking;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named(value = "amcCreateBacking")
@ViewScoped
public class ActeMouvementCreditCreateBacking extends BaseBacking {
    @Inject
    ActeService acteService;

    @Inject
    SectionService sectionService;

    @Getter @Setter
    List<Signataire> signataireList = new ArrayList<>();

    @Getter @Setter
    List<Operation> operationList = new ArrayList<>();

    @Getter @Setter
    List<Section> sectionList;

    @Getter @Setter
    private ActeDto acteDto;

    @Getter @Setter
    private Acte acte;

    @Getter @Setter
    private Date date;

    @Getter @Setter
    private Signataire signataire;

    @Getter @Setter
    private Section selectedSection;

    @PostConstruct
    public void init(){
        acte = new Acte();
        signataire = new Signataire();
        acteDto = new ActeDto();
        sectionList = sectionService.list();
    }

    public void addSignataire(){
        signataireList.add(signataire);
        signataire = new Signataire();
    }

    public void deleteSignataire(Signataire signataire){
        signataireList.remove(signataire);
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

    public void openLigneDepenseDialog(String typeImputation){
        Map<String,Object> options = getLevelOneDialogOptions();
        options.replace("width", "90vw");
        options.replace("height", "90vh");
        Map<String, List<String>> params = new HashMap<>();

        List<String> sectionList = new ArrayList<>();
        sectionList.add(selectedSection.getCode());
        params.put("sectionCode", sectionList);

        List<String> natureTransactionList = new ArrayList<>();
        natureTransactionList.add(acte.getNatureTransaction().toString());
        params.put("natureTransaction", natureTransactionList);

        List<String> typeImputationList = new ArrayList<>();
        typeImputationList.add(typeImputation);
        params.put("typeImputation", typeImputationList);

        PrimeFaces.current().dialog().openDynamic("rechercher-source-financement-dlg", options, params);
    }
}
