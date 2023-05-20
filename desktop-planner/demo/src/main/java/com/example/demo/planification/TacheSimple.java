package com.example.demo.planification;


import com.example.demo.enumerations.Prio;
import com.example.demo.user.Jour;
import com.example.demo.user.User;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public class TacheSimple extends Tache implements Serializable, Comparable<TacheSimple> {

    @Override
    public int compareTo(TacheSimple other) {
        Jour thisJour = this.getJournees().iterator().next();
        Jour otherJour = other.getJournees().iterator().next();

        int jourComparison = thisJour.compareTo(otherJour);
        if (jourComparison != 0) {
            return jourComparison;
        } else {
            return this.getCreneauDeTache().compareTo(other.getCreneauDeTache());
        }
    }
    public void setNbrJourDePeriodicite(int nbrJourDePeriodicite) {
        this.nbrJourDePeriodicite = nbrJourDePeriodicite;
    }

    // ces tachesSimples peuvent être périodiques (planifiées tous les n jours).
    //si n = 0 , la tâche n'est planifiée qu'une fois.
    private int nbrJourDePeriodicite;
    private Creneau creneauDeTache;
    private Jour journee;

    public Creneau getCreneauDeTache() {
        return creneauDeTache;
    }

    public TacheSimple(String nom, Duration duree, Prio priorite, LocalDate deadline, Categorie categorie, int nbrJourDePeriodicite){
        super(nom, priorite, deadline, categorie);
        this.nbrJourDePeriodicite = nbrJourDePeriodicite;
    }
    public TacheSimple(Duration duree, Creneau creneauDeTache, Jour journee){
        super(duree);
        this.creneauDeTache = creneauDeTache;
        this.journee = journee;
        super.getJournees().add(journee);
    }
    // dans un seul créneau
    public void planifierTache(User user){
        user.getPlanning().getTachesaPlanifier().add(this);
    }
    void replanifierTache(){

    }
    void evaluerTache(){

    }

    public void setCreneauDeTache(Creneau creneauDeTache) {
        this.creneauDeTache = creneauDeTache;
    }

}
