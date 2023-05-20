package com.example.demo.planification;

import com.example.demo.enumerations.Prio;
import com.example.demo.user.User;


import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class TacheDecomposable extends Tache implements Decomposable, Serializable {
    public TacheDecomposable(String nom, Duration duree, Prio priorite, LocalDate deadline, Categorie categorie){
        super(nom, priorite, deadline, categorie);
    }
    public TacheDecomposable(Duration duree){
        super(duree);
    }
    private List<Tache> sousTaches;
    // Ses sous-tâches auront le même nom que la tâche décomposée auquel sera concaténée le numéro de la sous tâche.
    void changerNom(){}
    void planifierTache(User user){
   // Une tâche décomposable peut être planifiée en plusieurs créneaux jusqu'à atteindre la durée prévue.
    }
    void replanifierTache(){

    }
    void evaluerTache(){

    }
    void decomposer(){}
}
