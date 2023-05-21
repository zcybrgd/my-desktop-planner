package com.example.demo.user;

import com.example.demo.enumerations.EtatTache;
import com.example.demo.planification.Tache;
import javafx.scene.control.ChoiceDialog;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class Projet implements Serializable {
    private String nom;
    private String description;
    private EtatTache stateDeTache= EtatTache.notRealized;
    private Set<Tache> ensembleDesTaches=new TreeSet<>();
    private int nbrDesTaches;

    public Set<Tache> getEnsembleDesTaches() {
        return ensembleDesTaches;
    }

    public void setEnsembleDesTaches(Set<Tache> ensembleDesTaches) {
        this.ensembleDesTaches = ensembleDesTaches;
    }

    public Projet(String nom, String desc){
        this.nom = nom;
        this.description = desc;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public EtatTache getStateDeTache() {
        return stateDeTache;
    }

    public void setStateDeTache(EtatTache stateDeTache) {
        this.stateDeTache = stateDeTache;
    }

    public String getDescription() {
        return description;
    }
    // modifierProjet() dans user : ajouter//supprimer tache;
    // void etatAvancement()
    public void evaluerProjet(){
        ChoiceDialog<EtatTache> choiceDialog = new ChoiceDialog<>(EtatTache.notRealized, EtatTache.values());
        choiceDialog.setTitle("Evaluation de la tache");
        choiceDialog.setHeaderText("évaluer la tache");
        choiceDialog.setContentText("Choisissez");

        // Show the choice dialog and get the selected evaluation
        Optional<EtatTache> result = choiceDialog.showAndWait();
        result.ifPresent(etatTache -> {
            System.out.println("Selected evaluation: " + etatTache);
            this.setStateDeTache(etatTache);
            // Perform the desired action with the selected evaluation
        });
    }
}
