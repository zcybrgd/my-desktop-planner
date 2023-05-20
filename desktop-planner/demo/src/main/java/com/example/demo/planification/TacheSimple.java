package com.example.demo.planification;


import com.example.demo.enumerations.Prio;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public class TacheSimple extends Tache implements Serializable {
    private boolean periodicite;
    // ces tachesSimples peuvent être périodiques (planifiées tous les n jours).
    //si n = 0 , la tâche n'est planifiée qu'une fois.
    private int nbrJourDePeriodicite;
    private Creneau creneauDeTache;
    public TacheSimple(String nom, Duration duree, Prio priorite, LocalDate deadline, Categorie categorie, int nbrJourDePeriodicite){
        super(nom, priorite, deadline, categorie);
        this.nbrJourDePeriodicite = nbrJourDePeriodicite;
    }
    public TacheSimple(Duration duree, Creneau creneauDeTache){
        super(duree);
        this.creneauDeTache = creneauDeTache;
    }
    // dans un seul créneau
    void planifierTache(){

    }
    void replanifierTache(){

    }
    void evaluerTache(){

    }

    public void setCreneauDeTache(Creneau creneauDeTache) {
        this.creneauDeTache = creneauDeTache;
    }

    public void setPeriodicite(boolean periodicite) {
        this.periodicite = periodicite;
    }
}
