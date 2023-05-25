package com.example.demo;




import com.example.demo.enumerations.EtatTache;
import com.example.demo.enumerations.Prio;
import com.example.demo.planification.Categorie;
import com.example.demo.planification.Creneau;
import com.example.demo.planification.Tache;
import com.example.demo.planification.TacheSimple;
import com.example.demo.user.*;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;


import java.time.LocalDate;
import java.util.*;


public class Calendrier {

    private User user;

    private Map<LocalDate, Integer> tachesRealiseesDansLaJournee = new HashMap<>();

    public void setUser(User user) {
        this.user = user;
        AfficherTasks();
    }
    @FXML
    private VBox boxAffichageTaches;


    @FXML
    void planification(ActionEvent event) {
           Pair<Boolean, Projet> projetAjout= new Pair<>(false, null);
           Planning.planifier(user, projetAjout);
           AfficherTasks();
    }

    void modifierNbrTachesDeLaJournee(TacheSimple tachesimple){
        List<Jour> jours = user.getPlanning().getJours();
        Jour targetJour = tachesimple.getJournee();  // The specific Jour object you want to modify
        int index = jours.indexOf(targetJour);
        if (index != -1) {
            Jour jourToUpdate = jours.get(index);
            jourToUpdate.setTachesRealisees(jourToUpdate.getTachesRealisees()+1);
            if(jourToUpdate.getTachesRealisees()==user.getMinTaskPerDay()){
                jourToUpdate.setEncouragement(jourToUpdate.getEncouragement()+1);
            }
        }
        if(tachesimple.getJournee().getEncouragement()>0){
            Label messageLabel = new Label("Félicitations ! Vous avez atteint le nombre minimal de tâches par jour.");
            messageLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 24px; -fx-text-fill: black;");
            VBox root = new VBox(messageLabel);
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 20px;");
            Scene scene = new Scene(root, 850, 200);
            Stage stage = new Stage();
            scene.setFill(Color.WHITE);
            stage.setScene(scene);
            stage.setTitle("Message d'encouragement");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            gererLesEncouragements();
            int closeDelaySeconds = 4;
            // Create a PauseTransition to delay the stage close
            PauseTransition delay = new PauseTransition(Duration.seconds(closeDelaySeconds));
            delay.setOnFinished(event -> stage.close());
            // Start the delay
            delay.play();
            System.out.println("--- Afficher les Badges ---");
            try{
                for(Badge badge : user.getPlanning().getBadges()){
                    System.out.println("Badge: " + badge.getBadgeLabel());
                }
            }catch(NullPointerException e){e.getMessage();}
        }
    }
    @FXML
    void affichage(ActionEvent event) {
        AfficherTasks();
    }
    void gererLesEncouragements() {
        List<Jour> jours = user.getPlanning().getJours();
        int consecutiveDays = 0; // Compteur de jours consécutifs avec 1 encouragement

        List<Badge> pendingBadges = new ArrayList<>(); // List to collect badges that need to be added

        for (Jour jour : jours) {
            if (jour.getEncouragement() == 1) {
                consecutiveDays++;
                if (consecutiveDays == 2) { // 5
                    Badge badgeGood = new Badge("Good");
                    pendingBadges.add(badgeGood);
                }
            } else {
                consecutiveDays = 0;
            }
        }

        int goodBadgeCount = 0; // Compteur de badges "Good" obtenus
        if (user.getPlanning().getBadges() == null) {
            user.getPlanning().setBadges(new ArrayList<>());
        }
        for (Badge badge : user.getPlanning().getBadges()) {
            if (badge.getBadgeLabel().equals("Good")) {
                goodBadgeCount++;
                if (goodBadgeCount == 2) { // 3
                    Badge badgeVGood = new Badge("VeryGood");
                    pendingBadges.add(badgeVGood);
                } else if (goodBadgeCount == 4) { // 6
                    Badge badgeE = new Badge("Excellent");
                    pendingBadges.add(badgeE);
                }
            }
        }

        // Add the pending badges to the user's planning badges list
        user.getPlanning().getBadges().addAll(pendingBadges);
    }



    void AfficherTasks(){
        if(user.getPlanning()!=null){
            Set<TacheSimple> tacheSimpleSet = user.getPlanning().getTachesaPlanifier();
            // Clear the existing buttons
            boxAffichageTaches.getChildren().clear();
            boxAffichageTaches.setSpacing(10);
            // Create a VBox to contain the task buttons
            VBox taskBoxContainer = new VBox(10);
            taskBoxContainer.setStyle("-fx-background-color: white;");
            taskBoxContainer.setPrefWidth(220); // Set the preferred width for the VBox
            // Wrap the VBox inside a ScrollPane
            ScrollPane scrollPane = new ScrollPane(taskBoxContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent;"); // Make the background transparent
            // Iterate over the sorted tasks and create buttons
            String currentDate = null;
            for (TacheSimple tacheSimple : tacheSimpleSet) {
                String taskDate = tacheSimple.getJournee().getDateDuJour().toString();
                // Add a separator between tasks of different dates
                if (!taskDate.equals(currentDate)) {
                    Label dateLabel = new Label(taskDate);
                    dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 0;");
                    taskBoxContainer.getChildren().add(dateLabel);
                    currentDate = taskDate;
                }
                // Create a button for the task
                Button taskButton = new Button(tacheSimple.getNom());
                taskButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: white; -fx-border-radius: 25;");
                taskButton.setPrefWidth(200);
                taskButton.setOnAction(clickedOnTask->{
                   lesActionsSurLesTaches(tacheSimple);
                });
                // Create a label for the task priority
                if(tacheSimple.getPriorite()==null){
                    tacheSimple.setPriorite(Prio.LOW);
                }
                Label priorityLabel = new Label(tacheSimple.getPriorite().toString());
                priorityLabel.setStyle("-fx-font-size: 12px;");

                // Create a label for the task creneau
                Creneau creneau = tacheSimple.getCreneauDeTache();
                Label creneauLabel = new Label(creneau.getHeureDebut() + " - " + creneau.getHeureFin());
                creneauLabel.setStyle("-fx-font-size: 12px;");

                // Create a label for the task etat
                Label etatLabel = new Label(tacheSimple.getStateDeTache().toString());
                etatLabel.setStyle("-fx-font-size: 12px;");

                // Create a colored circle representing the category
                Circle categoryCircle = new Circle(8);
                categoryCircle.setStyle("-fx-stroke: black;");
                Categorie category = tacheSimple.getCategorie();
                if (category != null) {
                    String color = category.getColor();
                    Color categoryColor = Color.web(color);
                    String categoryColorHex = String.format("#%02x%02x%02x", (int)(categoryColor.getRed() * 255),
                            (int)(categoryColor.getGreen() * 255), (int)(categoryColor.getBlue() * 255));
                    categoryCircle.setStyle("-fx-fill: " + categoryColorHex + ";");
                }

                // Create a VBox to contain the task details
                VBox taskDetailsBox = new VBox(5);
                taskDetailsBox.getChildren().addAll(priorityLabel, creneauLabel, etatLabel);
                taskDetailsBox.setAlignment(Pos.CENTER_LEFT);

                // Create a HBox to contain the category circle and task details
                HBox taskBox = new HBox(10);
                taskBox.getChildren().addAll(categoryCircle, taskButton, taskDetailsBox);
                taskBox.setAlignment(Pos.CENTER_LEFT);
                taskBox.setPadding(new Insets(5));

                taskBoxContainer.getChildren().add(taskBox);
            }

             // Add the ScrollPane to the main VBox
            boxAffichageTaches.getChildren().add(scrollPane);

        }
        else{
            System.out.println("ya pas de planning");
        }
    }
    @FXML
    void fixer(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene previousScene = calendarInit(currentStage);
        currentStage.setScene(previousScene);
        AfficherTasks();
    }

    public Scene calendarInit(Stage primaryStage) {
        Scene previousScene = primaryStage.getScene(); // Get the previous scene
        if (user.confirmerNouvellePeriode()) {
            Scene scene = user.fixerUneNouvellePériode(user);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); // make the window modal
            stage.setOnCloseRequest(event -> primaryStage.setScene(previousScene)); // Restore the previous scene on close
            stage.show(); // display the window modally
        } else {
            if (user.getPlanning() != null) {
                System.out.println("old planning of the user: " + user.getPlanning().getPeriode());
                System.out.println("Historique");
                if(user.getHistorique()!=null){
                    for(Planning planAncien : user.getHistorique()){
                        System.out.println("Historique de : " + planAncien.getDateFin());
                    }
                }
            }
        }

        if (user.getPlanning() != null) {
            System.out.println("periode existante on demande meme pas");
        } else {
            System.out.println("periode non existante mais il a fixer");
        }
        return previousScene;
    }

    void lesActionsSurLesTaches(TacheSimple tacheSimple){
        // Create the dialog window
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Task Options");
        dialog.setHeaderText("Choisissez une option pour modifier la tache");

        // Set the dialog buttons
        ButtonType evaluerButton = new ButtonType("Evaluer Tache", ButtonBar.ButtonData.OK_DONE);
        ButtonType renommerButton = new ButtonType("Renommer Tache", ButtonBar.ButtonData.OK_DONE);
        ButtonType replanifierButton = new ButtonType("Replanifier Tache", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(evaluerButton, renommerButton,replanifierButton, ButtonType.CANCEL);

        // Create the content for the dialog
        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);

        // Handle Evaluer Tache button
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == evaluerButton) {
                tacheSimple.evaluerTache(user);
                AfficherTasks();
                if(tacheSimple.getStateDeTache()==EtatTache.completed){
                    modifierNbrTachesDeLaJournee(tacheSimple);
                }
            } else if (dialogButton == renommerButton) {
                tacheSimple.changerNom(user);
                AfficherTasks();
            } else if (dialogButton == replanifierButton){
                tacheSimple.replanifierTache(user);
                AfficherTasks();
            }
            // Return null for cancel button or if no option was selected
            return null;
        });

        // Show the dialog window
        dialog.showAndWait();
    }

    @FXML
    void affichageUnscheduled(ActionEvent event) {
        Set<Tache> unscheduledTasks = user.getPlanning().getTachesUnscheduled();

        if (unscheduledTasks.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Aucune tâche non planifiée");
            alert.setContentText("Il n'y a aucune tâche non planifiée pour le moment.");
            alert.showAndWait();
        } else {
            StringBuilder contentText = new StringBuilder();
            for (Tache task : unscheduledTasks) {
                contentText.append("Nom : ").append(task.getNom()).append("\t\t\t\t");
                contentText.append("Deadline : ").append(task.getDeadline()).append("\n\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Tâches non planifiées");
            alert.setContentText(contentText.toString());
            alert.showAndWait();
        }

    }
}
