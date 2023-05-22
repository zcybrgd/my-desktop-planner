package com.example.demo.planification;

import com.example.demo.enumerations.Prio;
import com.example.demo.user.Jour;
import com.example.demo.user.Projet;
import com.example.demo.user.User;
import javafx.util.Pair;


import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TacheDecomposable extends Tache implements Decomposable, Serializable {
    public TacheDecomposable(String nom, Duration duree, Prio priorite, LocalDate deadline, Categorie categorie){
        super(nom, priorite, deadline, categorie);
    }
    public TacheDecomposable(Duration duree){
        super(duree);
    }
    private Set<TacheSimple> sousTaches = new TreeSet<>();

    public Set<TacheSimple> getSousTaches() {
        return sousTaches;
    }

    // Ses sous-tâches auront le même nom que la tâche décomposée auquel sera concaténée le numéro de la sous tâche.
    public void planifierTache(User user, Pair<Boolean, Projet> projetAjout){
        // Une tâche décomposable peut être planifiée en plusieurs créneaux jusqu'à atteindre la durée prévue.
        for(TacheSimple sousTache: sousTaches){
            sousTache.setDuree(sousTache.getCreneauDeTache().calculerDuree());
            user.getPlanning().getTachesaPlanifier().add(sousTache);
            if(projetAjout.getKey()){
                projetAjout.getValue().getEnsembleDesTaches().add(sousTache);
            }
        }
    }
    public void replanifierTache(){

    }
    public void evaluerTache(User user){

    }
    public TacheDecomposable(){}
    public void decomposer(int nbrDecompo, User user){
        if(nbrDecompo!=0){
            for(int i = 0;i<nbrDecompo;i++){
                LocalDate dateDejourneeChoisie = user.getPlanning().choisirDateDansPeriode();
                Jour journeeChoisie = user.getPlanning().chercherJourDansPeriode(dateDejourneeChoisie);
                Pair<Creneau, Integer> creneauChoisi = journeeChoisie.choisirCreneauDansUneJournee(user, journeeChoisie);
                creneauChoisi.getKey().decomposer(creneauChoisi, journeeChoisie.getCreneaux());
                Duration dureeDeSousTache = creneauChoisi.getKey().calculerDuree();
                TacheSimple sousTache = new TacheSimple(this.getNom() + " "+ i,dureeDeSousTache,this.getPriorite(),this.getDeadline(),this.getCategorie(),0);
                sousTache.setCreneauDeTache(creneauChoisi.getKey());
                sousTache.getJournees().add(journeeChoisie);
                sousTache.setJournee(journeeChoisie);
                sousTaches.add(sousTache);
            }} else{// dans l'automatique
         }}

    }

