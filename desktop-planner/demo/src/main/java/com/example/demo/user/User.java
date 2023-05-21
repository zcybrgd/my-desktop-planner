package com.example.demo.user;


import com.example.demo.Exceptions.DateAnterieure;
import com.example.demo.HelloApplication;
import com.example.demo.Projets;
import com.example.demo.enumerations.Prio;
import com.example.demo.planification.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;


import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 4652182404050987967L;
    private String pseudo;
    private String mdp;
    private Planning planning;
    private ArrayList<Planning> Historique;
    private int minTaskPerDay; // le minimum des taches requis pour une journée

    public void setMinTaskPerDay(int minTaskPerDay) {
        this.minTaskPerDay = minTaskPerDay;
    }

    public int getMinTaskPerDay() {
        return minTaskPerDay;
    }


    private Duration minDureeCreneau=Duration.ofMinutes(30);;

    public User(String pseudo, String mdp){
        this.pseudo = pseudo;
        this.mdp = mdp;
    }
   public User(String pseudo, String mdp, Planning planning, ArrayList<Planning> Historique, int minTaskPerDay, Duration minDureeCreneau){
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.planning = planning;
        this.Historique = Historique;
        this.minTaskPerDay = minTaskPerDay;
        this.minDureeCreneau = minDureeCreneau;
   }

   public void creerProjet(Projet nvProjet){
       this.getPlanning().getUserProjects().add(nvProjet);
   }

    // supprimerProjet()
    public void modifierProjet(Projet projet){
        // Create a new stage for modifying the project
        Stage modifyStage = new Stage();

        // Create input fields for project name and description
        TextField nameField = new TextField(projet.getNom());
        TextArea descriptionArea = new TextArea(projet.getDescription());

        // Create a VBox to hold the input fields
        VBox modifyBox = new VBox(10);
        modifyBox.getChildren().addAll(new Label("Nom du projet:"), nameField, new Label("Description:"), descriptionArea);
        modifyBox.setPadding(new Insets(10));

        // Create a button to validate the modifications
        Button validateButton = new Button("Valider les modifications");
        validateButton.setOnAction(validate -> {
            // Update the project with the modified information
            projet.setNom(nameField.getText());
            projet.setDescription(descriptionArea.getText());
            // Close the modify stage
            modifyStage.close();
        });

        // Create a VBox to hold the modify box and the validate button
        VBox rootBox = new VBox(10);
        rootBox.getChildren().addAll(modifyBox, validateButton);
        rootBox.setPadding(new Insets(10));

        // Create a scene and set it as the content of the modify stage
        Scene scene = new Scene(rootBox);
        modifyStage.setScene(scene);
        modifyStage.show();
    }
    // LocalTime dureeCat(), la durée du temps passé sur les taches d'une catégorie

    public static Pair<Boolean, User> seConnecter(String username, String password) {
        // récupérer les utilisateurs de l'application
        ArrayList<User> users = loadUsersFromFile(HelloApplication.getFileNameUsers());
        for (User user : users) {
            if (user.getPseudo().equals(username) && user.getMdp().equals(password)) {
                return new Pair<>(true, user); // un utilisateur existe avec ce username et ce mot de passe
            }
        }
        return new Pair<>(false, null); // y a aucun utilisateur qui existe
    }


    public static boolean creerCompte(String username, String password) {
        ArrayList<User> users = loadUsersFromFile(HelloApplication.getFileNameUsers());
        for (User user : users) {
            if (user.getPseudo().equals(username)) {
                return false; // Le nom d'utilisateur est déjà pris
            }
        }
        User newUser = new User(username, password);
        users.add(newUser);
        saveUsersToFile(users, HelloApplication.getFileNameUsers());
        return true; // Le compte a été créé avec succès
    }

    // pour fixer la période de ce planning
    public void fixerPlanning(LocalDate dateDebut, LocalDate dateFin) throws DateAnterieure {
        LocalDate dateActuelle = LocalDate.now();

        // Vérifier que la date de début est supérieure ou égale à la date actuelle
        if (dateDebut.isBefore(dateActuelle)) {
            throw new DateAnterieure("La date de début ne peut pas être antérieure à la date actuelle.");
        }
        // Vérifier que la date de fin est supérieure ou égale à la date de début
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        Planning nouveauPlanning = new Planning(dateDebut, dateFin);
        setPlanning(nouveauPlanning);
    }

    public void introduireUneTache(Tache tache, String type, Pair<Creneau, Integer> creneauChoisi, Jour journeeChoisie, User user) {
        // Create the fields for entering task data
        final int[] periodicite = {0};
        final int[] nbrdeDecompo = {0};
        TextField nomTacheTextField = new TextField();
        ComboBox<Prio> prioriteComboBox = new ComboBox<>();
        prioriteComboBox.setItems(FXCollections.observableArrayList(Prio.values()));
        DatePicker deadlineDatePicker = new DatePicker();
        ComboBox<String> categorieComboBox = new ComboBox<>();
        categorieComboBox.setItems(FXCollections.observableArrayList(Categorie.getCategoryNames()));
        // If the task is a TacheSimple, add a field for the periodicity
        VBox tacheSimpleVBox = new VBox();
        if (type.equals("Simple")) {
            TextField periodiciteTextField = new TextField();
            periodiciteTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0,
                    change -> {
                        String newText = change.getControlNewText();
                        if (newText.matches("\\d*")) {
                            try {
                                int intValue = Integer.parseInt(newText);
                                if (intValue > 0) {
                                    return change;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        return null;
                    }));

            // Listener to retrieve the periodicité value
            periodiciteTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    periodicite[0] = Integer.parseInt(newValue);
                    // Do something with the periodicité value
                    System.out.println("Périodicité: " + periodicite[0]);
                }
            });
            tacheSimpleVBox.getChildren().addAll(new Label("Périodicité :"), periodiciteTextField);
        }

        if (type.equals("Decomposable")) {
            TextField periodiciteTextField = new TextField();
            periodiciteTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0,
                    change -> {
                        String newText = change.getControlNewText();
                        if (newText.matches("\\d*")) {
                            try {
                                int intValue = Integer.parseInt(newText);
                                if (intValue > 0) {
                                    return change;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        return null;
                    }));

            // Listener to retrieve the periodicité value
            periodiciteTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    nbrdeDecompo[0] = Integer.parseInt(newValue);
                    // Do something with the periodicité value
                    System.out.println("nbr de décompo:  " + nbrdeDecompo[0]);
                }
            });
            tacheSimpleVBox.getChildren().addAll(new Label("Décomposer sur combien? :"), periodiciteTextField);
        }
        // Create the dialog box for entering the task
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("Nom de la tâche :"), nomTacheTextField);
        gridPane.addRow(1, new Label("Priorité :"), prioriteComboBox);
        gridPane.addRow(2, new Label("Deadline :"), deadlineDatePicker);
        gridPane.addRow(3, new Label("Catégorie :"), categorieComboBox);
        gridPane.addRow(4, tacheSimpleVBox);

        // Define the converter for displaying the priority in the drop-down list
        prioriteComboBox.setConverter(new StringConverter<Prio>() {
            @Override
            public String toString(Prio prio) {
                return prio != null ? prio.toString() : "";
            }
            @Override
            public Prio fromString(String string) {
                for (Prio prio : Prio.values()) {
                    if (prio.toString().equals(string)) {
                        return prio;
                    }
                }
                return null;
            }
        });

        // Display the dialog box and retrieve the entered values
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nouvelle tâche");
        alert.setHeaderText("Introduire une nouvelle tâche :");
        alert.getDialogPane().setContent(gridPane);

        // Wait for the user to close the dialog box and process the entered values
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // Retrieve the entered values
                String nomTache = nomTacheTextField.getText();
                Prio priorite = prioriteComboBox.getValue();
                LocalDate deadline = deadlineDatePicker.getValue();
                String categorie = categorieComboBox.getValue();
                Categorie selectedCategorie;
                if(categorie==null){
                    selectedCategorie = new Categorie("Other");
                }else{
                    selectedCategorie = new Categorie(categorie);
                }
                if (tache instanceof TacheSimple) {
                    creneauChoisi.getKey().decomposer(creneauChoisi, journeeChoisie.getCreneaux());
                    TacheSimple tacheSimple = (TacheSimple) tache;
                    tacheSimple.setNom(nomTache);
                    tacheSimple.setPriorite(priorite);
                    tacheSimple.setDeadline(deadline);
                    tacheSimple.setCategorie(selectedCategorie);
                    tacheSimple.setNbrJourDePeriodicite(periodicite[0]);
                    tacheSimple.planifierTache(user);
                }
                if (tache instanceof TacheDecomposable) {
                    TacheDecomposable tacheDecomposable = (TacheDecomposable) tache;
                    tacheDecomposable.setNom(nomTache);
                    tacheDecomposable.setDeadline(deadline);
                    tacheDecomposable.setPriorite(priorite);
                    tacheDecomposable.setCategorie(selectedCategorie);
                    tacheDecomposable.decomposer(nbrdeDecompo[0],user);
                    tacheDecomposable.planifierTache(user);
                }
            }
        });
    }
    // sauvegarder les utilisateurs dans le fichier
    public static void saveUsersToFile(ArrayList<User> users, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    // récuperer les utilisateurs de l'application de ce fichier binaire
    public static ArrayList<User> loadUsersFromFile(String fileName) {
        ArrayList<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public static void saveUpdateUsertoFile(User user){
        ArrayList<User> users = User.loadUsersFromFile(HelloApplication.getFileNameUsers());
        for (User userO : users) {
            if (userO.getPseudo().equals(user.getPseudo())) {
                for(TacheSimple t: user.getPlanning().getTachesaPlanifier()){
                    System.out.println("les taches: " + t.getNom());
                }
                if(userO.getPlanning()!=null){
                    for(TacheSimple t: user.getPlanning().getTachesaPlanifier()){
                        System.out.println("les taches: tae user 0 " + t.getNom());
                    }
                }
                users.remove(userO);
                break;
            }
        }
        users.add(user);
        User.saveUsersToFile(users, HelloApplication.getFileNameUsers());

    }
    public static void updateUsersFile(ArrayList<User> modifiedUsers, String fileName) {
        ArrayList<User> existingUsers = loadUsersFromFile(fileName);

        // Merge the modifiedUsers into the existingUsers
        for (User modifiedUser : modifiedUsers) {
            for (int i = 0; i < existingUsers.size(); i++) {
                User existingUser = existingUsers.get(i);
                if (existingUser.getPseudo().equals(modifiedUser.getPseudo())) {
                    // Update the existingUser with the modifiedUser's data
                    existingUsers.set(i, modifiedUser);
                    break;
                }
            }
        }
        // Save the updated users to the file
        saveUsersToFile(existingUsers, fileName);
    }

    // validerPlanning()
    public boolean confirmerNouvellePeriode() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous fixer une nouvelle période pour votre planning ?");

        ButtonType ouiButton = new ButtonType("Oui");
        ButtonType nonButton = new ButtonType("Non");

        alert.getButtonTypes().setAll(ouiButton, nonButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ouiButton){
            // Si l'utilisateur clique sur "Oui", on retourne true pour indiquer que l'utilisateur veut fixer une nouvelle période.
            return true;
        } else {
            // Sinon, on retourne false pour indiquer que l'utilisateur ne veut pas fixer une nouvelle période.
            return false;
        }
    }
    public Scene fixerUneNouvellePériode(User user){
        Font robotoRegular = Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto/Roboto-Regular.ttf"), 16);
        // Create form elements
        Label titleLabel = new Label("Fixer une période du planning");
        titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        Button submitButton = new Button("Valider");
        // Create form layout
        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(10));
        formLayout.setHgap(10);
        formLayout.setVgap(10);
        formLayout.add(new Label("Date de début :"), 0, 0);
        formLayout.add(startDatePicker, 1, 0);
        formLayout.add(new Label("Date de fin :"), 0, 1);
        formLayout.add(endDatePicker, 1, 1);
        formLayout.add(submitButton, 1, 2);
        // Create root layout
        VBox rootLayout = new VBox();
        rootLayout.setPadding(new Insets(10));
        rootLayout.getChildren().addAll(titleLabel, formLayout);
        // Create scene and show stage
        Scene scene = new Scene(rootLayout, 400, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/fixer.css").toExternalForm());
        // Handle submit button click
        submitButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            try {
                Planning planning = new Planning(startDate, endDate);
                if(user.getPlanning()!=null){
                    if(user.getHistorique()!=null){
                        ArrayList<Planning> historique = user.getHistorique();
                        historique.add(planning);
                        user.setHistorique(historique);
                    }else{
                        ArrayList<Planning> historique = new ArrayList<Planning>();
                        historique.add(planning);
                        user.setHistorique(historique);
                    }
                }
                user.fixerPlanning(startDate, endDate);
                user.setPlanning(planning);
                user.getPlanning().definirCreneauxLibres(user);
            } catch (DateAnterieure e) {
                // Afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("La date de début saisie est antérieure à la date actuelle.");
                alert.setContentText("Veuillez saisir une date de début valide.");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                // Afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("La date de fin saisie est antérieure à la date de début.");
                alert.setContentText("Veuillez saisir une date de fin valide.");
                alert.showAndWait();
            }
        });
        formLayout.getStyleClass().add("grid-pane");
        rootLayout.getStyleClass().add("v-box");
        return scene;
    }

    // les getters et les setters
    public Planning getPlanning() {
        return planning;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public Duration getMinDureeCreneau() {
        return minDureeCreneau;
    }
    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public ArrayList<Planning> getHistorique() {
        return Historique;
    }

    public void setHistorique(ArrayList<Planning> historique) {
        Historique = historique;
    }
}
