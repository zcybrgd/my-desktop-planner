package com.example.demo.user;

import com.example.demo.planification.Tache;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Projet implements Serializable {
    private String nom;
    private String description;
    private Set<Tache> ensembleDesTaches;
    private int nbrDesTaches;
    public Projet(String nom, String desc, Set<Tache> ens){
        this.nom = nom;
        this.description = desc;
        this.ensembleDesTaches = ens;
    }
    // modifierProjet() dans user : ajouter//supprimer tache;
    // void etatAvancement()
}
