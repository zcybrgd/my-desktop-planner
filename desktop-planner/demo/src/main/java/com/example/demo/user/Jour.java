package com.example.demo.user;


import com.example.demo.planification.Creneau;
import com.example.demo.planification.Tache;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class Jour implements Serializable, Comparable<Jour> {
    private LocalDate dateDuJour;
    public Jour(){}
    private int encouragement=0;
    private int tachesRealisees=0;

    public int getTachesRealisees() {
        return tachesRealisees;
    }

    public void setTachesRealisees(int tachesRealisees) {
        this.tachesRealisees = tachesRealisees;
    }

    public int getEncouragement() {
        return encouragement;
    }

    public void setEncouragement(int encouragement) {
        this.encouragement = encouragement;
    }

    @Override
    public int compareTo(Jour other) {
        return this.dateDuJour.compareTo(other.dateDuJour);
    }
    private Set<Tache> listesDesTaches= new TreeSet<>();
    private List<Creneau> creneaux = new ArrayList<>();

    public Jour(LocalDate date) {
        this.dateDuJour = date;
    }

    public LocalDate getDateDuJour() {
        return dateDuJour;
    }

    public void setDateDuJour(LocalDate dateDuJour) {
        this.dateDuJour = dateDuJour;
    }

    public List<Creneau> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<Creneau> creneaux) {
        this.creneaux = creneaux;
    }

    public static Pair<Jour, Pair<Creneau, Integer>> rechercherCreneauLibreSimple(User user, Duration duration, LocalDate deadline) {
        for (Jour jour : user.getPlanning().getJours()) {
            if (jour.getDateDuJour().isAfter(deadline)) {
                break; // Stop searching if we reach the deadline
            }
            List<Creneau> creneaux = jour.getCreneaux();
            for (int i = 0; i < creneaux.size(); i++) {
                Creneau creneau = creneaux.get(i);
                System.out.println( creneau.getHeureDebut() +" - " + creneau.getHeureFin());
                if (creneau.calculerDuree().compareTo(duration) >= 0) {
                    return new Pair<>(jour, new Pair<>(creneau.substituteDuration(duration), i)); // a trouvé un créneau
                }
            }
        }

        return null; // pas de créneau trouvé
    }
    public static Pair<Jour, Pair<Creneau, Integer>> rechercherCreneauLibreDecompo(User user, Duration duration, LocalDate deadline) {
        for (Jour jour : user.getPlanning().getJours()) {
            if (jour.getDateDuJour().isAfter(deadline)) {
                break; // Stop searching if we reach the deadline
            }
            List<Creneau> creneaux = jour.getCreneaux();
            for (int i = 0; i < creneaux.size(); i++) {
                Creneau creneau = creneaux.get(i);
                if (creneau.isEstLibre()) {
                    creneau.setEstLibre(false);
                    return new Pair<>(jour, new Pair<>(creneau, i)); // j'ai trouvé un créneau
                }
            }
        }

        return null; // on a pas trouver un créneau
    }



    public void incrementerJour(int numberOfDays) {
        LocalDate newDate = dateDuJour.plusDays(numberOfDays);
        dateDuJour = newDate;
    }
    public Pair<Creneau, Integer> choisirCreneauDansUneJournee(User user, Jour jourChoisi) {
        List<Creneau> lesCreneauxLibresDuJour = jourChoisi.getCreneaux();
        // Create a list of strings representing the creneaux
        ListView<Creneau> listView = new ListView<>();
        listView.getItems().addAll(lesCreneauxLibresDuJour);
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Creneau item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getHeureDebut().toString() + " - " + item.getHeureFin().toString());
                }
            }
        });
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Choisissez ou vous voulez planifier votre tache");

        ButtonType selectButtonType = new ButtonType("Seléctionner", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(listView);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                return selectedIndex;
            }
            return null;
        });
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            try{
                Optional<Creneau> optionalCreneau = Optional.ofNullable(lesCreneauxLibresDuJour.get(result.get().intValue()));
                Dialog<Creneau> dialog2 = new Dialog<>();
                dialog2.setTitle("Créneaux libres pour le " + jourChoisi);
                dialog2.setHeaderText(null);
                dialog2.setResizable(false);

                Label debutLabel = new Label("Heure de début :");
                Label finLabel = new Label("Heure de fin :");
                ComboBox<LocalTime> debutComboBox = new ComboBox<>();
                ComboBox<LocalTime> finComboBox = new ComboBox<>();
                debutComboBox.getItems().addAll(Creneau.getHorairesPossibles(user.getMinDureeCreneau(), optionalCreneau));
                finComboBox.getItems().addAll(Creneau.getHorairesPossibles(user.getMinDureeCreneau(), optionalCreneau));
                HBox debutBox = new HBox(10, debutLabel, debutComboBox);
                HBox finBox = new HBox(10, finLabel, finComboBox);
                VBox vbox = new VBox(10, debutBox, finBox);
                dialog2.getDialogPane().setContent(vbox);

                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog2.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                dialog2.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        LocalTime debut = debutComboBox.getValue();
                        LocalTime fin = finComboBox.getValue();
                        if (debut == null) {
                            debut = Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).get(0);
                        }
                        if (fin == null) {
                            fin = Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).get(Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).size() - 1);
                        }
                        if (debut != null && fin != null) {
                            if (debut.isAfter(fin)) {
                                LocalTime temp = debut;
                                debut = fin;
                                fin = temp;
                            }
                            Creneau creneau = new Creneau(debut, fin);
                            return creneau;
                        }
                    }
                    return null;
                });

                Optional<Creneau> creneauResult = dialog2.showAndWait();
                if (creneauResult.isPresent()) {
                    Pair<Creneau, Integer> creneauChoisi = new Pair<>(creneauResult.get(), result.get());
                    return creneauChoisi;
                }
            }catch(IndexOutOfBoundsException e){
                // je fais en sorte qu'il fixe les créneaux pour cette journée manquante
            }


        }else{

        }
        return null;
    }
    public int comparerDates(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2);
    }



}
