package com.example.demo.planification;



import com.example.demo.enumerations.EtatTache;
import com.example.demo.enumerations.Prio;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public abstract class Tache implements Serializable {
    private String nom;
    private Duration duree;
    private Prio priorite;
    private LocalDate deadline;
    private Categorie categorie;
    private EtatTache stateDeTache=EtatTache.notRealized;

    abstract void planifierTache();
    // dans le cas ou la tache est inProgress ou notRealized
    // le système lui demande la durée supplémentaire nécessaire pour l'accomplir,
    // et le nouveau deadline si jamais elle en possède un.
    abstract void replanifierTache();
    abstract void evaluerTache();
    public Tache(Duration duree){
        this.duree = duree;
    }
    public Tache(String nom, Prio priorite, LocalDate deadline, Categorie categorie){
      this.nom = nom;
      this.priorite = priorite;
      this.deadline = deadline;
      this.categorie = categorie;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDuree(Duration duree) {
        this.duree = duree;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setPriorite(Prio priorite) {
        this.priorite = priorite;
    }

    public void setStateDeTache(EtatTache stateDeTache) {
        this.stateDeTache = stateDeTache;
    }
}
