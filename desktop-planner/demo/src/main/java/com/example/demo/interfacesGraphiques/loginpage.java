package com.example.demo.interfacesGraphiques;


import com.example.demo.HelloApplication;
import com.example.demo.SideBar;
import com.example.demo.user.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

public class loginpage implements Serializable {
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public GridPane creerPageLogin() {
        Font robotoRegular = Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto/Roboto-Regular.ttf"), 16);
        GridPane loginGrid = new GridPane();
        Label welcomeLabel = new Label("Bienvenue dans My Desktop Planner");
        welcomeLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        loginGrid.add(welcomeLabel, 0, 0, 2, 1);
        welcomeLabel.setStyle("-fx-padding: 17px 0;");
        GridPane.setHalignment(welcomeLabel, HPos.CENTER);
        loginGrid.getStyleClass().add("login-grid");
        loginGrid.setAlignment(Pos.CENTER);
        // Add Username Label
        Label userNameLabel = new Label("Username:");
        userNameLabel.getStyleClass().add("login-label");
        loginGrid.add(userNameLabel, 0, 1);
        userNameLabel.setFont(robotoRegular);
        // Add Username Text Field
        TextField userNameTextField = new TextField();
        userNameTextField.getStyleClass().add("login-field");
        loginGrid.add(userNameTextField, 1, 1);

        // Add Password Label
        Label passwordLabel = new Label("Mot de passe: ");
        passwordLabel.getStyleClass().add("login-label");
        passwordLabel.setFont(robotoRegular);
        loginGrid.add(passwordLabel, 0, 2);

        // Add Password Text Field
        PasswordField passwordTextField = new PasswordField();
        passwordTextField.getStyleClass().add("login-field");
        loginGrid.add(passwordTextField, 1, 2);

        // Add Login Button
        Button loginButton = new Button("Se Connecter");
        loginButton.getStyleClass().add("login-button");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton);
        loginGrid.add(hbBtn, 1, 4);

        // Add Create Account Button
        Button createAccountButton = new Button("Créer un compte");
        createAccountButton.getStyleClass().add("create-account-button");
        HBox hbCreateAccount = new HBox(10);
        hbCreateAccount.setAlignment(Pos.BOTTOM_RIGHT);
        hbCreateAccount.getChildren().add(createAccountButton);
        loginGrid.add(hbCreateAccount, 1, 5);

        // Add Status Label
        Label statusLabel = new Label("");
        statusLabel.getStyleClass().add("status-label");
        statusLabel.setFont(robotoRegular);
        loginGrid.add(statusLabel, 1, 6);

        // Set login button action
        loginButton.setOnAction(event -> {
            String username = userNameTextField.getText();
            String password = passwordTextField.getText();

            if (currentUser != null) {
                // A user is already logged in
                statusLabel.setText("Veuillez Déconnecter");
            } else {
                Pair<Boolean, User> loginSuccessful = User.seConnecter(username, password);
                if (loginSuccessful.getKey()) {
                    // Login successful
                    statusLabel.setText("Connexion réussie!");
                    currentUser = loginSuccessful.getValue();
                    Stage currentStage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sidebar.fxml"));

                    try {
                        Parent root = fxmlLoader.load();
                        SideBar controller = fxmlLoader.getController();
                        controller.setUtilisateur(currentUser);
                        controller.setUserNameLabel(currentUser.getPseudo());
                        // Create a new scene with the loaded FXML root
                        Scene scene = new Scene(root);
                        // Set the scene for the current stage
                        currentStage.setScene(scene);
                        currentStage.setOnCloseRequest(event2 -> {
                            if (controller.getUtilisateur().getPlanning() != null) {
                                System.out.println("on a get ce planning: c le notre " + controller.getUtilisateur().getPlanning().getPeriode());
                            }
                            // Show confirmation alert
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Sauvegarder votre activité dans l'application");
                            alert.setHeaderText("Voulez-vous sauvegarder avant de quitter l'application?");
                            alert.setContentText("Cliquez sur OK pour enregistrer ou sur Annuler pour annuler vos modifications.");

                            ButtonType saveButton = new ButtonType("OK");
                            ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
                            alert.getButtonTypes().setAll(saveButton, cancelButton);
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == saveButton) {
                                User.saveUpdateUsertoFile(controller.getUtilisateur());
                            } else {
                                // User clicked Cancel button, close application
                                Platform.exit();
                            }
                        });
                        currentStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Login failed
                    statusLabel.setText("username ou mot de passe incorrect.");
                }
            }
        });

        // Set create account button action
        createAccountButton.setOnAction(event -> {
            String username = userNameTextField.getText();
            String password = passwordTextField.getText();

            boolean accountCreated = User.creerCompte(username, password);
            if (accountCreated) {
                // Account creation successful
                statusLabel.setText("Compte créé avec succès.");
            } else {
                // Account creation failed
                statusLabel.setText("Username déjà pris.");
            }
        });
        setCurrentUser(currentUser);
        return loginGrid;
    }

}
