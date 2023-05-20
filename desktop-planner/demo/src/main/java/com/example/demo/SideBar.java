package com.example.demo;

import com.example.demo.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class SideBar {

    private User utilisateur;

    @FXML
    private ImageView calendarIcon;

    @FXML
    private Button calendarItem;

    @FXML
    private AnchorPane currentPage;

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

    public void setUserNameLabel(String name) {
        System.out.println("the name that reached : " + name);
        this.userNameLabel = new Label(name);
    }

    @FXML
    void deconnecter(ActionEvent event) {

    }

}
