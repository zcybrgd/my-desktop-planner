package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class Param {
    @FXML
    private ColorPicker categoryColorPicker;

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField minDurationTextField;

    @FXML
    private Spinner<Integer> minNumTasksSpinner;

}
