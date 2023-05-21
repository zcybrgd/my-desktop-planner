package com.example.demo;

import com.example.demo.interfacesGraphiques.loginpage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application{

    private static String fileNameUsers = "users.bin";

    public static String getFileNameUsers() {
        return fileNameUsers;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("User Login");
        loginpage page = new loginpage();
        GridPane loginGrid = page.creerPageLogin();
        Scene loginScene = new Scene(loginGrid, 900, 600);
        loginScene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}