package ci.gouv.dgbf.agc.enumeration;

public enum TypeDemande {
    DEMANDE_VIREMENT("Demande de virement de crédits", "ACTE_MOUVEMENT"),
    DEMANDE_TRANSFERT("Demande de virement de crédits", "ACTE_MOUVEMENT"),
    DEMANDE_REPPORT_CREDIT("Demande de repport de crédits", "ACTE_REPORT"),
    DEMANDE_INTEGRATION_RESOURCE("Demande d'intégration de ressource", "ACTE_INTEGRATION_RESSOURCES"),
    DEMANDE_GELE_DEGELE("Demande de gèle ou dégèle de crédits", "ACTE_GELE_DEGELE"),
    DEMANDE_ANNULATION_CREDIT("Demande d'annulation de crédits", "ACTE_ANNULATION");

    public String categorieActe;
    public String libelle;

    TypeDemande(String categorieActe, String libelle) {
        this.categorieActe = categorieActe;
        this.libelle = libelle;
    }
}
