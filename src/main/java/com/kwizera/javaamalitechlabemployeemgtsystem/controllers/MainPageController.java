package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import com.kwizera.javaamalitechlabemployeemgtsystem.utils.Util;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public class MainPageController {
    Util util = new Util();

    @FXML
    private ComboBox<String> sortCombo;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    private void initialize() {
        sortCombo.getItems().addAll("Experience", "Salary", "Performance rating");
        filterCombo.getItems().addAll("Performance", "Department", "Salary range");

        filterCombo.setOnAction(event -> {
            String selectedFilter = filterCombo.getValue();

            switch (selectedFilter) {
                case "Salary range":
                    util.salaryRangeDialogBox();
                    break;
                case "Performance":
                    util.performanceRatingDialogBox();
                    break;
                case "Department":
                    util.departmentFilterDialogBox();
                    break;
                default:
                    break;
            }
        });
    }
}
