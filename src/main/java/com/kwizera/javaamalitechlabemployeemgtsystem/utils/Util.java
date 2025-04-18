package com.kwizera.javaamalitechlabemployeemgtsystem.utils;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

public class Util {

    public void displayModularScene(String fxmlFile, Button sourceButton, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner((Stage) sourceButton.getScene().getWindow());

        stage.setResizable(false);
        stage.show();
    }

    public void switchScene(String fxmlFile, Button sourceButton, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
    }

    public void departmentFilterDialogBox() {
        Dialog<String> departmentFilterDialog = new Dialog<>();
        departmentFilterDialog.setTitle("Filter by department");
        departmentFilterDialog.setHeaderText("Select department");

        // buttons
        ButtonType departmentFilterButton = new ButtonType("Filter", ButtonBar.ButtonData.OK_DONE);
        departmentFilterDialog.getDialogPane().getButtonTypes().addAll(departmentFilterButton, ButtonType.CANCEL);

        GridPane departmentFilterDialogGrid = new GridPane();
        departmentFilterDialogGrid.setHgap(10);
        departmentFilterDialogGrid.setVgap(10);
        departmentFilterDialogGrid.setPadding(new Insets(20, 150, 10, 10));

        Label departmentFilterErrorLabel = new Label("Invalid input, please input valid numbers");
        departmentFilterErrorLabel.setStyle("-fx-text-fill: red;");
        departmentFilterErrorLabel.setWrapText(true);
        departmentFilterErrorLabel.setMinSize(100, 20);
        departmentFilterErrorLabel.setVisible(false);

        ComboBox<String> departmentSelectInput = new ComboBox<>();
        departmentSelectInput.getItems().addAll("Human Resources", "IT", "Finance");
        departmentSelectInput.setValue("None");

        departmentFilterDialogGrid.add(new Label("Department: "), 0, 0);
        departmentFilterDialogGrid.add(departmentSelectInput, 1, 0);
        departmentFilterDialogGrid.add(departmentFilterErrorLabel, 0, 2, 2, 1);

        departmentFilterDialog.getDialogPane().setContent(departmentFilterDialogGrid);

        // input validation
        Node departmentFilterButtonType = departmentFilterDialog.getDialogPane().lookupButton(departmentFilterButton);
        departmentFilterButtonType.setDisable(true);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String departmentText = departmentSelectInput.getValue();

            if (departmentText.equals("None")) {
                departmentFilterErrorLabel.setText("Please select a department");
                departmentFilterErrorLabel.setVisible(true);
                departmentFilterButtonType.setDisable(true);
            } else {
                departmentFilterErrorLabel.setText("");
                departmentFilterButtonType.setDisable(false);
            }
        };

        departmentSelectInput.valueProperty().addListener(validationListener);

        departmentFilterDialog.setResultConverter(dialogButton -> {
            if (dialogButton == departmentFilterButton) {
                return departmentSelectInput.getValue();
            }
            return null;
        });

        Optional<String> result = departmentFilterDialog.showAndWait();

        result.ifPresent(department -> {
            // invoke employeedb method
        });

    }

    public void performanceRatingDialogBox() {
        Dialog<Double> ratingDialog = new Dialog<>();
        ratingDialog.setTitle("Filter by Performance rating");
        ratingDialog.setHeaderText("Enter minimum rating");

        Label ratingErrorLabel = new Label("Invalid input, please input valid numbers");
        ratingErrorLabel.setStyle("-fx-text-fill: red;");
        ratingErrorLabel.setWrapText(true);
        ratingErrorLabel.setMinSize(100, 20);
        ratingErrorLabel.setVisible(false);

        // buttons
        ButtonType ratingFilterButtonType = new ButtonType("Filter", ButtonBar.ButtonData.OK_DONE);
        ratingDialog.getDialogPane().getButtonTypes().addAll(ratingFilterButtonType, ButtonType.CANCEL);

        GridPane ratingGrid = new GridPane();
        ratingGrid.setHgap(10);
        ratingGrid.setVgap(10);
        ratingGrid.setPadding(new Insets(20, 150, 10, 10));

        // min rating input
        TextField minRatingInput = new TextField();
        minRatingInput.setPromptText("Minimum rating");

        ratingGrid.add(new Label("Min rating:"), 0, 0);
        ratingGrid.add(minRatingInput, 1, 0);
        ratingGrid.add(ratingErrorLabel, 0, 1, 2, 1);

        ratingDialog.getDialogPane().setContent(ratingGrid);

        // input validation
        Node ratingFilterButton = ratingDialog.getDialogPane().lookupButton(ratingFilterButtonType);
        ratingFilterButton.setDisable(true);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String minRatingText = minRatingInput.getText();

            try {
                double minRating = Double.parseDouble(minRatingText);

                if (minRating < 0 || minRating > 5) {
                    ratingErrorLabel.setText("Please input a number in range of 0 to 5");
                    ratingErrorLabel.setVisible(true);
                    ratingFilterButton.setDisable(true);
                } else {
                    ratingErrorLabel.setText("");
                    ratingFilterButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                if (!minRatingText.isEmpty()) {
                    ratingErrorLabel.setText("Invalid numbers");
                    ratingErrorLabel.setVisible(true);
                } else {
                    ratingErrorLabel.setText("");
                }

                ratingFilterButton.setDisable(true);
            }
        };

        minRatingInput.textProperty().addListener(validationListener);

        ratingDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ratingFilterButtonType) {
                try {
                    return Double.parseDouble(minRatingInput.getText());
                } catch (NumberFormatException e) {
                    ratingErrorLabel.setVisible(true);
                }
            }
            return null;
        });

        Optional<Double> ratingDialogBoxResult = ratingDialog.showAndWait();

        ratingDialogBoxResult.ifPresent(rating -> {
            double minRating = rating;

            // invoke employeedb method
        });
    }

    public void salaryRangeDialogBox() {
        Dialog<Pair<Double, Double>> salaryRangeDialog = new Dialog<>();
        salaryRangeDialog.setTitle("Filter by Salary Range");
        salaryRangeDialog.setHeaderText("Enter minimum and maximum salary");

        // buttons
        ButtonType salaryRangeFilterButton = new ButtonType("Filter", ButtonBar.ButtonData.OK_DONE);
        salaryRangeDialog.getDialogPane().getButtonTypes().addAll(salaryRangeFilterButton, ButtonType.CANCEL);

        GridPane salaryRangeDialogGrid = new GridPane();
        salaryRangeDialogGrid.setHgap(10);
        salaryRangeDialogGrid.setVgap(10);
        salaryRangeDialogGrid.setPadding(new Insets(20, 150, 10, 10));

        // min and max inputs
        TextField minSalaryInput = new TextField();
        minSalaryInput.setPromptText("Minimum salary");

        TextField maxSalaryInput = new TextField();
        maxSalaryInput.setPromptText("Maximum salary");

        Label salaryRangeErrorLabel = new Label("Invalid input, please input valid numbers");
        salaryRangeErrorLabel.setStyle("-fx-text-fill: red;");
        salaryRangeErrorLabel.setWrapText(true);
        salaryRangeErrorLabel.setMinSize(100, 20);
        salaryRangeErrorLabel.setVisible(false);

        salaryRangeDialogGrid.add(new Label("Min Salary:"), 0, 0);
        salaryRangeDialogGrid.add(minSalaryInput, 1, 0);
        salaryRangeDialogGrid.add(new Label("Max Salary:"), 0, 1);
        salaryRangeDialogGrid.add(maxSalaryInput, 1, 1);
        salaryRangeDialogGrid.add(salaryRangeErrorLabel, 0, 2, 2, 1);

        salaryRangeDialog.getDialogPane().setContent(salaryRangeDialogGrid);

        // input validation
        Node salaryFilterButton = salaryRangeDialog.getDialogPane().lookupButton(salaryRangeFilterButton);
        salaryFilterButton.setDisable(true);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String minText = minSalaryInput.getText().trim();
            String maxText = maxSalaryInput.getText().trim();

            try {
                double min = Double.parseDouble(minText);
                double max = Double.parseDouble(maxText);

                if (min < 0 || max < 0) {
                    salaryRangeErrorLabel.setText("Negative numbers are not allowed");
                    salaryRangeErrorLabel.setVisible(true);
                    salaryFilterButton.setDisable(true);
                } else if (min > max) {
                    salaryRangeErrorLabel.setText("Minimum salary must be less than maximum salary");
                    salaryRangeErrorLabel.setVisible(true);
                    salaryFilterButton.setDisable(true);
                } else {
                    salaryRangeErrorLabel.setText("");
                    salaryFilterButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                if (!minText.isEmpty() && !maxText.isEmpty()) {
                    salaryRangeErrorLabel.setText("Invalid numbers");
                    salaryRangeErrorLabel.setVisible(true);
                } else {
                    salaryRangeErrorLabel.setText("");
                }

                salaryFilterButton.setDisable(true);
            }
        };

        minSalaryInput.textProperty().addListener(validationListener);
        maxSalaryInput.textProperty().addListener(validationListener);

        salaryRangeDialog.setResultConverter(dialogButton -> {
            if (dialogButton == salaryRangeFilterButton) {
                try {
                    double min = Double.parseDouble(minSalaryInput.getText());
                    double max = Double.parseDouble(maxSalaryInput.getText());

                    return new Pair<>(min, max);
                } catch (NumberFormatException e) {
                    salaryRangeErrorLabel.setVisible(true);
                }
            }
            return null;
        });

        Optional<Pair<Double, Double>> result = salaryRangeDialog.showAndWait();

        result.ifPresent(range -> {
            double min = range.getKey();
            double max = range.getValue();

            // invoke employeedb method
        });
    }
}
