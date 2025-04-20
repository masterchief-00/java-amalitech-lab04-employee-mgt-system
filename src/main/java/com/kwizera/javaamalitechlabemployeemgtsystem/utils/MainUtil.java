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
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

// contains methods to spawn dialog boxes and switch between screens
public class MainUtil {

    // displays a modular window
    public void displayModularScene(String fxmlFile, Button sourceButton, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner((Stage) sourceButton.getScene().getWindow());

        stage.setResizable(false);
        stage.setOnShown(e -> {
            Window owner = stage.getOwner();
            stage.setX(owner.getX() + (owner.getWidth() - stage.getWidth()) / 2);
            stage.setY(owner.getY() + (owner.getHeight() - stage.getHeight()) / 3);
        });

        stage.show();
    }

    // switches to a different window
    public void switchScene(String fxmlFile, Button sourceButton, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
    }

    public Optional<String> departmentAvgSalaryDialogBox() {
        Dialog<String> departmentAvgSalaryDialog = new Dialog<>();
        departmentAvgSalaryDialog.setTitle("Calculate average salary by department");
        departmentAvgSalaryDialog.setHeaderText("Select department");

        // buttons
        ButtonType departmentAvgButton = new ButtonType("Get average", ButtonBar.ButtonData.OK_DONE);
        departmentAvgSalaryDialog.getDialogPane().getButtonTypes().addAll(departmentAvgButton, ButtonType.CANCEL);

        GridPane departmentAvgSalaryDialogGrid = new GridPane();
        departmentAvgSalaryDialogGrid.setHgap(10);
        departmentAvgSalaryDialogGrid.setVgap(10);
        departmentAvgSalaryDialogGrid.setPadding(new Insets(20, 150, 10, 10));

        Label departmentAvgSalaryErrorLabel = new Label("Invalid input, please input valid numbers");
        departmentAvgSalaryErrorLabel.setStyle("-fx-text-fill: red;");
        departmentAvgSalaryErrorLabel.setWrapText(true);
        departmentAvgSalaryErrorLabel.setMinSize(100, 20);
        departmentAvgSalaryErrorLabel.setVisible(false);

        ComboBox<String> departmentSelectInput = new ComboBox<>();
        departmentSelectInput.getItems().addAll("Engineering", "Marketing", "Sales", "HR");
        departmentSelectInput.setValue("None");

        departmentAvgSalaryDialogGrid.add(new Label("Department: "), 0, 0);
        departmentAvgSalaryDialogGrid.add(departmentSelectInput, 1, 0);
        departmentAvgSalaryDialogGrid.add(departmentAvgSalaryErrorLabel, 0, 2, 2, 1);

        departmentAvgSalaryDialog.getDialogPane().setContent(departmentAvgSalaryDialogGrid);

        // input validation
        Node departmentAvgButtonType = departmentAvgSalaryDialog.getDialogPane().lookupButton(departmentAvgButton);
        departmentAvgButtonType.setDisable(true);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String departmentText = departmentSelectInput.getValue();

            if (departmentText.equals("None")) {
                departmentAvgSalaryErrorLabel.setText("Please select a department");
                departmentAvgSalaryErrorLabel.setVisible(true);
                departmentAvgButtonType.setDisable(true);
            } else {
                departmentAvgSalaryErrorLabel.setText("");
                departmentAvgButtonType.setDisable(false);
            }
        };

        departmentSelectInput.valueProperty().addListener(validationListener);

        departmentAvgSalaryDialog.setResultConverter(dialogButton -> {
            if (dialogButton == departmentAvgButton) {
                return departmentSelectInput.getValue();
            }
            return null;
        });

        return departmentAvgSalaryDialog.showAndWait();
    }

    public Optional<Integer> topEarnersDialogBox() {
        Dialog<Integer> topEarnersDialog = new Dialog<>();
        topEarnersDialog.setTitle("Top earners");
        topEarnersDialog.setHeaderText("Enter number to retrieve");

        Label topEarnersErrorLabel = new Label("Invalid input, please input valid numbers");
        topEarnersErrorLabel.setStyle("-fx-text-fill: red;");
        topEarnersErrorLabel.setWrapText(true);
        topEarnersErrorLabel.setMinSize(100, 20);
        topEarnersErrorLabel.setVisible(false);

        // buttons
        ButtonType topEarnersButtonType = new ButtonType("Filter", ButtonBar.ButtonData.OK_DONE);
        topEarnersDialog.getDialogPane().getButtonTypes().addAll(topEarnersButtonType, ButtonType.CANCEL);

        GridPane topEarnerGrid = new GridPane();
        topEarnerGrid.setHgap(10);
        topEarnerGrid.setVgap(10);
        topEarnerGrid.setPadding(new Insets(20, 150, 10, 10));

        // N earners input
        TextField topNEarnersInput = new TextField();
        topNEarnersInput.setPromptText("Number of earners to retrieve");

        topEarnerGrid.add(new Label("Number to retrieve:"), 0, 0);
        topEarnerGrid.add(topNEarnersInput, 1, 0);
        topEarnerGrid.add(topEarnersErrorLabel, 0, 1, 2, 1);

        topEarnersDialog.getDialogPane().setContent(topEarnerGrid);

        // input validation
        Node topEarnerButton = topEarnersDialog.getDialogPane().lookupButton(topEarnersButtonType);
        topEarnerButton.setDisable(true);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String minRatingText = topNEarnersInput.getText();

            try {
                double minRating = Double.parseDouble(minRatingText);

                if (minRating < 0) {
                    topEarnersErrorLabel.setText("Please input a number above zero");
                    topEarnersErrorLabel.setVisible(true);
                    topEarnerButton.setDisable(true);
                } else {
                    topEarnersErrorLabel.setText("");
                    topEarnerButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                if (!minRatingText.isEmpty()) {
                    topEarnersErrorLabel.setText("Invalid numbers");
                    topEarnersErrorLabel.setVisible(true);
                } else {
                    topEarnersErrorLabel.setText("");
                }

                topEarnerButton.setDisable(true);
            }
        };

        topNEarnersInput.textProperty().addListener(validationListener);

        topEarnersDialog.setResultConverter(dialogButton -> {
            if (dialogButton == topEarnersButtonType) {
                try {
                    return Integer.parseInt(topNEarnersInput.getText());
                } catch (NumberFormatException e) {
                    topEarnersErrorLabel.setVisible(true);
                }
            }
            return null;
        });

        return topEarnersDialog.showAndWait();
    }

    public Optional<Pair<Double, Double>> salaryRaiseDialogBox() {
        Dialog<Pair<Double, Double>> salaryRaiseDialog = new Dialog<>();
        salaryRaiseDialog.setTitle("Salary raise");
        salaryRaiseDialog.setHeaderText("Enter the threshold score and salary increase rate");

        // buttons
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        salaryRaiseDialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        GridPane salaryRaiseDialogGrid = new GridPane();
        salaryRaiseDialogGrid.setHgap(10);
        salaryRaiseDialogGrid.setVgap(10);
        salaryRaiseDialogGrid.setPadding(new Insets(20, 150, 10, 10));

        // threshold score and salary increase rate inputs
        TextField thresholdScoreInput = new TextField();
        thresholdScoreInput.setPromptText("Minimum score");

        TextField increaseRateInput = new TextField();
        increaseRateInput.setPromptText("Salary increase rate");

        Label salaryRaiseErrorLabel = new Label("Invalid input, please input valid numbers");
        salaryRaiseErrorLabel.setStyle("-fx-text-fill: red;");
        salaryRaiseErrorLabel.setWrapText(true);
        salaryRaiseErrorLabel.setMinSize(100, 20);
        salaryRaiseErrorLabel.setVisible(false);

        salaryRaiseDialogGrid.add(new Label("Threshold score:"), 0, 0);
        salaryRaiseDialogGrid.add(thresholdScoreInput, 1, 0);
        salaryRaiseDialogGrid.add(new Label("Increase rate(%):"), 0, 1);
        salaryRaiseDialogGrid.add(increaseRateInput, 1, 1);
        salaryRaiseDialogGrid.add(salaryRaiseErrorLabel, 0, 2, 2, 1);

        salaryRaiseDialog.getDialogPane().setContent(salaryRaiseDialogGrid);

        // input validation
        Node salaryRaiseConfirmButton = salaryRaiseDialog.getDialogPane().lookupButton(confirmButton);
        salaryRaiseConfirmButton.setDisable(true);

        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            String minScore = thresholdScoreInput.getText().trim();
            String rate = increaseRateInput.getText().trim();

            try {
                double score = Double.parseDouble(minScore);
                double increaseBy = Double.parseDouble(rate);

                if (score < 0 || increaseBy < 0) {
                    salaryRaiseErrorLabel.setText("Negative numbers are not allowed");
                    salaryRaiseErrorLabel.setVisible(true);
                    salaryRaiseConfirmButton.setDisable(true);
                } else if (score > increaseBy) {
                    salaryRaiseErrorLabel.setText("Minimum salary must be less than maximum salary");
                    salaryRaiseErrorLabel.setVisible(true);
                    salaryRaiseConfirmButton.setDisable(true);
                } else {
                    salaryRaiseErrorLabel.setText("");
                    salaryRaiseConfirmButton.setDisable(false);
                }
            } catch (NumberFormatException e) {
                if (!minScore.isEmpty() && !rate.isEmpty()) {
                    salaryRaiseErrorLabel.setText("Invalid numbers");
                    salaryRaiseErrorLabel.setVisible(true);
                } else {
                    salaryRaiseErrorLabel.setText("");
                }

                salaryRaiseConfirmButton.setDisable(true);
            }
        };

        thresholdScoreInput.textProperty().addListener(validationListener);
        increaseRateInput.textProperty().addListener(validationListener);

        salaryRaiseDialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                try {
                    double thresholdScore = Double.parseDouble(thresholdScoreInput.getText());
                    double increaseRate = Double.parseDouble(increaseRateInput.getText());

                    return new Pair<>(thresholdScore, increaseRate);
                } catch (NumberFormatException e) {
                    salaryRaiseErrorLabel.setVisible(true);
                }
            }
            return null;
        });

        return salaryRaiseDialog.showAndWait();

    }

    public Optional<String> departmentFilterDialogBox() {
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
        departmentSelectInput.getItems().addAll("Engineering", "Marketing", "Sales", "HR");
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

        return departmentFilterDialog.showAndWait();
    }

    public Optional<Double> performanceRatingDialogBox() {
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

        return ratingDialog.showAndWait();
    }

    public Optional<Pair<Double, Double>> salaryRangeDialogBox() {
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

        return salaryRangeDialog.showAndWait();

    }

    // a utility to render error messages
    public void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // utility method to render confirmation messages
    public void displayConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Completed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
