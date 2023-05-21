package com.example.demo.planification;

import com.example.demo.enumerations.Prio;
import com.example.demo.user.Jour;
import com.example.demo.user.User;


import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

public class TacheSimple extends Tache implements Serializable, Comparable<TacheSimple>, Cloneable {

    public TacheSimple() {
        // Default constructor required for deserialization
    }



    @Override
    public int compareTo(TacheSimple other) {
        Set<Jour> thisJournees = this.getJournees();
        Set<Jour> otherJournees = other.getJournees();

        if (thisJournees.isEmpty() && otherJournees.isEmpty()) {
            return 0;
        } else if (thisJournees.isEmpty()) {
            return -1;
        } else if (otherJournees.isEmpty()) {
            return 1;
        }

        Jour thisJour = thisJournees.iterator().next();
        Jour otherJour = otherJournees.iterator().next();

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

    public void setJournee(Jour journee) {
        this.journee = journee;
    }

    public Jour getJournee() {
        return journee;
    }

    // dans un seul créneau
    public void planifierTache(User user){
        user.getPlanning().getTachesaPlanifier().add(this);
        if(nbrJourDePeriodicite>0){
            Jour jour = new Jour(this.journee.getDateDuJour());
            while(jour.comparerDates(jour.getDateDuJour(),user.getPlanning().getDateFin())<=0){
                try{
                    System.out.println("Jour de périodicité: " + jour.getDateDuJour().toString());
                    TacheSimple tachePeriodique = (TacheSimple) this.clone();
                    tachePeriodique.setJournee(jour);
                    System.out.println("nom tache périodique: " + tachePeriodique.getNom());
                    System.out.println("date tache périodique: " + tachePeriodique.getJournee().getDateDuJour());
                    user.getPlanning().getTachesaPlanifier().add(tachePeriodique);
                }catch(CloneNotSupportedException e){
                    System.out.println("not cloneable");
                }
                jour.incrementerJour(nbrJourDePeriodicite);
            }
        }
    }
    void replanifierTache(){

    }
    void evaluerTache(){

    }

    public void setCreneauDeTache(Creneau creneauDeTache) {
        this.creneauDeTache = creneauDeTache;
    }

}
