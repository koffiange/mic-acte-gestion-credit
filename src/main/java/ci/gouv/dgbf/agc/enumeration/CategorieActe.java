package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum CategorieActe {

    LOI_FINANCES_INITIALE("01", "Loi de finances initiale", "LOI", "BUDGETISATION", "LEGISLATIF"),
    LOI_FINANCES_RECTIFICATIVE("02","Loi de finances rectificative","LOI", "BUDGETISATION","LEGISLATIF"),
    LOI_FINANCES_PORTANT_DOUZIEME_PROVISOIRE("03","finances portant douzième provisoire","LOI", "BUDGETISATION","LEGISLATIF"),
    LOI_REGLEMENT("04", "Loi de règlement", "LOI", "BUDGETISATION","LEGISLATIF"),
    ORDONNANCE("05", "Odonnance", "ORDONNANCE", "BUDGETISATION","LEGISLATIF"),
    DECRET_AVANCE("06", "Décret d'avances", "DECRET", "GESTION","REGLEMENTAIRE"),
    DECRET_PORTANT_REPORT_CREDITS("07", "Decret portant report de credits", "DECRET", "GESTION","REGLEMENTAIRE"),
    DECRET_MODIFIANT_REPARTITION_CREDITS("08", "Decret modifiant la repartition des credits", "DECRET", "GESTION","REGLEMENTAIRE"),
    DECRET_PORTANT_GELE_DEGELE_CREDITS("09", "Decret portant Gel ou Dégel des crédits", "DECRET", "GESTION","REGLEMENTAIRE"),
    DECRET_PORTANT_ANNULATION_RETABLISSEMENT_CREDITS("10", "Decret portant annulation ou rétablissement des crédits", "DECRET", "GESTION","REGLEMENTAIRE"),
    DECRET_PORTANT_INTEGRATION_RESSOURCES("11", "Decret portant intégration de ressource", "DECRET", "GESTION","REGLEMENTAIRE"),
    DECRET("12", "Décret", "DECRET", "GESTION","REGLEMENTAIRE"),
    ARRETE_INTERMINISTERIEL_MODIFIANT_REPARTITION_CREDITS("13", "Arrêté interministériel  modifiant la repartition des crédits", "ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_INTERMINISTERIEL_PORTANT_REPORT_CREDITS("14", "Arrêté interministériel portant  report de crédits", "ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_INTERMINISTERIEL_PORTANT_INTEGRATION_RESSOURCES("15", "Arrêté interministériel portant  intégration de ressource","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_MODIFIANT_REPARTITION_CREDIT("16", "Arrêté modifiant la repartition des crédits","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_PORTANT_INTEGRATION_RESSOURCES("17", "Arrêté portant  intégration de ressource","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_PORTANT_REPORT_CREDITS("18", "Arrêté portant report de crédits","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_PORTANT_GELE_DEGELE_CREDITS("19", "Arrêté portant Gel ou Dégel des crédits","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE_PORTANT_ANNULATION_RETABLISSEMENT_CREDITS("20", "Arrêté portant annulation ou rétablissement des crédits","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    ARRETE("21", "Arrêté","ARRETE INTERMINISTERIEL", "GESTION","REGLEMENTAIRE"),
    DECISION_DGBF_MODIFIANT_REPARTITION_CREDITS("22", "Décision du DGBF modifiant la repartition des crédits","DECISION", "GESTION","REGLEMENTAIRE"),
    DECISION_RESPONSABLE_PROGRAMME_PORTANT_VIREMENT_CREDITS("23", "Décision du responsable de programme portant virement des credits","DECISION", "GESTION","REGLEMENTAIRE"),
    ACTE_REGLEMENTAIRE_OUVERTURE_CREDIT("24", "Acte reglementaire d'ouverture de crédits","AUTRE", "GESTION","REGLEMENTAIRE"),
    ACTE_REGLEMENTAIRE_REDUCTION_CREDIT("25", "Acte reglementaire de reduction de crédits","AUTRE", "GESTION","REGLEMENTAIRE"),
    ACTE_OUVERTURE_AE_COMPLEMENTAIRE("26", "Ouverture d'AE Complémentaire","AUTRE", "GESTION","REGLEMENTAIRE");

    @Getter @Setter
    private String code;
    @Getter @Setter
    private String designation;
    @Getter @Setter
    private String natureActe;
    @Getter @Setter
    private String usageType;
    @Getter @Setter
    private String acteType;

    CategorieActe(String code, String designation, String natureActe, String usageType, String acteType) {
        this.code = code;
        this.designation = designation;
        this.natureActe = natureActe;
        this.usageType = usageType;
        this.acteType = acteType;
    }

}
