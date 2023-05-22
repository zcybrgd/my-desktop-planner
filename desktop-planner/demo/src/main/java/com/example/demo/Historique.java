package com.example.demo;

import com.example.demo.user.Planning;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
                        AfficherTasksOfOldPlanning(planning);
                    });
                    button.setStyle("-fx-background-color: white;");
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
    void AfficherTasksOfOldPlanning(Planning planning){}
}
