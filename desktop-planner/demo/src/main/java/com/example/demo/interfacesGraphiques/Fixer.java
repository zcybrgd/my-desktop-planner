package com.example.demo.interfacesGraphiques;


import com.example.demo.user.User;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.Serializable;
import java.util.Optional;

public class Fixer implements Serializable {

    private User utilisateur;


/*
    public static Scene calendarInit(Stage primaryStage, User user){
        Scene scene;
            if(user.confirmerNouvellePeriode()){
                scene = user.fixerUneNouvellePériode(user);
                primaryStage.setScene(scene);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); // make the window modal
                stage.show(); // display the window modally
            }else{
                System.out.println("on a choisi aucun planning its supposed to have a planning; cet user : " + user.getPseudo());
                if(user.getPlanning()!=null){
                    System.out.println("old planning of the user: " + user.getPlanning().getPeriode());
                }
            }
        if(user.getPlanning()!=null){System.out.println("periode existante on demande meme pas");}
        else{System.out.println("periode non existante mais il a fixer");}
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(user.getPlanning()!=null){
                    System.out.println("on a get ce planning: " + user.getPlanning().getPeriode());
                }
                // Show confirmation alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sauvegarder votre activité dans l'application");
                alert.setHeaderText("Voulez vous sauvegarder avant de quitter l'application?");
                alert.setContentText("Cliquez sur OK pour enregistrer ou sur Annuler pour annuler vos modifications.");

                ButtonType saveButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(saveButton, cancelButton);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == saveButton){
                    User.saveUpdateUsertoFile(user);
                } else {
                    // User clicked Cancel button, close application
                    Platform.exit();
                }
            }
        });

        return scene;
    }*/
}
