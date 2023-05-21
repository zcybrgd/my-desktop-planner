package com.example.demo;



import com.example.demo.planification.Tache;
import com.example.demo.planification.TacheDecomposable;
import com.example.demo.planification.TacheSimple;
import com.example.demo.user.Planning;
import com.example.demo.user.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.Serializable;
import java.util.Optional;


public class Calendrier {

    private User user;


    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void planification(ActionEvent event) {
           Planning.planifier(user);
    }

    @FXML
    void affichage(ActionEvent event) {
         if(user.getPlanning()!=null){
             for(TacheSimple tacheSimple : user.getPlanning().getTachesaPlanifier()){
                     System.out.println("tache: " + tacheSimple.getNom());
                     System.out.println("prio : " + tacheSimple.getPriorite());
                     System.out.println("--- simple -- ");
                     System.out.println("date: " + tacheSimple.getJournee().getDateDuJour().toString());
                     System.out.println("heure debut : " + tacheSimple.getCreneauDeTache().getHeureDebut());
                     System.out.println("heure fin : " + tacheSimple.getCreneauDeTache().getHeureFin());
             }
         }else{
             System.out.println("ya pas de planning");
         }
    }
    @FXML
    void fixer(ActionEvent event) {
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
