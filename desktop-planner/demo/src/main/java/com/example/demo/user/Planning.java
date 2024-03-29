package com.example.demo.user;

import com.example.demo.Exceptions.PasDePlanning;
import com.example.demo.planification.Creneau;
import com.example.demo.planification.Tache;
import com.example.demo.planification.TacheDecomposable;
import com.example.demo.planification.TacheSimple;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Planning implements Serializable {
    private List<Jour> jours = new ArrayList<Jour>();
    private Set<TacheSimple> tachesaPlanifier = new TreeSet<>();
    private Set<Tache> tachesUnscheduled = new TreeSet<>();
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Pair<LocalDate, LocalDate> periode;
    private List<Badge> lesBadges = new ArrayList<>();

    public void addBadge(Badge badge) {
        lesBadges.add(badge);
    }
    private List<Badge> Badges;
    private ArrayList<Projet> userProjects = new ArrayList<>();

    public ArrayList<Projet> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(ArrayList<Projet> userProjects) {
        this.userProjects = userProjects;
    }

    public void setBadges(List<Badge> badges) {
        Badges = badges;
    }

    public List<Badge> getBadges() {
        return Badges;
    }


    public Set<TacheSimple> getTachesaPlanifier() {
        return tachesaPlanifier;
    }

    public void setTachesaPlanifier(Set<TacheSimple> tachesaPlanifier) {
        this.tachesaPlanifier = tachesaPlanifier;
    }

    public void setPeriode(LocalDate dateDebut, LocalDate dateFin) {
        this.periode = new Pair<LocalDate, LocalDate>(dateDebut, dateFin);
    }

    private List<Pair<LocalTime, LocalTime>> creneaux;

    public Planning(){}
     public Planning(LocalDate dateDebut, LocalDate dateFin){
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        setPeriode(dateDebut, dateFin);
        setJours(periode);
        this.lesBadges = new ArrayList<>();
     }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }
    public Pair<LocalDate, LocalDate> getPeriode() {
        return new Pair<>(getDateDebut(), getDateFin());
    }

    public LocalDate choisirDateDansPeriode() {
        DatePicker datePicker = new DatePicker();
        datePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(dateDebut) || item.isAfter(dateFin)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // rendre les dates en dehors de la plage en rose
                }
            }
        });

        Button btnOk = new Button("Valider");
        btnOk.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
        btnOk.setOnAction(event -> {
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        });

        VBox root = new VBox(datePicker, btnOk);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f5f5f5; -fx-font-size: 14px;");

        // afficher une fenêtre avec le date picker et le bouton pour que l'utilisateur puisse sélectionner une date
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.setOnCloseRequest(event -> {
            Jour jour = new Jour(datePicker.getValue());
            this.setJours(new Pair<>(dateDebut, dateFin));
        });

        stage.showAndWait();

        // retourner la date sélectionnée
        return datePicker.getValue();
    }

    public Set<Tache> getTachesUnscheduled() {
        return tachesUnscheduled;
    }

    public void setJours(Pair<LocalDate, LocalDate> periode) {
        LocalDate startDate = periode.getKey();
        LocalDate endDate = periode.getValue();

        List<Jour> jours = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            jours.add(new Jour(currentDate));
            currentDate = currentDate.plusDays(1);
        }
        this.jours = jours;
    }

    public List<Jour> getJours() {
        return jours;
    }

    public void definirCreneauxLibres(User user) {
        // Récupérer la période de planification du user
        Pair<LocalDate, LocalDate> periode = user.getPlanning().getPeriode();
        LocalDate currentDate = periode.getKey();
        LocalDate endDate = periode.getValue();
        this.setJours(periode);
        // Itérer sur chaque jour de la période
        while (!currentDate.isAfter(endDate)) {
            // Demander à l'utilisateur de saisir les créneaux libres pour ce jour
            List<Creneau> creneaux = demanderCreneauxLibres(currentDate, user);
            // Ajouter les créneaux libres au planning pour ce jour
            user.getPlanning().chercherJourDansPeriode(currentDate).setCreneaux(creneaux);
            // Passer au jour suivant
            currentDate = currentDate.plusDays(1);
        }
    }
    public Jour chercherJourDansPeriode(LocalDate date) {

        try{
            if (date.isBefore(dateDebut) || date.isAfter(dateFin)) {
                return null;
            }
            // Vérifier que la date recherchée est dans la période donnée
            // Rechercher le jour correspondant à la date
            for (Jour jour : this.jours) {
                if (jour.getDateDuJour().isEqual(date)) {
                    return jour;
                }
            }
        }catch(NullPointerException e){System.out.println(e.getMessage());}
        // Si la date n'a pas été trouvée, retourner null
        return null;
    }



    private List<Creneau> demanderCreneauxLibres(LocalDate date, User user) {
        // Créer la boîte de dialogue
        Dialog<List<Pair<LocalTime, LocalTime>>> dialog = new Dialog<>();
        dialog.setTitle("Créneaux libres pour le " + date.toString());
        dialog.setHeaderText(null);
        dialog.setResizable(false);

        // Créer les champs pour les heures de début et de fin
        Label debutLabel = new Label("Heure de début :");
        Label finLabel = new Label("Heure de fin :");
        ComboBox<LocalTime> debutComboBox = new ComboBox<>();
        ComboBox<LocalTime> finComboBox = new ComboBox<>();
        debutComboBox.getItems().addAll(Creneau.getHorairesPossibles(user.getMinDureeCreneau(),null));
        finComboBox.getItems().addAll(Creneau.getHorairesPossibles(user.getMinDureeCreneau(),null));
        HBox debutBox = new HBox(10, debutLabel, debutComboBox);
        HBox finBox = new HBox(10, finLabel, finComboBox);
        VBox vbox = new VBox(10, debutBox, finBox);
        dialog.getDialogPane().setContent(vbox);
        // Ajouter les boutons OK et Annuler
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Vérifier que les heures de début et de fin sont valides avant de fermer la boîte de dialogue
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                LocalTime debut = debutComboBox.getValue();
                LocalTime fin = finComboBox.getValue();
                if(debut==null){
                    debut = Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).get(0);
                }
                if(fin==null){
                    fin = Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).get(Creneau.getHorairesPossibles(user.getMinDureeCreneau(),null).size() - 1);
                }
                if (debut != null && fin != null) {
                    if (debut.isAfter(fin)) {
                        // swap debut and fin
                        LocalTime temp = debut;
                        debut = fin;
                        fin = temp;
                    }
                    Pair<LocalTime, LocalTime> creneau = new Pair<>(debut, fin);
                    return Arrays.asList(creneau);
                }
            }
            return null;
        });


        // Afficher la boîte de dialogue
        Optional<List<Pair<LocalTime, LocalTime>>> result = dialog.showAndWait();
        if (result.isPresent()) {
            List<Creneau> creneaux = new ArrayList<>();
            for (Pair<LocalTime, LocalTime> pair : result.get()) {
                creneaux.add(new Creneau(pair.getKey(), pair.getValue()));
            }
            return creneaux;
        } else {
            return Collections.emptyList();
        }
    }

    public static void planifier(User user, Pair<Boolean, Projet> projetAjout){
        // Afficher une boîte de dialogue avec deux options de planification
        List<String> choixPlanification = new ArrayList<>();
        choixPlanification.add("Planification manuelle");
        choixPlanification.add("Planification automatique");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Planification manuelle", choixPlanification);
        dialog.setTitle("Planification");
        dialog.setHeaderText("Choisissez une option de planification");
        dialog.setContentText("Option:");

        Optional<String> result = dialog.showAndWait();

        // Traiter le résultat de la boîte de dialogue
        result.ifPresent(selected -> {
            if (selected.equals("Planification manuelle")) {
                Stage stage = new Stage();
                stage.setTitle("Le type de la tache que vous allez planifier");
                Button simpleBtn = new Button("Simple");
                Button decomposableBtn = new Button("Decomposable");
                simpleBtn.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
                decomposableBtn.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
                simpleBtn.setOnAction(e -> {
                    try{
                        if(user.getPlanning()==null) throw new PasDePlanning("Vous n'avez initialiser aucun Planning");
                        e.consume();
                        LocalDate dateDejourneeChoisie = user.getPlanning().choisirDateDansPeriode();
                        Jour journeeChoisie = user.getPlanning().chercherJourDansPeriode(dateDejourneeChoisie);
                        try{
                            Pair<Creneau, Integer> creneauChoisi = journeeChoisie.choisirCreneauDansUneJournee(user, journeeChoisie);
                            if (creneauChoisi != null && creneauChoisi.getKey() != null) {
                                Duration dureeDeTache = creneauChoisi.getKey().calculerDuree();
                                TacheSimple tacheaIntroduire = new TacheSimple(dureeDeTache, creneauChoisi.getKey(), journeeChoisie);
                                user.introduireUneTacheManuelle(tacheaIntroduire, "Simple", creneauChoisi, journeeChoisie,user, projetAjout);
                            } else {

                            }
                        }catch(NullPointerException a){System.out.println("vous n'avez choisi aucune journée");}
                    }catch(PasDePlanning ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Y a aucun Planning courant dans votre Application!, veuillez initialiser un planning");
                        alert.setContentText("Veuillez choisissez une période pour votre planning, clicker sur le boutton Fixer pour faire cela");
                        alert.showAndWait();
                    }
                });

                decomposableBtn.setOnAction(e -> {
                    try{
                        if(user.getPlanning()==null) throw new PasDePlanning("Vous n'avez initialiser aucun Planning");
                        // Create a dialog box for entering the duration
                        TextInputDialog dialogD = new TextInputDialog();
                        dialogD.setTitle("Entrer la Durée ");
                        dialogD.setHeaderText(null);
                        dialogD.setContentText("Entrer la durée provisoire pour cette tache décomposée:");

                        // Show the dialog and wait for the user's input
                        Optional<String> resultD = dialogD.showAndWait();

                        // Process the user's input
                        resultD.ifPresent(durationString -> {
                            try {
                                Duration duration = Duration.ofMinutes(Long.parseLong(durationString));
                                TacheDecomposable tacheaIntroduire = new TacheDecomposable(duration);
                                user.introduireUneTacheManuelle(tacheaIntroduire, "Decomposable", null, null,user, projetAjout);
                            } catch (NumberFormatException ex) {
                                // Handle invalid input format
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Durée invalide");
                                alert.setContentText("La durée doit être un nombre entier de minutes");
                                alert.showAndWait();
                            }
                        });

                    }catch(PasDePlanning ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Y a aucun Planning courant dans votre Application!, veuillez initialiser un planning");
                        alert.setContentText("Veuillez choisissez une période pour votre planning, clicker sur le boutton Fixer pour faire cela");
                        alert.showAndWait();
                    }
                });

                stage.setOnCloseRequest(e -> {
                    System.out.println("closed without choosing");
                });

                HBox buttonsBox = new HBox(simpleBtn, decomposableBtn);
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.setSpacing(10);
                buttonsBox.setStyle("-fx-background: white;");
                Scene scene = new Scene(buttonsBox, 400, 100);
                stage.setScene(scene);
                stage.showAndWait();
                stage.setOnCloseRequest(close->{
                    System.out.println("pas de créneau choisi");
                });
                System.out.println("Planification manuelle choisie");

            } else if (selected.equals("Planification automatique")) {
                Stage stage = new Stage();
                stage.setTitle("Le type de la tache que vous voulez que le système planifie: ");
                Button simpleBtn = new Button("Simple");
                Button decomposableBtn = new Button("Decomposable");
                simpleBtn.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
                decomposableBtn.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
                simpleBtn.setOnAction(e -> {
                    try{
                        if(user.getPlanning()==null) throw new PasDePlanning("Vous n'avez initialiser aucun Planning");
                        e.consume();
                        TextInputDialog dialogD = new TextInputDialog();
                        dialogD.setTitle("Entrer la Durée ");
                        dialogD.setHeaderText(null);
                        dialogD.setContentText("Entrer la durée provisoire pour cette tache décomposée:");
                        // Show the dialog and wait for the user's input
                        Optional<String> resultD = dialogD.showAndWait();
                        // Process the user's input
                        resultD.ifPresent(durationString -> {
                            try {
                                Duration duration = Duration.ofMinutes(Long.parseLong(durationString));
                                if (duration.compareTo(user.getMinDureeCreneau()) < 0) {
                                    // La durée est inférieure à la durée minimale requise
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Erreur");
                                    alert.setHeaderText("Durée invalide");
                                    alert.setContentText("La durée doit être supérieure ou égale à " + user.getMinDureeCreneau().toMinutes() + " minutes");
                                    alert.showAndWait();
                                } else {
                                    TacheSimple tacheaIntroduire = new TacheSimple(duration);
                                    tacheaIntroduire.setJournee(null);
                                    tacheaIntroduire.setCreneauDeTache(null);
                                    user.introduireUneTacheAuto(tacheaIntroduire, "Simple", user, projetAjout);
                                }
                            } catch (NumberFormatException ex) {
                                // Handle invalid input format
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Durée invalide");
                                alert.setContentText("La durée doit être un nombre entier de minutes");
                                alert.showAndWait();
                            }
                        });

                    }catch(PasDePlanning ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Y a aucun Planning courant dans votre Application!, veuillez initialiser un planning");
                        alert.setContentText("Veuillez choisissez une période pour votre planning, clicker sur le boutton Fixer pour faire cela");
                        alert.showAndWait();
                    }
                });
                decomposableBtn.setOnAction(e -> {
                    try{
                        if(user.getPlanning()==null) throw new PasDePlanning("Vous n'avez initialiser aucun Planning");
                        // Create a dialog box for entering the duration
                        TextInputDialog dialogD = new TextInputDialog();
                        dialogD.setTitle("Entrer la Durée ");
                        dialogD.setHeaderText(null);
                        dialogD.setContentText("Entrer la durée provisoire pour cette tache décomposée:");

                        // Show the dialog and wait for the user's input
                        Optional<String> resultD = dialogD.showAndWait();

                        // Process the user's input
                        resultD.ifPresent(durationString -> {
                            try {
                                Duration duration = Duration.ofMinutes(Long.parseLong(durationString));
                                TacheDecomposable tacheaIntroduire = new TacheDecomposable(duration);
                                user.introduireUneTacheAuto(tacheaIntroduire, "Decomposable",user, projetAjout);
                            } catch (NumberFormatException ex) {
                                // Handle invalid input format
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Durée invalide");
                                alert.setContentText("La durée doit être un nombre entier de minutes");
                                alert.showAndWait();
                            }
                        });

                    }catch(PasDePlanning ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Y a aucun Planning courant dans votre Application!, veuillez initialiser un planning");
                        alert.setContentText("Veuillez choisissez une période pour votre planning, clicker sur le boutton Fixer pour faire cela");
                        alert.showAndWait();
                    }
                });
                stage.setOnCloseRequest(e -> {
                    System.out.println("closed without choosing");
                });
                HBox buttonsBox = new HBox(simpleBtn, decomposableBtn);
                buttonsBox.setSpacing(10);
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.setStyle("-fx-background: white;");
                Scene scene = new Scene(buttonsBox, 400, 100);
                stage.setScene(scene);
                stage.showAndWait();
                stage.setOnCloseRequest(close->{
                    System.out.println("pas de créneau choisi");
                });
                // Code pour la planification automatique
                System.out.println("Planification automatique choisie");
            }

        });
    }
}

