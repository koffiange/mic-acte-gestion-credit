package ci.gouv.dgbf.agc.dto;


import ci.gouv.dgbf.agc.enumeration.CategorieActe;
import ci.gouv.dgbf.agc.enumeration.NatureActe;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Acte{
    @Getter @Setter
    private String corpus;
    @Getter @Setter
    private CategorieActe categorieActe;
    @Getter @Setter
    private NatureActe natureActe;
    @Getter @Setter
    private String reference;
    @Getter @Setter
    private LocalDate dateSignature;
    @Getter @Setter
    private Demande demande;

    public Acte() {
    }
}
