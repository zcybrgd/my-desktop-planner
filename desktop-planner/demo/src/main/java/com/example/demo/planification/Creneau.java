package com.example.demo.planification;


import com.example.demo.user.Jour;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Creneau implements Decomposable, Serializable, Comparable<Creneau> {
    private LocalTime HeureDebut;
    private LocalTime HeureFin;
    private boolean estLibre=true;

    public Creneau(LocalTime HeureDebut, LocalTime HeureFin){
        this.HeureDebut = HeureDebut;
        this.HeureFin = HeureFin;
    }

    public Creneau(){}
    @Override
    public int compareTo(Creneau other) {
        return this.getHeureDebut().compareTo(other.getHeureDebut());
    }

    public boolean isEstLibre() {
        return estLibre;
    }

    public Creneau substituteDuration(Duration newDuration) {
        LocalTime newHeureFin = HeureDebut.plus(newDuration);
        return new Creneau(HeureDebut, newHeureFin);
    }

    public Duration calculerDuree() {
        return Duration.between(HeureDebut, HeureFin);
    }


    public boolean comparerHeure(LocalTime heure) {
        return (heure.isAfter(HeureDebut) && heure.isBefore(HeureFin)) || heure.equals(HeureDebut) || heure.equals(HeureFin);
    }
    public static List<LocalTime> getHorairesPossibles(Duration minDureeCreneau, Optional<Creneau> creneau) {
        List<LocalTime> horairesPossibles = new ArrayList<>();
        LocalTime heure;
        LocalTime heureFin;
        if(creneau == null || creneau.isEmpty()){
            heure = LocalTime.of(4, 30); // il commence sa journée
            heureFin = LocalTime.of(23, 30); // il la termine
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

    /** les getters et les setters**/
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
            Creneau nvCreneau = new Creneau(heureDebutCreneau, heureDebutTache);
            creneauxDeLaJournee.set(creneauChoisi.getValue(), nvCreneau);
        } else {
            // Cas où la tâche ne chevauche pas le créneau
            // on le décompose pas
        }
    }
    public static Duration calculateTotalDuration(Map<Jour, ArrayList<Pair<Creneau, Integer>>> creneauxDeTache) {
        Duration totalDuration = Duration.ZERO;

        for (ArrayList<Pair<Creneau, Integer>> creneauxList : creneauxDeTache.values()) {
            for (Pair<Creneau, Integer> pair : creneauxList) {
                Creneau creneau = pair.getKey();
                totalDuration = totalDuration.plus(creneau.calculerDuree());
            }
        }
        return totalDuration;
    }
}
