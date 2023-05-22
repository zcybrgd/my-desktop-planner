package com.example.demo;

import com.example.demo.interfacesGraphiques.loginpage;
import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

public class SideBar  {

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
        paramItem.setOnAction(calendrier->{
            showPage("param.fxml");
        });
        projectsItem.setOnAction((mesprojets->{
            showPage("projets.fxml");
        }));
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
        statItem.setOnAction(stats->{
            showPage("stats.fxml");
        });
        historyItem.setOnAction(histo->{
            showPage("historique.fxml");
        });
    }


    public void showPage(String fxmlFile) {
        // Load the FXML file for the page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent page = fxmlLoader.load();
            if(mainPage.getChildren().size()==3){
                mainPage.getChildren().remove(0);
            }
            mainPage.getChildren().add(0,page); // Add the new page
            if(fxmlFile.equals("calendrier.fxml")){
                Calendrier controller = fxmlLoader.getController();
                controller.setUser(utilisateur);
            }
            if(fxmlFile.equals("param.fxml")){
                Param controller = fxmlLoader.getController();
                controller.setUser(utilisateur);
            }
            if(fxmlFile.equals("projets.fxml")){
                Projets controller = fxmlLoader.getController();
                controller.setUser(utilisateur);
            }
            if(fxmlFile.equals("stats.fxml")){
                Stats controller = fxmlLoader.getController();
                controller.setUser(utilisateur);
            }
            if(fxmlFile.equals("historique.fxml")){
                Historique controller = fxmlLoader.getController();
                controller.setUser(utilisateur);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
