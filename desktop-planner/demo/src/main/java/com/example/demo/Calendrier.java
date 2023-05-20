package com.example.demo;

import com.example.demo.planification.Creneau;
import com.example.demo.user.Planning;
import com.example.demo.user.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.Serializable;
import java.util.Optional;


public class Calendrier implements Serializable {

    private User user;


    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void planification(ActionEvent event) {
            System.out.println("j'ai clické sur le bouton");
           // Creneau.planifierManuellementAuto(user);
           Planning.planifier(user);
    }

    @FXML
    void fixer(ActionEvent event) {
        System.out.println("je vais fixer alala et mon user : " + user.getPseudo());
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene previousScene = calendarInit(currentStage);
        currentStage.setScene(previousScene);
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
            System.out.println("on a choisi aucun planning; cet user: " + user.getPseudo());
            if (user.getPlanning() != null) {
                System.out.println("old planning of the user: " + user.getPlanning().getPeriode());
            }
        }

        if (user.getPlanning() != null) {
            System.out.println("periode existante on demande meme pas");
        } else {
            System.out.println("periode non existante mais il a fixer");
        }


        return previousScene;
    }


}
