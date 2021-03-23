package ci.gouv.dgbf.agc.enumeration;

public enum StatutOperation {
    EN_ATTENTE("En attente d'application"),
    APPLIQUE("Acte appliqué"),
    APPLICATION_ECHOUE("Application échoué pour défaut de crédit");

    public String libelle;

    StatutOperation(String libelle) {
        this.libelle = libelle;
    }
}
