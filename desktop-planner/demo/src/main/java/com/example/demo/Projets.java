package com.example.demo;

import com.example.demo.planification.Tache;
import com.example.demo.planification.TacheSimple;
import com.example.demo.user.Planning;
import com.example.demo.user.Projet;
import com.example.demo.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class Projets {
    @FXML
    private VBox affichageProjetsBox;
    private User user;

    public void setUser(User user) {
        this.user = user;
        updateCptProjets();
        AffichageProjects();
    }

    @FXML
    private Label cptProjets;


    /**créer un nouveau projet**/

    @FXML
    void nouveauProjet(ActionEvent event) {
        Dialog<Projet> dialog = new Dialog<>();
        dialog.setTitle("Créer un nouveau projet");
        dialog.setHeaderText("Saisissez les détails du projet");

        // Set up the dialog buttons
        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Set up the grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create the input fields
        TextField projectNameField = new TextField();
        TextArea projectDescriptionArea = new TextArea();

        // Add the input fields to the grid pane
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(projectNameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(projectDescriptionArea, 1, 1);

        // Enable/disable the create button based on input validation
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);
        projectNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty());
        });

        // Set the dialog content
        dialog.getDialogPane().setContent(grid);

        // Convert the result when the create button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                String projectName = projectNameField.getText();
                String projectDescription = projectDescriptionArea.getText();
                return new Projet(projectName, projectDescription);
            }
            return null;
        });

        // Show the dialog and wait for user input
        Optional<Projet> result = dialog.showAndWait();
        result.ifPresent(projectData -> {
            String projectName = projectData.getNom();
            String projectDescription = projectData.getDescription();
            Projet nvProjet = new Projet(projectName, projectDescription);
            user.creerProjet(nvProjet);
            // After creating a new project, update the label
            updateCptProjets();
            AffichageProjects();
        });
    }

    /**mettre à jour le nombre des projets quand on ajoute un nouveau projet**/
    private void updateCptProjets() {
        int numberOfProjects = 0;
        try{
            if(user.getPlanning().getUserProjects()!=null && user.getPlanning().getUserProjects().size()>0){
                numberOfProjects = user.getPlanning().getUserProjects().size();
                // Update the label text
                cptProjets.setText("Vous avez " + numberOfProjects + " projets pour le moment");
            }
        }catch(NullPointerException e){System.out.println("ya pas de projets, ya meme pas un planning courant, veuillez fixer un planning dans la page Calendrier");}
    }
    void AffichageProjects(){
        try{
            VBox projectBox = new VBox();
            projectBox.setSpacing(35);
            projectBox.setAlignment(Pos.CENTER);
            projectBox.setStyle("-fx-background-color: white;");
            // Create a ScrollPane to contain the VBox
            // Iterate over the projects and create buttons
            for (Projet projet : user.getPlanning().getUserProjects()) {
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
            affichageProjetsBox.getChildren().clear();
            ScrollPane scrollPane = new ScrollPane(projectBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            scrollPane.setStyle("-fx-background-color: white;");
            affichageProjetsBox.setStyle("-fx-background-color: white;");
            affichageProjetsBox.getChildren().add(scrollPane);

        }catch(NullPointerException e){e.getMessage();}


    }
    void AfficherProjet(Projet projet){
        Stage taskStage = new Stage();
        VBox taskBox = new VBox(10);
        taskBox.setStyle("-fx-background-color: white;");
        taskBox.setPadding(new Insets(10));

        // Create buttons for each task in the project
        for (Tache task : projet.getEnsembleDesTaches()) {
            Button taskButton = new Button(task.getNom());
            taskButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 25px; -fx-border-color: white; -fx-border-radius: 25;");
            taskButton.setOnAction(clickedOnTask->{
                if(task instanceof TacheSimple tacheSimple)
                lesActionsSurLesTaches(tacheSimple);
            });
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

        // Create buttons for evaluating, modifying, and adding a project
        Button evaluateButton = new Button("Evaluer Projet");
        Button modifyButton = new Button("Modifier Projet");
        Button addButton = new Button("Ajouter une Tache à ce projet");

        // Set the preferred width of the buttons
        evaluateButton.setMinWidth(150);
        evaluateButton.setMaxWidth(200);
        modifyButton.setMinWidth(150);
        modifyButton.setMaxWidth(200);
        addButton.setMinWidth(150);
        addButton.setMaxWidth(200);

        evaluateButton.setOnAction(evaluer -> {
            projet.evaluerProjet();
            AffichageProjects();
        });
        modifyButton.setOnAction(modifier -> {
            user.modifierProjet(projet);
            AffichageProjects();
        });
        addButton.setOnAction(ajouter -> {
            Pair<Boolean, Projet> projetAjout = new Pair<>(true, projet);
            Planning.planifier(user, projetAjout);
        });
        // Create an HBox to hold the three buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(evaluateButton, modifyButton, addButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Create a VBox to hold the task box and button box
        VBox rootBox = new VBox(10);
        rootBox.getChildren().addAll(taskBox, buttonBox);
        rootBox.setPadding(new Insets(10));
        rootBox.setStyle("-fx-background-color: white;");
        // Create a ScrollPane and set it as the content of the rootBox
        ScrollPane scrollPane = new ScrollPane(rootBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        // Create a scene and set it as the content of the stage
        Scene scene = new Scene(scrollPane);
        scene.setFill(Color.WHITE);
        taskStage.setScene(scene);
        // Set the preferred width of the stage
        taskStage.setWidth(800);
        taskStage.show();

    }

    void lesActionsSurLesTaches(TacheSimple tacheSimple){
        System.out.println("clicked on this task : " + tacheSimple.getNom());
        // Create the dialog window
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Task Options");
        dialog.setHeaderText("Choisissez une option pour modifier la tache");

        // Set the dialog buttons
        ButtonType evaluerButton = new ButtonType("Evaluer Tache", ButtonBar.ButtonData.OK_DONE);
        ButtonType renommerButton = new ButtonType("Renommer Tache", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(evaluerButton, renommerButton, ButtonType.CANCEL);

        // Create the content for the dialog
        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == evaluerButton) {
                tacheSimple.evaluerTache(user);
            } else if (dialogButton == renommerButton) {
                tacheSimple.changerNom(user);
            }
            // Return null for cancel button or if no option was selected
            return null;
        });

        // Show the dialog window
        dialog.showAndWait();
    }



}
