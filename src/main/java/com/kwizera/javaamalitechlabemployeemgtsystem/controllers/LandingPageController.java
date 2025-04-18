package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;
import com.kwizera.javaamalitechlabemployeemgtsystem.session.SessionManager;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.Util;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.UUID;

public class LandingPageController {
    Util util = new Util();

    @FXML
    private Button startBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private void onStartClicked() throws IOException {
        util.switchScene("/com/kwizera/javaamalitechlabemployeemgtsystem/main_page.fxml", startBtn, "EMS | Home");
    }

    @FXML
    private void onExitClicked() {
        Platform.exit();
    }

    @FXML
    private void initialize() {
        EmployeeDatabase<UUID> db = new EmployeeDatabase<>();
        SessionManager<UUID> session = SessionManager.getInstance();
        session.setDatabase(db);
    }
}
