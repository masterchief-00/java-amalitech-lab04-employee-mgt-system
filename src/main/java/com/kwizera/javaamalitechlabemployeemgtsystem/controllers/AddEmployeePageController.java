package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.Employee;
import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;
import com.kwizera.javaamalitechlabemployeemgtsystem.session.SessionManager;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.InputValidationUtil;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.MainUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class AddEmployeePageController {
    SessionManager<UUID> instance = SessionManager.getInstance();
    private EmployeeDatabase<UUID> database;
    MainUtil util = new MainUtil();
    InputValidationUtil inputValidationUtil = new InputValidationUtil();

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
    private void onConfirmClicked() {
        String names = nameInput.getText();
        String salary = salaryInput.getText();
        String department = selectDepartmentInput.getValue();
        String experience = experienceInput.getText();
        String rating = ratingInput.getText();
        boolean isValid = true;

        if (inputValidationUtil.invalidNames(names)) {
            nameErrorLabel.setText("Invalid names");
            nameErrorLabel.setVisible(true);
            nameInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else if (inputValidationUtil.invalidSalary(salary)) {
            salaryErrorLabel.setText("Invalid number for salary");
            salaryErrorLabel.setVisible(true);
            salaryInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else if (department == null || department.equals("None")) {
            departmentErrorLabel.setText("Please select department");
            departmentErrorLabel.setVisible(true);
            selectDepartmentInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else if (inputValidationUtil.invalidExperienceYears(experience)) {
            experienceErrorLabel.setText("Invalid input for years of experience");
            experienceErrorLabel.setVisible(true);
            experienceInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else if (inputValidationUtil.invalidRating(rating)) {
            ratingErrorLabel.setText("Invalid input for performance rating");
            ratingErrorLabel.setVisible(true);
            ratingInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (!isValid) {
            return;
        } else {
            try {
                UUID uuid = UUID.randomUUID();
                double salaryDbl = Double.parseDouble(salary);
                int experienceInt = Integer.parseInt(experience);
                double ratingDbl = Double.parseDouble(rating);
                Employee<UUID> newEmployee = new Employee<>(uuid, names, department, salaryDbl, ratingDbl, experienceInt, true);

                if (database.addEmployee(newEmployee)) {
                    util.displayConfirmation("Employee added successfully");
                    submitEmployeeBtn.setDisable(true);
                } else {
                    util.displayError("Employee not added, key/ID provided already exists");
                }

            } catch (RuntimeException e) {
                util.displayError("Employee not added, something went wrong");
            }
        }
    }

    public void onCancelClicked() throws IOException {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void initialize() {
        database = instance.getDatabase();

        if (database == null) {
            util.displayError("Database initialization failed, please try again later");
            return;
        } else {
            selectDepartmentInput.getItems().addAll("Engineering", "Marketing", "Sales", "HR");
        }
    }
}
