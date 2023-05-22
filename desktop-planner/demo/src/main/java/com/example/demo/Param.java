package com.example.demo;

import com.example.demo.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class Param {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private ColorPicker categoryColorPicker;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField minDurationTextField;

    @FXML
    private TextField minTasksField;

    @FXML
    void AjouterNvlCat(ActionEvent event) {

    }

    @FXML
    void modifierDurMini(ActionEvent event) {
        String input = minDurationTextField.getText();
        input = "PT" + input;
        try {
            Duration duration = Duration.parse(input);
            System.out.println("Valid duration: " + duration);
            user.setMinDureeCreneau(duration);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid duration");
            // Display an error message or handle the invalid input
        }
    }

    @FXML
    void modifierMinTask(ActionEvent event) {
        String input = minTasksField.getText();
        try {
            int minTasks = Integer.parseInt(input);
            System.out.println("Valid min tasks: " + minTasks);
            user.setMinTaskPerDay(minTasks);
        } catch (NumberFormatException e) {
            System.out.println("Invalid min tasks");
            // Display an error message or handle the invalid input
        }
    }


}
