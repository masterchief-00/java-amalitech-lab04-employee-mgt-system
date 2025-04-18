package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddEmployeePageController {


    @FXML
    public TextField nameInput;
    @FXML
    public Label nameErrorLabel;
    @FXML
    public TextField salaryInput;
    @FXML
    public Label salaryErrorLabel;
    @FXML
    public Label departmentErrorLabel;
    @FXML
    public ComboBox<String> selectDepartmentInput;
    @FXML
    public TextField experienceInput;
    @FXML
    public Label experienceErrorLabel;
    @FXML
    public TextField ratingInput;
    @FXML
    public Label ratingErrorLabel;
    @FXML
    public Button submitEmployeeBtn;
    @FXML
    public Button cancelBtn;

    @FXML
    private void initialize() {
    }
}
