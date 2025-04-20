package com.kwizera.javaamalitechlabemployeemgtsystem.controllers;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.Employee;
import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;
import com.kwizera.javaamalitechlabemployeemgtsystem.session.SessionManager;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.InputValidationUtil;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.MainUtil;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.SafeDoubleStringConverterUtil;
import com.kwizera.javaamalitechlabemployeemgtsystem.utils.SafeIntegerStringConverterUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MainPageController {

    SessionManager<UUID> instance = SessionManager.getInstance();
    private EmployeeDatabase<UUID> database;
    MainUtil util = new MainUtil();
    InputValidationUtil inputValidationUtil = new InputValidationUtil();

    DecimalFormat formatter = new DecimalFormat("#,###.00");

    @FXML
    public Button salaryRaiseBtn;

    @FXML
    public Button reportBtn;

    @FXML
    Button resetBtn;

    @FXML
    public Button avgSalaryBtn;

    @FXML
    public Button topEarnersBtn;

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
    private void onResetClicked() {
        // resets the table view component to default all employees display
        database.reset();
    }

    @FXML
    private void onConsoleReportClicked() throws IOException {
        // loads and displays report screen
        util.displayModularScene("/com/kwizera/javaamalitechlabemployeemgtsystem/report.fxml", reportBtn, "EMS | Report");
    }

    @FXML
    private void onAvgSalaryClicked() {
        // displays dialog box to collect department input
        Optional<String> result = util.departmentAvgSalaryDialogBox();
        result.ifPresent(dept -> {
            // if the department was provided, use is to calculate average
            double avg = database.calculateAverageSalaryByDepartment(dept);
            util.displayConfirmation("The average for the " + dept + " department is " + formatter.format(avg) + " RWF.");
        });
    }

    @FXML
    private void onTopEarnersClicked() {
        // displays dialog box to collect user's input about the number of top earners to retrieve
        Optional<Integer> result = util.topEarnersDialogBox();

        result.ifPresent(N -> {
            // if input provided invokes the method from the database instance
            List<Employee<UUID>> list = database.getTopEarners(N);

            // updates the table view
            tableData.clear();
            tableData.addAll(list);
        });
    }

    @FXML
    private void onSalaryRaiseClicked() {

        // dialog box to collect the min salary and max salary inputs
        Optional<Pair<Double, Double>> result = util.salaryRaiseDialogBox();
        result.ifPresent(pair -> {
            // extracting the inputs from the pair variable
            double minScore = pair.getKey();
            double increaseScore = pair.getValue();

            // if the operations is successful, the method returns the number of records affected
            long updatedCount = database.giveRaiseToTopPerformers(minScore, increaseScore);

            // confirmation pop up
            util.displayConfirmation("Salary for " + updatedCount + " employees updated");

        });
    }

    @FXML
    public void onAddNewClicked() throws IOException {
        // loads and displays add employee UI
        util.displayModularScene("/com/kwizera/javaamalitechlabemployeemgtsystem/add_employee.fxml", addNewBtn, "EMS | Add a new employee");
    }

    @FXML
    private void initialize() {
        database = instance.getDatabase();

        if (database == null) {
            util.displayError("Database initialization failed, please try again later");
            return;
        } else {
            tableData = database.getAllEmployees(); // loads and initializes the table with all employees
            setUpTableColumns();
            loadInitialTableData();
            setUpListeners();
            setTableColumnsForEditing();
        }
    }

    // sets initial configurations for the table columns
    private void setUpTableColumns() {
        // bind columns with the employee class attributes
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("active"));
    }

    // initializes the table view and the drop-down list controls
    private void loadInitialTableData() {
        // initialize table with all employees
        employeeTable.setItems(tableData);

        // create filter and sort drop down menus
        sortCombo.getItems().addAll("Experience", "Salary", "Performance");
        filterCombo.getItems().addAll("Performance", "Department", "Salary range");
    }

    // adds listeners to some inputs to track user interactions
    private void setUpListeners() {
        // define actions on filter selections
        filterCombo.setOnAction(event -> {
            String selectedFilter = filterCombo.getValue();

            if (selectedFilter == null) return;

            switch (selectedFilter) {
                case "Salary range":
                    Optional<Pair<Double, Double>> result = util.salaryRangeDialogBox();
                    result.ifPresent(range -> {
                        double min = range.getKey();
                        double max = range.getValue();

                        List<Employee<UUID>> list = database.getEmployeeBySalaryRange(min, max);
                        tableData.clear();
                        tableData.addAll(list);
                    });
                    break;
                case "Performance":
                    Optional<Double> ratingDialogBoxResult = util.performanceRatingDialogBox();

                    ratingDialogBoxResult.ifPresent(rating -> {
                        double minRating = rating;

                        List<Employee<UUID>> list = database.getEmployeeByPerformanceRating(minRating);
                        tableData.clear();
                        tableData.addAll(list);
                    });
                    break;
                case "Department":
                    Optional<String> deptDialogResult = util.departmentFilterDialogBox();

                    deptDialogResult.ifPresent(department -> {
                        List<Employee<UUID>> list = database.getEmployeesByDepartment(department);
                        tableData.clear();
                        tableData.addAll(list);
                    });
                    break;
                default:
                    break;
            }
        });

        // display employee details by selecting from table
        employeeTable.getSelectionModel().selectedItemProperty().addListener(((obs, oldSelection, selected) -> {
            if (selected != null) {
                updateEmployeeDetailsPane();
            }
        }));

        // real time searching employee by name
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Employee<UUID>> result = database.getEmployeeBySearchTerm(newValue);

            tableData.clear();
            tableData.addAll(result);
        });

        // define actions on sort operations
        sortCombo.setOnAction(event -> {
            String selectedSort = sortCombo.getValue();

            if (selectedSort == null) return;

            switch (selectedSort) {
                case "Experience":
                    List<Employee<UUID>> byExperienceList = database.sortByExperience();
                    tableData.clear();
                    tableData.addAll(byExperienceList.reversed());
                    break;
                case "Salary":
                    List<Employee<UUID>> bySalaryList = database.sortBySalary();
                    tableData.clear();
                    tableData.addAll(bySalaryList.reversed());
                    break;
                case "Performance":
                    List<Employee<UUID>> byPerformanceList = database.sortByPerformance();
                    tableData.clear();
                    tableData.addAll(byPerformanceList.reversed());
                    break;
                default:
                    break;
            }
        });
    }

    // adds configurations to table cells to allow direct editing from UI interaction(double clicking)
    private void setTableColumnsForEditing() {
        List<String> departments = List.of("Engineering", "Marketing", "Sales", "HR");
        ObservableList<String> departmentOptions = FXCollections.observableArrayList(departments);

        // defining input components for each column in case of editing
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        departmentCol.setCellFactory(ComboBoxTableCell.forTableColumn(departmentOptions));
        ratingCol.setCellFactory(TextFieldTableCell.forTableColumn(new SafeDoubleStringConverterUtil()));
        statusCol.setCellFactory(CheckBoxTableCell.forTableColumn(statusCol));

        // number formatting for salary
        salaryCol.setCellFactory(col -> new TextFieldTableCell<Employee<UUID>, Double>(new SafeDoubleStringConverterUtil()) {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // adding text formatting to experience column
        experienceCol.setCellFactory(col -> new TextFieldTableCell<Employee<UUID>, Integer>(new SafeIntegerStringConverterUtil()) {
            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " years");
                }
            }
        });

        statusCol.setCellValueFactory(cellData -> {
            Employee<UUID> employee = cellData.getValue();
            return new SimpleBooleanProperty(employee.isActive());
        });

        nameCol.setOnEditCommit(event -> {
            Employee<UUID> emp = event.getRowValue();
            String newName = event.getNewValue();
            if (!inputValidationUtil.invalidNames(newName)) {
                emp.setName(newName);
                if (database.updateEmployeeDetails(emp.getEmployeeId(), "name", emp)) {
                    util.displayConfirmation("Employee data updated successfully");
                    updateEmployeeDetailsPane();
                } else {
                    util.displayError("Error: employee not updated");
                    emp.setName(event.getOldValue());
                }
            } else {
                util.displayError("Error: Invalid names");
                emp.setName(event.getOldValue());
            }
        });

        salaryCol.setOnEditCommit(event -> {
            Employee<UUID> emp = event.getRowValue();
            Double newSalary = event.getNewValue();

            if (newSalary == null) {
                util.displayError("Error: Invalid value. Employee not updated");
                emp.setSalary(event.getOldValue());
                return;
            }

            if (!inputValidationUtil.invalidSalary(String.valueOf(newSalary))) {
                emp.setSalary(newSalary);
                if (database.updateEmployeeDetails(emp.getEmployeeId(), "salary", emp)) {
                    util.displayConfirmation("Employee data updated successfully");
                    updateEmployeeDetailsPane();
                } else {
                    util.displayError("Error: employee not updated");
                    emp.setSalary(event.getOldValue());
                }
            } else {
                util.displayError("Error: Invalid value. Employee not updated");
                emp.setSalary(event.getOldValue());
            }
        });

        departmentCol.setOnEditCommit(event -> {
            Employee<UUID> emp = event.getRowValue();
            String newDpt = event.getNewValue();
            if (newDpt != null || !newDpt.equals("None")) {
                emp.setDepartment(newDpt);
                if (database.updateEmployeeDetails(emp.getEmployeeId(), "department", emp)) {
                    util.displayConfirmation("Employee data updated successfully");
                    updateEmployeeDetailsPane();
                } else {
                    util.displayError("Error: employee not updated");
                    emp.setDepartment(event.getOldValue());
                }
            } else {
                util.displayError("Error: Invalid department. Employee not updated");
                emp.setDepartment(event.getOldValue());
            }
        });

        ratingCol.setOnEditCommit(event -> {
            Employee<UUID> emp = event.getRowValue();

            Double newRating = event.getNewValue();

            if (newRating == null) {
                util.displayError("Error: Invalid rating. Employee not updated");
                emp.setPerformanceRating(event.getOldValue());
                return;
            }

            if (!inputValidationUtil.invalidRating(String.valueOf(newRating))) {
                emp.setPerformanceRating(newRating);
                if (database.updateEmployeeDetails(emp.getEmployeeId(), "performanceRating", emp)) {
                    util.displayConfirmation("Employee data updated successfully");
                    updateEmployeeDetailsPane();
                } else {
                    util.displayError("Error: employee not updated");
                    emp.setPerformanceRating(event.getOldValue());
                }
            } else {
                util.displayError("Error: Invalid rating. Employee not updated");
                emp.setPerformanceRating(event.getOldValue());
            }
        });

        experienceCol.setOnEditCommit(event -> {
            Employee<UUID> emp = event.getRowValue();

            Integer newExp = event.getNewValue();

            if (newExp == null) {
                util.displayError("Error: Invalid input. Employee not updated");
                emp.setYearsOfExperience(event.getOldValue());
                return;
            }

            if (!inputValidationUtil.invalidExperienceYears(String.valueOf(newExp))) {
                emp.setYearsOfExperience(newExp);
                if (database.updateEmployeeDetails(emp.getEmployeeId(), "yearsOfExperience", emp)) {
                    util.displayConfirmation("Employee data updated successfully");
                    updateEmployeeDetailsPane();
                } else {
                    util.displayError("Error: employee not updated");
                    emp.setYearsOfExperience(event.getOldValue());
                }
            } else {
                util.displayError("Error: Invalid input. Employee not updated");
                emp.setYearsOfExperience(event.getOldValue());
            }

        });

        statusCol.setCellValueFactory(cellData -> {
            Employee<UUID> employee = cellData.getValue();
            SimpleBooleanProperty property = new SimpleBooleanProperty(employee.isActive());

            property.addListener((obs, wasSelected, isNowSelected) -> {

                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Are you sure you want to change status of " + employee.getName() + "?",
                        ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("Confirm");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        employee.setActive(isNowSelected);
                        util.displayConfirmation("Employee status updated");
                        updateEmployeeDetailsPane();
                    }
                });
            });

            return property;
        });
    }

    // vbox to display details of the selected employee from the table view
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

    // creating a remove button and adding method to it that remove an employee
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

    // in case an employee was removed, this method selects the next record in the table(if possible)
    private void selectNext(int removedIndex) {
        if (!tableData.isEmpty()) {
            int nextIndex = Math.min(removedIndex, tableData.size() - 1); // stay in bounds
            employeeTable.getSelectionModel().select(nextIndex);
        }
    }

    // in case there is an update operation refreshes selected employee details
    private void updateEmployeeDetailsPane() {
        Employee<UUID> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            employeeDetailsPane.getChildren().clear();

            VBox detailsBox = getDetailsBox(selected);

            employeeDetailsPane.getChildren().add(detailsBox);
        }
    }

}


