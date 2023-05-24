package com.example.demo.enumerations;

import java.io.Serializable;

/**Etat des taches, fourni des statistiques sur l'état de chaque tache**/

public enum EtatTache implements Serializable {
    notRealized, completed, inProgress, cancelled, delayed, unscheduled
    }
