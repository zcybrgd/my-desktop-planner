package com.example.demo;

import com.example.demo.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

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
    private Spinner<Integer> minNumTasksSpinner;


}
