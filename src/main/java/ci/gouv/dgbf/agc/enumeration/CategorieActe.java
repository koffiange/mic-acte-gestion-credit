package ci.gouv.dgbf.agc.enumeration;

import lombok.Getter;
import lombok.Setter;

public enum CategorieActe {
    ACTE_MOUVEMENT("ACT_MOUV_CRED", "Acte de mouvement de crédit", "Acte de déplacement des crédits d'une ligne à une autre; le sole est toujours vrai"),
    ACTE_REPORT("ACT_REP_CRED", "Acte de repport de crédit", "Acte par lequel on affecte des crédits d'une exercice 'A' à un exercice 'B' : il augment le budget de l'exercice 'B'"),
    ACTE_INTEGRATION_RESSOURCES("ACT_INT_RES", "Acte d'intégration de resource", "Acte par lequel on précise de nouvelles recette et de nouvelles dépenses"),
    ACTE_GELE_DEGELE("ACT_GELE/DEGELE_CRED", "Acte portant gèle et dégèle de crédits", "Acte par lequel l'on ferme ou ouvre des crédits à l'exécussion"),
    ACTE_ANNULATION("ACT_ANN_CRED", "Acte portant annulation de crédit", "Acte par lequel on retire des credits non affectés"),
    ACTE_BUDGET_INITIAL("BUD_INI", "Budget Initial", "Budget Initial");

    @Getter @Setter
    private String code;
    @Getter @Setter
    private String libelle;
    @Getter @Setter
    private String description;

    CategorieActe(String code, String libelle, String description) {
        this.code = code;
        this.libelle = libelle;
        this.description = description;
    }
}
