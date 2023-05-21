package com.example.demo;

import com.example.demo.user.Projet;
import com.example.demo.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
            user.getPlanning().getUserProjects().add(nvProjet);
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
            affichageProjetsBox.getChildren().clear();
            VBox projectBox = new VBox();
            projectBox.setSpacing(10);
            projectBox.setStyle("-fx-background-color: white;");
            // Create a ScrollPane to contain the VBox
            ScrollPane scrollPane = new ScrollPane(projectBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(400);
            scrollPane.setStyle("-fx-background-color: white;");
            // Iterate over the projects and create buttons
            for (Projet projet : user.getPlanning().getUserProjects()) {
                // Create a button for the project
                Button projectButton = new Button(projet.getNom() + " - " + projet.getStateDeTache());
                projectButton.setOnAction(clickedOnProject->{
                    System.out.println("ahaha j'ai clické");
                    AfficherProjet(projet);
                });
                // Add the project button to the VBox
                projectBox.getChildren().add(projectButton);
            }

            // Set the ScrollPane as the content of your container
            affichageProjetsBox.getChildren().add(scrollPane);
        }catch(NullPointerException e){e.getMessage();}


    }
    void AfficherProjet(Projet projet){
        if (!projet.getEnsembleDesTaches().isEmpty()) {
            // Display the list of tasks (similar to previous code)
            // Add code to display the tasks for the selected project
        }

        // Create "Modify Project" button
        Button modifyButton = new Button("Modify Project");
        modifyButton.setOnAction(modifyEvent -> {
            // Handle modify project action
        });

        // Create "Add New Task" button
        Button addTaskButton = new Button("Add New Task");
        addTaskButton.setOnAction(addTaskEvent -> {
            // Handle add new task action
        });

        // Create an HBox to hold the two buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(modifyButton, addTaskButton);
        buttonsBox.setAlignment(Pos.CENTER);

        // Add the buttonsBox to the scene (e.g., add it to a parent container)
        // ..
    }
}
