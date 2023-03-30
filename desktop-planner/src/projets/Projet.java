package projets;

import taches.Tache;

public class Projet {
    private String nomDuProjet;
    private String description;
    private Tache[] taches; // l'ensemble des taches de ce projet
    public void setNomDuProjet(String nomDuProjet) {
        this.nomDuProjet = nomDuProjet;
    }
    public String getNomDuProjet() {
        return nomDuProjet;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setTaches(Tache[] taches) {
        this.taches = taches;
    }
    public Tache[] getTaches() {
        return taches;
    }
}
