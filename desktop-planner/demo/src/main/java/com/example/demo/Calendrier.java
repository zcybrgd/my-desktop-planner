package com.example.demo;




import com.example.demo.planification.TacheSimple;
import com.example.demo.user.Planning;
import com.example.demo.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public class Calendrier {

    private User user;


    public void setUser(User user) {
        this.user = user;
    }
    @FXML
    private VBox boxAffichageTaches;

    @FXML
    private ScrollPane scrollAffichageTaches;


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
             Set<TacheSimple> tacheSimpleSet = user.getPlanning().getTachesaPlanifier() ; // Your Set<TacheSimple> instance
             // Clear the existing buttons
             boxAffichageTaches.getChildren().clear();
             // Sort the tasks by date
             List<TacheSimple> sortedTasks = new ArrayList<>(tacheSimpleSet);
             sortedTasks.sort(Comparator.comparing(t -> t.getJournee().getDateDuJour()));
            // Iterate over the sorted tasks and create buttons
             String currentDate = null;
             for (TacheSimple tacheSimple : sortedTasks) {
                 String taskDate = tacheSimple.getJournee().getDateDuJour().toString();
                 // Add a separator between tasks of different dates
                 if (!taskDate.equals(currentDate)) {
                     Label dateLabel = new Label(taskDate);
                     boxAffichageTaches.getChildren().add(dateLabel);
                     currentDate = taskDate;
                 }

                 // Create a button for the task
                 Button taskButton = new Button(tacheSimple.getNom() + " - " + tacheSimple.getPriorite());
                 boxAffichageTaches.getChildren().add(taskButton);
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
            if(user.getPlanning()!=null && user.getHistorique()==null){
                ArrayList<Planning> historique = new ArrayList<>();
                historique.add(user.getPlanning());
                user.setHistorique(historique);
            }else if(user.getPlanning()!=null && user.getHistorique()!=null){
                user.getHistorique().add(user.getPlanning());
            }
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


}
