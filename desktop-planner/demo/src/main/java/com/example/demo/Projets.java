package com.example.demo;

import com.example.demo.planification.Tache;
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
            // Perform your actions with the new project data
            String projectName = projectData.getNom();
            String projectDescription = projectData.getDescription();
            System.out.println("New project created: " + projectName + ", Description: " + projectDescription);
            Projet nvProjet = new Projet(projectName, projectDescription);
            user.creerProjet(nvProjet);
            // After creating a new project, update the label
            updateCptProjets();
            AffichageProjects();
        });
    }
    private void updateCptProjets() {
        // Perform your conditions to determine the value of cptProjets
        int numberOfProjects = 0;
        try{
            if(user.getPlanning().getUserProjects()!=null && user.getPlanning().getUserProjects().size()>0){
                numberOfProjects = user.getPlanning().getUserProjects().size();
                // Update the label text
                cptProjets.setText("Vous avez  " + numberOfProjects + "  projets pour le moment");
            }
        }catch(NullPointerException e){System.out.println(e.getMessage());}
    }
    void AffichageProjects(){
        try{
            // Assuming you have an ArrayList<Projet> named projects
            // Create a VBox to hold the project buttons
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

           // Set the ScrollPane as the content of your container
            affichageProjetsBox.getChildren().clear();
            ScrollPane scrollPane = new ScrollPane(projectBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            scrollPane.setStyle("-fx-background-color: white;");
            affichageProjetsBox.setStyle("-fx-background-color: white;");
            scrollPane.setStyle("-fx-background-color: white;");
            affichageProjetsBox.getChildren().add(scrollPane);

        }catch(NullPointerException e){e.getMessage();}


    }
    void AfficherProjet(Projet projet){
        Stage taskStage = new Stage();
        VBox taskBox = new VBox(10);
        taskBox.setPadding(new Insets(10));

        // Create buttons for each task in the project
        for (Tache task : projet.getEnsembleDesTaches()) {
            Button taskButton = new Button(task.getNom());
            // Create labels for deadline, duration, and state
            Label deadlineLabel = new Label("Deadline: " + task.getDeadline());
            Label durationLabel = new Label("Duration: " + task.getDuree());
            Label stateLabel = new Label("State: " + task.getStateDeTache());

            // Create an HBox to hold the labels
            HBox taskDetailsBox = new HBox(10);
            taskDetailsBox.getChildren().addAll(deadlineLabel, durationLabel, stateLabel);

            // Create a VBox to hold the task button and details
            VBox taskItemBox = new VBox(5);
            taskItemBox.getChildren().addAll(taskButton, taskDetailsBox);

            taskBox.getChildren().add(taskItemBox);
        }

        // Create buttons for evaluating, modifying, and adding a project
        Button evaluateButton = new Button("Evaluer Projet");
        Button modifyButton = new Button("Modifier Projet");
        Button addButton = new Button("Ajouter une Tache à ce projet");
        evaluateButton.setOnAction(evaluer->{
            projet.evaluerProjet();
            AffichageProjects();
        });
        modifyButton.setOnAction(modifier->{
            user.modifierProjet(projet);
            AffichageProjects();
        });
        addButton.setOnAction(ajouter->{
            Planning.planifier(user);
        });
        // Create an HBox to hold the three buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(evaluateButton, modifyButton, addButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Create a VBox to hold the task box and button box
        VBox rootBox = new VBox(10);
        rootBox.getChildren().addAll(taskBox, buttonBox);
        rootBox.setPadding(new Insets(10));

        // Create a scene and set it as the content of the stage
        Scene scene = new Scene(rootBox);
        taskStage.setScene(scene);
        taskStage.show();
    }

}
