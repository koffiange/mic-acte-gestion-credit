package ci.gouv.dgbf.agc.enumeration;

public enum TypeOperation {
    ORIGINE("Opération d'origine"),
    DESTINATION("Opération de destination");

    public String libelle;

    TypeOperation(String libelle) {
        this.libelle = libelle;
    }
}
