package com.example.demo;

import com.example.demo.enumerations.Prio;
import com.example.demo.planification.Categorie;
import com.example.demo.planification.Creneau;
import com.example.demo.planification.Tache;
import com.example.demo.planification.TacheSimple;
import com.example.demo.user.Badge;
import com.example.demo.user.Planning;
import com.example.demo.user.Projet;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Set;

public class Historique {
    private User user;

    public void setUser(User user) {
        this.user = user;
        AfficherHistorique();
    }
    @FXML
    private AnchorPane afficherHisto;
    /**quand il n'ya pas de historique**/
    void AfficherNoHistory(){
        Label promptLabel = new Label("Il n'y a pas d'historique dans cette application");
        promptLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 24px;");
        AnchorPane.setTopAnchor(promptLabel, 20.0);
        AnchorPane.setBottomAnchor(promptLabel, 20.0);
        AnchorPane.setLeftAnchor(promptLabel, 20.0);
        AnchorPane.setRightAnchor(promptLabel, 20.0);
        afficherHisto.getChildren().add(promptLabel);
    }
    /**Affichage de l'historique**/
    void AfficherHistorique(){
        List<Planning> historique = user.getHistorique();

        afficherHisto.getChildren().clear(); // Clear previous content

        try{
            if (historique.isEmpty()) {
                AfficherNoHistory();
            } else {
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(10);

                for (Planning planning : historique) {
                    Button button = new Button("Planning de " + planning.getDateDebut() + " à " + planning.getDateFin());
                    button.setOnAction(afficherOldPlanning->{
                        AfficherDataofOldPlanning(planning);
                    });
                    button.setStyle("-fx-background-color: white; -fx-font-size: 18px;");
                    button.setMaxWidth(Double.MAX_VALUE);
                    VBox.setMargin(button, new Insets(10));
                    vbox.getChildren().add(button);
                }

                afficherHisto.getChildren().add(vbox);
            }
        }catch(NullPointerException e){
            AfficherNoHistory();
        }

    }
    /**afficher les infos d'un ancien planning, ses taches, ses badges, ses projets**/
    void AfficherDataofOldPlanning(Planning planning){
        // Create a new stage
        Stage newStage = new Stage();
        newStage.setTitle("Afficher ancien Planning");
        // Create the buttons
        Button projetsButton = new Button("Afficher Projets");
        Button badgesButton = new Button("Afficher Badges");
        Button tachesButton = new Button("Afficher Taches");
        projetsButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
        badgesButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
        tachesButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: #2F80ED; -fx-border-radius: 25; -fx-min-width: 150px;");
        // Set the button actions
        projetsButton.setOnAction(e -> {
            // Handle "Afficher Projets" button action
            AfficherAncienProjets(planning);
            System.out.println("Afficher Projets button clicked");
        });
        badgesButton.setOnAction(e -> {
            // Handle "Afficher Badges" button action
            AfficherAncienBadges(planning);
            System.out.println("Afficher Badges button clicked");
        });
        tachesButton.setOnAction(e -> {
            // Handle "Afficher Taches" button action
            AfficherAncienTasks(planning);
            System.out.println("Afficher Taches button clicked");
        });
        // Create a horizontal box to hold the buttons
        HBox buttonBox = new HBox(30); // spacing between buttons
        buttonBox.getChildren().addAll(projetsButton, badgesButton, tachesButton);
        buttonBox.setAlignment(Pos.CENTER);
        // Create a scene and set it in the stage
        Scene scene = new Scene(buttonBox);
        newStage.setScene(scene);
        // Show the stage
        newStage.show();
    }
    /**Afficher ancien taches d'un planning**/
    void AfficherAncienTasks(Planning planning){
        if(planning!=null){
            Set<TacheSimple> tacheSimpleSet = planning.getTachesaPlanifier();
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
                taskDetailsBox.setAlignment(Pos.CENTER);

                // Create a HBox to contain the category circle and task details
                HBox taskBox = new HBox(10);
                taskBox.getChildren().addAll(categoryCircle, taskButton, taskDetailsBox);
                taskBox.setAlignment(Pos.CENTER);
                taskBox.setPadding(new Insets(5));

                taskBoxContainer.getChildren().add(taskBox);
            }
            // Add the ScrollPane to the main VBox
            Scene newScene = new Scene(scrollPane);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();

        }
        else{
            System.out.println("ya pas de planning");
        }
    }
    /**Afficher ancien projets d'un planning**/
    void AfficherAncienProjets(Planning planning){
        try{
            // Assuming you have an ArrayList<Projet> named projects
            // Create a VBox to hold the project buttons
            VBox projectBox = new VBox();
            projectBox.setSpacing(35);
            projectBox.setAlignment(Pos.CENTER);
            projectBox.setStyle("-fx-background-color: white;");
            // Create a ScrollPane to contain the VBox
            // Iterate over the projects and create buttons
            for (Projet projet : planning.getUserProjects()) {
                // Create a button for the project
                Button projectButton = new Button(projet.getNom());
                projectButton.setPrefWidth(280);
                projectButton.setStyle("-fx-font-size: 19px; -fx-background-color: #FFC0CB; -fx-border-radius: 25;");
                projectButton.setOnAction(clickedOnProject -> {
                    AfficherProjet(projet);
                });
                // Create a label for the task state
                Label stateLabel = new Label(projet.getStateDeTache().toString());
                stateLabel.setStyle("-fx-font-size: 12px;");

                // Create an HBox to hold the project button and state label
                HBox projectDetailsBox = new HBox(13);
                projectDetailsBox.setAlignment(Pos.CENTER);
                projectDetailsBox.getChildren().addAll(projectButton, stateLabel);

                // Add the project button and details to the VBox
                projectBox.getChildren().add(projectDetailsBox);
            }

            ScrollPane scrollPane = new ScrollPane(projectBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            scrollPane.setStyle("-fx-background-color: white;");
            Scene newScene = new Scene(scrollPane);
            Stage pStage = new Stage();
            pStage.setScene(newScene);
            pStage.show();
        }catch(NullPointerException e){e.getMessage();}

    }
    /**Affichage d'un projet dans la fenetre des anciens projets d'un planning**/
    void AfficherProjet(Projet projet){
        Stage taskStage = new Stage();
        VBox taskBox = new VBox(10);
        taskBox.setStyle("-fx-background-color: white;");
        taskBox.setPadding(new Insets(10));

        // Create buttons for each task in the project
        for (Tache task : projet.getEnsembleDesTaches()) {
            Button taskButton = new Button(task.getNom());
            taskButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: white; -fx-border-radius: 25;");
            // Create labels for deadline, duration, and state
            Label deadlineLabel = new Label("Deadline: " + task.getDeadline());
            Label durationLabel = new Label("Duration: " + task.getDuree());
            Label stateLabel = new Label("State: " + task.getStateDeTache());

            // Create an HBox to hold the labels
            HBox taskDetailsBox = new HBox(10);
            taskDetailsBox.getChildren().addAll(deadlineLabel, durationLabel, stateLabel);
            taskDetailsBox.setAlignment(Pos.CENTER);
            // Create a VBox to hold the task button and details
            VBox taskItemBox = new VBox(5);
            taskItemBox.getChildren().addAll(taskButton, taskDetailsBox);
            taskItemBox.setAlignment(Pos.CENTER);
            taskBox.getChildren().add(taskItemBox);
        }
        ScrollPane scrollPane = new ScrollPane(taskBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        Scene newScene = new Scene(scrollPane);
        taskStage.setScene(newScene);
        taskStage.show();

    }
    void AfficherAncienBadges(Planning planning){
        Stage newStage = new Stage();
        List<Badge> badges = planning.getBadges();
        Label goodLabel = new Label();
        Label vGoodLabel = new Label();
        Label excellentLabel = new Label();

        // Count the occurrences of each badge type
        int goodCount = 0;
        int vGoodCount = 0;
        int excellentCount = 0;
        try{
            for (Badge badge : badges) {
                System.out.println("on est dans la boucle pour afficher les badges: " + badge.getBadgeLabel());
                String badgeType = badge.getBadgeLabel();
                if (badgeType.equals("Good")) {
                    goodCount++;
                } else if (badgeType.equals("VeryGood")) {
                    vGoodCount++;
                } else if (badgeType.equals("Excellent")) {
                    excellentCount++;
                }
            }
        }catch(NullPointerException e){e.getMessage();}
        // Update the label texts with the badge counts
        goodLabel.setText(goodCount + " Good Badges");
        vGoodLabel.setText(vGoodCount + " Very Good Badges");
        excellentLabel.setText(excellentCount + " Excellent Badges");
        VBox badgeBox = new VBox(30);
        badgeBox.getChildren().addAll(goodLabel, vGoodLabel, excellentLabel);
        Scene newScene = new Scene(badgeBox);
        newStage.setScene(newScene);
        newStage.show();

    }
}
