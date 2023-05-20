package com.example.demo;

import com.example.demo.interfacesGraphiques.loginpage;
import com.example.demo.user.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SideBar implements Serializable {

    private User utilisateur;

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    @FXML
    private ImageView calendarIcon;

    @FXML
    private Button calendarItem;

    @FXML
    private ImageView historyIcon;

    @FXML
    private Button historyItem;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView logoutIcon;

    @FXML
    private AnchorPane mainPage;

    @FXML
    private Button paramItem;

    @FXML
    private ImageView projectsIcon;

    @FXML
    private Button projectsItem;

    @FXML
    private ImageView settingsIcon;

    @FXML
    private AnchorPane sideIcons;

    @FXML
    private AnchorPane sideItems;

    @FXML
    private Button statItem;

    @FXML
    private ImageView statsIcon;

    @FXML
    private ImageView userIcon;

    @FXML
    private Label userNameLabel;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public User getUtilisateur() {
        return utilisateur;
    }


    public void setUserNameLabel(String name) {
        System.out.println("the name that reached : " + name);
        this.userNameLabel.setText(name);
        calendarItem.setOnAction(calendrier->{
             showPage("calendrier.fxml");
        });
        logoutButton.setOnAction(logout->{
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            // Close the current window
            currentStage.close();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("User Login");
            System.out.println("ahaha");
            loginpage page = new loginpage();
            GridPane loginGrid = page.creerPageLogin();
            Scene loginScene = new Scene(loginGrid, 900, 600);
            loginScene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            primaryStage.setScene(loginScene);
            primaryStage.show();
        });
    }


    public void showPage(String fxmlFile) {
        // Load the FXML file for the page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent page = fxmlLoader.load();
            mainPage.getChildren().add(0,page); // Add the new page
            if(fxmlFile.equals("calendrier.fxml")){
                Calendrier controller = fxmlLoader.getController();
                controller.setUser(utilisateur);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
