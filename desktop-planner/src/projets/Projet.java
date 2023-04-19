package projets;

import java.util.HashSet;
import java.util.Set;

import taches.Tache;

public class Projet {
    private String nomDuProjet;
    private String description;
    private Set<Tache> tasks;
    public Projet(String name, String description) {
        this.nomDuProjet = name;
        this.description = description;
        this.tasks = new HashSet<>();
    }
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
}
