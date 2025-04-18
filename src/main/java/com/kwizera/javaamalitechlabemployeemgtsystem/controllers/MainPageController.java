package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.Employee;
import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;
import com.kwizera.javaamalitechlabemployeemgtsystem.session.SessionManager;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainPageController {

    SessionManager<UUID> instance = SessionManager.getInstance();
    private EmployeeDatabase<UUID> database;
    Util util = new Util();

    DecimalFormat formatter = new DecimalFormat("#,###.00");

    @FXML
    public Label noEmployeeLabel;

    @FXML
    public Button addNewBtn;

    @FXML
    public TextField searchInput;

    @FXML
    private ComboBox<String> sortCombo;

    @FXML
    private ComboBox<String> filterCombo;

    @FXML
    public AnchorPane employeeDetailsPane;

    @FXML
    public TableView<Employee<UUID>> employeeTable;

    @FXML
    TableColumn<Employee<UUID>, String> nameCol;

    @FXML
    TableColumn<Employee<UUID>, String> departmentCol;

    @FXML
    TableColumn<Employee<UUID>, Double> salaryCol;

    @FXML
    TableColumn<Employee<UUID>, Double> ratingCol;

    @FXML
    TableColumn<Employee<UUID>, Integer> experienceCol;

    @FXML
    TableColumn<Employee<UUID>, Boolean> statusCol;

    private ObservableList<Employee<UUID>> tableData = FXCollections.observableArrayList();

    @FXML
    public void onAddNewClicked() throws IOException {
        util.displayModularScene("/com/kwizera/javaamalitechlabemployeemgtsystem/add_employee.fxml", addNewBtn, "Add a new employee");
    }

    @FXML
    void onRemoveClicked() {

    }

    @FXML
    private void initialize() {
        database = instance.getDatabase();

        if (database == null) {
            util.displayError("Database initialization failed, please try again later");
            return;
        } else {

            tableData = database.getAllEmployees();

            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
            salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
            ratingCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
            experienceCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("active"));

            salaryCol.setCellFactory(col -> new TableCell<Employee<UUID>, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));
                    }
                }
            });

            experienceCol.setCellFactory(col -> new TableCell<Employee<UUID>, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item + " years");
                    }
                }
            });

            statusCol.setCellFactory(column -> new TableCell<Employee<UUID>, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "active" : "inactive");
                    }
                }
            });

            employeeTable.setItems(tableData);

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

            employeeTable.getSelectionModel().selectedItemProperty().addListener(((obs, oldSelection, selected) -> {
                if (selected != null) {
                    employeeDetailsPane.getChildren().clear();


                    VBox detailsBox = getDetailsBox(selected);

                    employeeDetailsPane.getChildren().add(detailsBox);
                }
            }));
        }
    }

    private VBox getDetailsBox(Employee<UUID> selectedEmployee) {
        Label name = new Label("Name: " + selectedEmployee.getName());
        Label dept = new Label("Department: " + selectedEmployee.getDepartment());
        Label salary = new Label("Salary: " + formatter.format(selectedEmployee.getSalary()) + " RWF");
        Label rating = new Label("Rating: " + selectedEmployee.getPerformanceRating());
        Label exp = new Label("Experience: " + selectedEmployee.getYearsOfExperience() + " years");
        Label status = new Label("Status: " + (selectedEmployee.isActive() ? "Active" : "Inactive"));
        Button removeEmployeeBtn = getRemoveEmployeeBtn(selectedEmployee);

        name.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        dept.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        rating.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        salary.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        exp.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        status.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");

        VBox detailsBox = new VBox(10, name, dept, salary, rating, exp, status, removeEmployeeBtn);
        detailsBox.setLayoutX(10);
        detailsBox.setLayoutY(10);
        return detailsBox;
    }

    private Button getRemoveEmployeeBtn(Employee<UUID> selectedEmployee) {
        Button removeEmployeeBtn = new Button("Remove this employee");

        removeEmployeeBtn.setOnAction(event -> {
            int removedIndex = employeeTable.getSelectionModel().getSelectedIndex();

            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Are you sure you want to delete " + selectedEmployee.getName() + "?",
                    ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Confirm Deletion");

            alert.showAndWait().ifPresent(response -> {

                if (response == ButtonType.YES) {
                    if (database.removeEmployee(selectedEmployee.getEmployeeId())) {
                        util.displayConfirmation("Employee deleted");
                        selectNext(removedIndex);
                        removeEmployeeBtn.setDisable(true);
                    } else {
                        util.displayError("Employee not deleted");
                        removeEmployeeBtn.setDisable(true);
                    }
                } else {
                    removeEmployeeBtn.setDisable(false);
                }
            });

        });
        return removeEmployeeBtn;
    }

    private void selectNext(int removedIndex) {
        if (!tableData.isEmpty()) {
            int nextIndex = Math.min(removedIndex, tableData.size() - 1); // stay in bounds
            employeeTable.getSelectionModel().select(nextIndex);
        }
    }
}


