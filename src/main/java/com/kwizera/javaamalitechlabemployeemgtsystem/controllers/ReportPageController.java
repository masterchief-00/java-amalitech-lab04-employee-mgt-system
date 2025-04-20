package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;
import com.kwizera.javaamalitechlabemployeemgtsystem.session.SessionManager;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.MainUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.UUID;

public class ReportPageController {

    SessionManager<UUID> instance = SessionManager.getInstance();
    private EmployeeDatabase<UUID> database;
    MainUtil util = new MainUtil();

    @FXML
    public AnchorPane scrollPane;

    @FXML
    public TextArea reportTextArea;

    @FXML
    private void initialize() {
        database = instance.getDatabase();

        if (database == null) {
            util.displayError("Database initialization failed, please try again later");
            return;
        } else {
            String report = database.getReport();
            reportTextArea.textProperty().setValue(report);
        }
    }
}
