package com.example.demo;




import com.example.demo.enumerations.EtatTache;
import com.example.demo.planification.Categorie;
import com.example.demo.planification.Creneau;
import com.example.demo.planification.TacheSimple;
import com.example.demo.user.Planning;
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
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.*;


public class Calendrier {

    private User user;


    public void setUser(User user) {
        this.user = user;
        AfficherTasks();
    }
    @FXML
    private VBox boxAffichageTaches;

    @FXML
    private ScrollPane scrollAffichageTaches;


    @FXML
    void planification(ActionEvent event) {
           Planning.planifier(user);
           AfficherTasks();
    }

    @FXML
    void affichage(ActionEvent event) {
        AfficherTasks();
    }
    void AfficherTasks(){
        if(user.getPlanning()!=null){
            Set<TacheSimple> tacheSimpleSet = user.getPlanning().getTachesaPlanifier();
            // Clear the existing buttons
            boxAffichageTaches.getChildren().clear();
            boxAffichageTaches.setSpacing(10);
            // Create a VBox to contain the task buttons
            VBox taskBoxContainer = new VBox(10);
            taskBoxContainer.setStyle("-fx-background-color: white;");
            taskBoxContainer.setPrefWidth(220); // Set the preferred width for the VBox
            // Wrap the VBox inside a ScrollPane
            ScrollPane scrollPane = new ScrollPane(taskBoxContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent;"); // Make the background transparent
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
                    dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 0;");
                    taskBoxContainer.getChildren().add(dateLabel);
                    currentDate = taskDate;
                }
                // Create a button for the task
                Button taskButton = new Button(tacheSimple.getNom());
                taskButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-color: black; -fx-border-radius: 5;");
                taskButton.setPrefWidth(200);
                taskButton.setOnAction(clickedOnTask->{
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

                    // Handle Evaluer Tache button
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == evaluerButton) {
                            // Create the choice dialog for evaluation selection
                            ChoiceDialog<EtatTache> choiceDialog = new ChoiceDialog<>(EtatTache.notRealized, EtatTache.values());
                            choiceDialog.setTitle("Evaluation de la tache");
                            choiceDialog.setHeaderText("évaluer la tache");
                            choiceDialog.setContentText("Choisissez");

                            // Show the choice dialog and get the selected evaluation
                            Optional<EtatTache> result = choiceDialog.showAndWait();
                            result.ifPresent(etatTache -> {
                                System.out.println("Selected evaluation: " + etatTache);
                                tacheSimple.setStateDeTache(etatTache);
                                user.getPlanning().getTachesaPlanifier().add(tacheSimple);
                                AfficherTasks();
                                // Perform the desired action with the selected evaluation
                            });
                        } else if (dialogButton == renommerButton) {
                            // Create the text input dialog for new name entry
                            TextInputDialog inputDialog = new TextInputDialog();
                            inputDialog.setTitle("Renommer la tache");
                            inputDialog.setHeaderText("Entrer un nouveau nom pour votre tache");
                            inputDialog.setContentText("Le nouveau nom:");

                            // Show the text input dialog and get the entered name
                            Optional<String> result = inputDialog.showAndWait();
                            result.ifPresent(newName -> {
                                System.out.println("New name: " + newName);
                                // Perform the desired action with the entered new name
                                tacheSimple.setNom(newName);
                                user.getPlanning().getTachesaPlanifier().add(tacheSimple);
                                AfficherTasks();
                            });
                        }
                        // Return null for cancel button or if no option was selected
                        return null;
                    });

                    // Show the dialog window
                    dialog.showAndWait();
                });
                // Create a label for the task priority
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
                if (category != null && category.getCouleur() != null) {
                    Color categoryColor = category.getCouleur();
                    String categoryColorHex = String.format("#%02x%02x%02x", (int)(categoryColor.getRed() * 255),
                            (int)(categoryColor.getGreen() * 255), (int)(categoryColor.getBlue() * 255));
                    categoryCircle.setStyle("-fx-fill: " + categoryColorHex + ";");
                }

                // Create a VBox to contain the task details
                VBox taskDetailsBox = new VBox(5);
                taskDetailsBox.getChildren().addAll(priorityLabel, creneauLabel, etatLabel);
                taskDetailsBox.setAlignment(Pos.CENTER_LEFT);

                // Create a HBox to contain the category circle and task details
                HBox taskBox = new HBox(10);
                taskBox.getChildren().addAll(categoryCircle, taskButton, taskDetailsBox);
                taskBox.setAlignment(Pos.CENTER_LEFT);
                taskBox.setPadding(new Insets(5));

                taskBoxContainer.getChildren().add(taskBox);
            }

// Add the ScrollPane to the main VBox
            boxAffichageTaches.getChildren().add(scrollPane);

        }
        else{
            System.out.println("ya pas de planning");
        }
    }
    @FXML
    void fixer(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene previousScene = calendarInit(currentStage);
        currentStage.setScene(previousScene);
        AfficherTasks();
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
