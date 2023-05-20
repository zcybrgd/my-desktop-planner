package com.example.demo.planification;


import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Creneau implements Decomposable, Serializable, Comparable<Creneau> {
    private LocalTime HeureDebut;
    private LocalTime HeureFin;
    private boolean estLibre=true;

    public Creneau(LocalTime HeureDebut, LocalTime HeureFin){
        this.HeureDebut = HeureDebut;
        this.HeureFin = HeureFin;
    }

    @Override
    public int compareTo(Creneau other) {
        return this.getHeureDebut().compareTo(other.getHeureDebut());
    }

    public boolean isEstLibre() {
        return estLibre;
    }
    // decomposer() implements Décomposable
    // comparerDuree()

    public Duration calculerDuree() {
        return Duration.between(HeureDebut, HeureFin);
    }



    // si l'heure hadi elle est f l'intervalle du créneau
    public boolean comparerHeure(LocalTime heure) {
        return (heure.isAfter(HeureDebut) && heure.isBefore(HeureFin)) || heure.equals(HeureDebut) || heure.equals(HeureFin);
    }
    public static List<LocalTime> getHorairesPossibles(Duration minDureeCreneau, Optional<Creneau> creneau) {
        List<LocalTime> horairesPossibles = new ArrayList<>();
        LocalTime heure;
        LocalTime heureFin;
        if(creneau == null || creneau.isEmpty()){
            heure = LocalTime.of(6, 0); // il commence sa journée à 6:00 AM
            heureFin = LocalTime.of(22, 0); // il la termine 10:00 PM
        }else{
            heure = creneau.get().getHeureDebut();
            heureFin = creneau.get().getHeureFin().plus(minDureeCreneau);
        }

        while (heure.isBefore(heureFin)) {
            horairesPossibles.add(heure);
            heure = heure.plusMinutes((int) minDureeCreneau.toMinutes()); // increment by 30 minutes
        }
        return horairesPossibles;
    }

    // les getters et les setters
    public void setEstLibre(boolean estLibre) {
        this.estLibre = estLibre;
    }


    public void setHeureDebut(LocalTime heureDebut) {
        HeureDebut = heureDebut;
    }

    public void setHeureFin(LocalTime heureFin) {
        HeureFin = heureFin;
    }

    public LocalTime getHeureDebut() {
        return HeureDebut;
    }

    public LocalTime getHeureFin() {
        return HeureFin;
    }
    public void setOccupe(){
        estLibre = false;
    }

    public boolean inclusDans(Creneau creneau1, Creneau creneau2) {
        return creneau1.getHeureDebut().compareTo(creneau2.getHeureDebut()) >= 0 &&
                creneau1.getHeureFin().compareTo(creneau2.getHeureFin()) <= 0;
    }
    public boolean creneauChevauche(Creneau creneau1, Creneau creneau2) {
        return creneau1.getHeureDebut().compareTo(creneau2.getHeureFin()) <= 0 &&
                creneau2.getHeureDebut().compareTo(creneau1.getHeureFin()) <= 0;
    }

    // return Duration de la tache;
    public void decomposer(Pair<Creneau, Integer> creneauChoisi, List<Creneau> creneauxDeLaJournee){
        Creneau creneauaDecomposer = creneauxDeLaJournee.get(creneauChoisi.getValue());
        LocalTime heureDebutCreneau = creneauaDecomposer.getHeureDebut();
        LocalTime heureFinCreneau = creneauaDecomposer.getHeureFin();
        LocalTime heureDebutTache = creneauChoisi.getKey().getHeureDebut();
        LocalTime heureFinTache = creneauChoisi.getKey().getHeureFin();

        if ( heureFinTache.isBefore(heureFinCreneau)
                && heureFinTache.isAfter(heureDebutCreneau)&& heureDebutTache.equals(heureDebutCreneau)) {
            // Cas où la tâche occupe le début du créneau
            System.out.println("la tache est programmée au début du créneau");
            Creneau nvCreneau = new Creneau(heureFinTache, heureFinCreneau);
            creneauxDeLaJournee.set(creneauChoisi.getValue(), nvCreneau);
        } else if (heureDebutTache.isAfter(heureDebutCreneau) && heureFinTache.isBefore(heureFinCreneau)) {
            // Cas où la tâche occupe le milieu du créneau
            Creneau creneauDebut = new Creneau(heureDebutCreneau, heureDebutTache);
            Creneau creneauFin = new Creneau(heureFinTache, heureFinCreneau);
            creneauxDeLaJournee.remove(creneauChoisi.getValue().intValue());
            creneauxDeLaJournee.add(creneauDebut);
            creneauxDeLaJournee.add(creneauFin);
        } else if (heureDebutTache.isAfter(heureDebutCreneau)
                && heureDebutTache.isBefore(heureFinCreneau)&& heureFinTache.equals(heureFinCreneau)) {
            System.out.println("la tache est programmée à la fin du créneau");
            Creneau nvCreneau = new Creneau(heureDebutCreneau, heureDebutTache);
            creneauxDeLaJournee.set(creneauChoisi.getValue(), nvCreneau);
        } else {
            // Cas où la tâche ne chevauche pas le créneau
            // on le décompose pas
        }
    }
}
