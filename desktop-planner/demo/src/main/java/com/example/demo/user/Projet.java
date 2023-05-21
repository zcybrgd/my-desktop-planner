package com.example.demo.user;

import com.example.demo.enumerations.EtatTache;
import com.example.demo.planification.Tache;

import java.io.Serializable;
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

    public String getNom() {
        return nom;
    }

    public EtatTache getStateDeTache() {
        return stateDeTache;
    }


    public String getDescription() {
        return description;
    }
    // modifierProjet() dans user : ajouter//supprimer tache;
    // void etatAvancement()
}
