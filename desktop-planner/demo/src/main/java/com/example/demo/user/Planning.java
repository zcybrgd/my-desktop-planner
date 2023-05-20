package com.example.demo.user;

import com.example.demo.planification.Creneau;
import com.example.demo.planification.Tache;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Planning implements Serializable {
    private List<Jour> jours = new ArrayList<Jour>();
    private Set<Tache> tachesaPlanifier;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Pair<LocalDate, LocalDate> periode;



    public void setPeriode(LocalDate dateDebut, LocalDate dateFin) {
        this.periode = new Pair<LocalDate, LocalDate>(dateDebut, dateFin);
    }

    // pour stocker les créneaux horaires libres. Par exemple,
    // {(LocalTime.of(13,0), LocalTime.of(15,0)), (LocalTime.of(18,0), LocalTime.of(22,0))}
    // représente les créneaux libres de 13h à 15h et de 18h à 22h.
    private List<Pair<LocalTime, LocalTime>> creneaux;

     public Planning(LocalDate dateDebut, LocalDate dateFin){
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        setPeriode(dateDebut, dateFin);
        setJours(periode);
     }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    // LocalDate rentabilité() // le jour ou il était le plus rentable
    // replanifier() // replanifier les taches
    // modifierPeriode() // la période du planning
    // ajouter une tâche à une journée spécifique
    //

    public Pair<LocalDate, LocalDate> getPeriode() {
        return new Pair<>(getDateDebut(), getDateFin());
    }

    public LocalDate choisirDateDansPeriode() {
        DatePicker datePicker = new DatePicker();
        datePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(dateDebut) || item.isAfter(dateFin)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // rendre les dates en dehors de la plage en rose
                }
            }
        });

        Button btnOk = new Button("OK");
        btnOk.setOnAction(event -> {
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        });

        VBox root = new VBox(datePicker, btnOk);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        // appliquer le style CSS directement dans le code Java
        root.setStyle("-fx-background-color: #f5f5f5; -fx-font-size: 14px;");

        // afficher une fenêtre avec le date picker et le bouton pour que l'utilisateur puisse sélectionner une date
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.setOnCloseRequest(event -> {
            Jour jour = new Jour(datePicker.getValue());
            this.setJours(new Pair<>(dateDebut, dateFin));
        });

        stage.showAndWait();

        // retourner la date sélectionnée
        return datePicker.getValue();
    }



    public void setJours(Pair<LocalDate, LocalDate> periode) {
        LocalDate startDate = periode.getKey();
        LocalDate endDate = periode.getValue();

        List<Jour> jours = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            jours.add(new Jour(currentDate));
            currentDate = currentDate.plusDays(1);
        }
       for(Jour j : jours){
           System.out.println(j.getDateDuJour().toString());
       }
        this.jours = jours;
    }

    public void definirCreneauxLibres(User user) {
        // Récupérer la période de planification du user
        Pair<LocalDate, LocalDate> periode = user.getPlanning().getPeriode();
        LocalDate currentDate = periode.getKey();
        LocalDate endDate = periode.getValue();
        this.setJours(periode);
        // Itérer sur chaque jour de la période
        while (!currentDate.isAfter(endDate)) {
            // Demander à l'utilisateur de saisir les créneaux libres pour ce jour
            List<Creneau> creneaux = demanderCreneauxLibres(currentDate, user);
            // Ajouter les créneaux libres au planning pour ce jour
            user.getPlanning().chercherJourDansPeriode(currentDate).setCreneaux(creneaux);
            System.out.println("les créneaux libres de cet utilisateur: " + user.getPseudo() + " du jour : " + chercherJourDansPeriode(currentDate));
            for(Creneau c : user.getPlanning().chercherJourDansPeriode(currentDate).getCreneaux()){
                System.out.println("date début : " + c.getHeureDebut());
                System.out.println("date fin : " + c.getHeureFin());
            }
            // Passer au jour suivant
            currentDate = currentDate.plusDays(1);
        }
    }
    public Jour chercherJourDansPeriode(LocalDate date) {

        // Vérifier que la date recherchée est dans la période donnée
        if (date.isBefore(dateDebut) || date.isAfter(dateFin)) {
            return null;
        }

        // Rechercher le jour correspondant à la date
        for (Jour jour : this.jours) {
            if (jour.getDateDuJour().isEqual(date)) {
                return jour;
            }
        }
        // Si la date n'a pas été trouvée, retourner null
        return null;
    }



    private List<Creneau> demanderCreneauxLibres(LocalDate date, User user) {
        // Créer la boîte de dialogue
        Dialog<List<Pair<LocalTime, LocalTime>>> dialog = new Dialog<>();
        dialog.setTitle("Créneaux libres pour le " + date.toString());
        dialog.setHeaderText(null);
        dialog.setResizable(false);

        // Créer les champs pour les heures de début et de fin
        Label debutLabel = new Label("Heure de début :");
        Label finLabel = new Label("Heure de fin :");
        ComboBox<LocalTime> debutComboBox = new ComboBox<>();
        ComboBox<LocalTime> finComboBox = new ComboBox<>();
        debutComboBox.getItems().addAll(Creneau.getHorairesPossibles(user.getMinDureeCreneau(),null));
        finComboBox.getItems().addAll(Creneau.getHorairesPossibles(user.getMinDureeCreneau(),null));
        HBox debutBox = new HBox(10, debutLabel, debutComboBox);
        HBox finBox = new HBox(10, finLabel, finComboBox);
        VBox vbox = new VBox(10, debutBox, finBox);
        dialog.getDialogPane().setContent(vbox);
        // Ajouter les boutons OK et Annuler
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Vérifier que les heures de début et de fin sont valides avant de fermer la boîte de dialogue
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                LocalTime debut = debutComboBox.getValue();
                LocalTime fin = finComboBox.getValue();
                if(debut==null){
                    debut = Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).get(0);
                }
                if(fin==null){
                    fin = Creneau.getHorairesPossibles(user.getMinDureeCreneau(), null).get(Creneau.getHorairesPossibles(user.getMinDureeCreneau(),null).size() - 1);
                }
                if (debut != null && fin != null) {
                    if (debut.isAfter(fin)) {
                        // swap debut and fin
                        LocalTime temp = debut;
                        debut = fin;
                        fin = temp;
                    }
                    Pair<LocalTime, LocalTime> creneau = new Pair<>(debut, fin);
                    return Arrays.asList(creneau);
                }
            }
            return null;
        });


        // Afficher la boîte de dialogue
        Optional<List<Pair<LocalTime, LocalTime>>> result = dialog.showAndWait();
        if (result.isPresent()) {
            List<Creneau> creneaux = new ArrayList<>();
            for (Pair<LocalTime, LocalTime> pair : result.get()) {
                creneaux.add(new Creneau(pair.getKey(), pair.getValue()));
            }
            return creneaux;
        } else {
            return Collections.emptyList();
        }
    }


}

