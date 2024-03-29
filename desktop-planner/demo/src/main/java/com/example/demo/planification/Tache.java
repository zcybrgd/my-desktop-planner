package com.example.demo.planification;



import com.example.demo.enumerations.EtatTache;
import com.example.demo.enumerations.Prio;
import com.example.demo.user.Jour;
import com.example.demo.user.User;


import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public abstract class Tache implements Serializable {
    protected String nom;
    protected Duration duree;
    protected Prio priorite;
    protected LocalDate deadline;
    protected Categorie categorie;
    protected EtatTache stateDeTache=EtatTache.notRealized;
    protected Set<Jour> journees = new TreeSet<>();
    public Tache(Duration duree){
        this.duree = duree;
    }
    public Tache(String nom, Prio priorite, LocalDate deadline, Categorie categorie){
        this.nom = nom;
        this.priorite = priorite;
        this.deadline = deadline;
        this.categorie = categorie;
    }

    public Set<Jour> getJournees() {
        return journees;
    }

    public String getNom() {
        return nom;
    }

    public Duration getDuree() {
        return duree;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public EtatTache getStateDeTache() {
        return stateDeTache;
    }

    public Prio getPriorite() {
        return priorite;
    }

    public Tache(){}
    public void setJournees(Set<Jour> journees) {
        this.journees = journees;
    }


    // dans le cas ou la tache est inProgress ou notRealized
    // le système lui demande la durée supplémentaire nécessaire pour l'accomplir,
    // et le nouveau deadline si jamais elle en possède un.
    abstract void replanifierTache(User user);
    abstract void evaluerTache(User user);

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
