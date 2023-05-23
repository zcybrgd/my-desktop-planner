package com.example.demo;

import com.example.demo.user.Planning;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class Historique {
    private User user;

    public void setUser(User user) {
        this.user = user;
        AfficherHistorique();
    }
    @FXML
    private AnchorPane afficherHisto;
    void AfficherNoHistory(){
        Label promptLabel = new Label("Il n'y a pas d'historique dans cette application");
        promptLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 24px;");
        AnchorPane.setTopAnchor(promptLabel, 20.0);
        AnchorPane.setBottomAnchor(promptLabel, 20.0);
        AnchorPane.setLeftAnchor(promptLabel, 20.0);
        AnchorPane.setRightAnchor(promptLabel, 20.0);
        afficherHisto.getChildren().add(promptLabel);
    }
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
    void AfficherDataofOldPlanning(Planning planning){
        // Create a new stage
        Stage newStage = new Stage();
        newStage.setTitle("Afficher ancien Planning");
        // Create the buttons
        Button projetsButton = new Button("Afficher Projets");
        Button badgesButton = new Button("Afficher Badges");
        Button tachesButton = new Button("Afficher Taches");
        // Set the button actions
        projetsButton.setOnAction(e -> {
            // Handle "Afficher Projets" button action
            System.out.println("Afficher Projets button clicked");
        });
        badgesButton.setOnAction(e -> {
            // Handle "Afficher Badges" button action
            System.out.println("Afficher Badges button clicked");
        });
        tachesButton.setOnAction(e -> {
            // Handle "Afficher Taches" button action
            System.out.println("Afficher Taches button clicked");
        });
        // Create a horizontal box to hold the buttons
        HBox buttonBox = new HBox(10); // spacing between buttons
        buttonBox.getChildren().addAll(projetsButton, badgesButton, tachesButton);
        buttonBox.setAlignment(Pos.CENTER);
        // Create a scene and set it in the stage
        Scene scene = new Scene(buttonBox);
        newStage.setScene(scene);
        // Show the stage
        newStage.show();
    }
}
