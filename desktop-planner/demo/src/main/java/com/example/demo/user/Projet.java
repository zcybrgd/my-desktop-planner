package com.example.demo.user;

import com.example.demo.planification.Tache;

import java.io.Serializable;
import java.util.List;

public class Projet implements Serializable {
    private String nom;
    private String description;
    private List<Tache> ensembleDesTaches;
    private int nbrDesTaches;
    public Projet(String nom, String desc, List<Tache> ens){
        this.nom = nom;
        this.description = desc;
        this.ensembleDesTaches = ens;
    }
    // void ordonnerLesTaches() ordonner l'ensemble des taches entrées
    // modifierProjet() dans user : ajouter//supprimer tache;
    // void etatAvancement()
}
