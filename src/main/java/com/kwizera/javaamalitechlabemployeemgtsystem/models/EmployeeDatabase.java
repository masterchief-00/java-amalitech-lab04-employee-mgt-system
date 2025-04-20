package com.kwizera.javaamalitechlabemployeemgtsystem.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {
    private Map<T, Employee<T>> employeeMap;

    // observable list to allow real time sync with the UI table
    private ObservableList<Employee<T>> employeesList = FXCollections.observableArrayList();

    // creates a database using a hash map on instantiation
    public EmployeeDatabase() {
        this.employeeMap = new HashMap<>();
    }

    // adds an employee to the database and syncs with table list and returns a boolean for feedback
    public boolean addEmployee(Employee<T> employee) {
        if (!employeeMap.containsKey(employee.getEmployeeId())) {
            employeeMap.put(employee.getEmployeeId(), employee);
            employeesList.add(employee); // syncing with the table on UI
            return true;
        } else {
            return false;
        }
    }

    // removes employee from db and syncs with the UI
    public boolean removeEmployee(T employeeId) {
        Employee<T> employeeToDelete = employeeMap.get(employeeId);
        if (employeeMap.remove(employeeId) == null) {
            System.out.println("Employee with " + employeeId + " was not found");
            return false;
        }
        employeesList.remove(employeeToDelete);
        return true;
    }

    // updates employee details and returns boolean for feedback
    public boolean updateEmployeeDetails(T employeeId, String field, Object newValue) {
        Employee<T> employee = employeeMap.get(employeeId);

        if (employee == null) {
            System.out.println("Employee with " + employeeId + " was not found");
            return false;
        }

        // switch case to allow type checking before updating
        switch (field) {
            case "name":
                if (newValue instanceof String) employee.setName((String) newValue);
                break;
            case "department":
                if (newValue instanceof String) employee.setDepartment((String) newValue);
                break;
            case "salary":
                if (newValue instanceof Double) employee.setSalary((Double) newValue);
                break;
            case "performanceRating":
                if (newValue instanceof Double) employee.setPerformanceRating((Double) newValue);
                break;
            case "yearsOfExperience":
                if (newValue instanceof Integer) employee.setYearsOfExperience((Integer) newValue);
                break;
            case "isActive":
                if (newValue instanceof Boolean) employee.setActive((Boolean) newValue);
                break;
            default:
                return false;
        }

        return true;
    }

    // returns observable list to sync with the employee list to the UI
    public ObservableList<Employee<T>> getAllEmployees() {
        return employeesList;
    }

    // retrieves employee by department and returns a list
    public List<Employee<T>> getEmployeesByDepartment(String department) {
        return employeeMap.values()
                .stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    // traverses the employee database as the user types and returns all matching employees as in a list
    public List<Employee<T>> getEmployeeBySearchTerm(String searchTerm) {
        return employeeMap.values()
                .stream()
                .filter(e -> e.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    // traverses the database and uses stream to filter for the salary range provided, has to be reversed on the receiving end for descending order. returns list
    public List<Employee<T>> getEmployeeBySalaryRange(double minSalary, double maxSalary) {
        return employeeMap
                .values()
                .stream()
                .filter(e -> e.getSalary() >= minSalary && e.getSalary() <= maxSalary)
                .collect(Collectors.toList());
    }

    // uses stream to traverse filter employee database against the given minimum rating. returns list
    public List<Employee<T>> getEmployeeByPerformanceRating(double minRating) {
        return employeeMap
                .values()
                .stream()
                .filter(e -> e.getPerformanceRating() >= minRating)
                .collect(Collectors.toList());
    }

    // uses a comparator to sort employees by salary, has to be reversed on the receiving end for descending order
    public List<Employee<T>> sortBySalary() {
        return employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .collect(Collectors.toList());
    }

    // uses a comparator to sort employees by performance rating, has to be reversed on the receiving end for descending order
    public List<Employee<T>> sortByPerformance() {
        return employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Employee::getPerformanceRating))
                .collect(Collectors.toList());
    }

    // uses a comparator to sort employees by years of experience, has to be reversed on the receiving end for descending order
    public List<Employee<T>> sortByExperience() {
        return employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingInt(Employee::getYearsOfExperience))
                .collect(Collectors.toList());
    }

    // uses stream, filter and peek to update salary attribute and return the count of affected records.
    public long giveRaiseToTopPerformers(double thresholdScore, double raiseRate) {
        long updateCount = employeeMap
                .values()
                .stream()
                .filter(e -> e.getPerformanceRating() >= thresholdScore)
                .peek(e -> {
                    double newSalary = e.getSalary() * (1 + raiseRate / 100);
                    e.setSalary(newSalary);
                }).count();

        employeesList.setAll(employeeMap.values()); // syncs with the UI

        return updateCount;
    }

    // uses stream to calculate average salary per department
    public double calculateAverageSalaryByDepartment(String department) {
        return employeeMap
                .values()
                .stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    // uses the input integer type to limit the list of employees previously sorted by salary
    public List<Employee<T>> getTopEarners(Integer N) {
        List<Employee<T>> sortedBySalary = employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .toList();
        return sortedBySalary.reversed().stream().limit(N).toList();
    }

    // uses a string builder to create a report. returns strings and prints the report to console.
    public String getReport() {
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        double totalSpending = employeeMap
                .values()
                .stream()
                .mapToDouble(Employee::getSalary)
                .sum();

        // uses collectors to group employees by department, the resulting map returned has the department name as a key with the value being a list of employees in that department
        Map<String, List<Employee<T>>> groupedDepts = employeeMap
                .values()
                .stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));

        stringBuilder.append("==== REPORT SUMMARY ====\n");
        stringBuilder.append("\nEmployee head count: ").append(employeeMap.size());
        stringBuilder.append("\nDepartment count: ").append(groupedDepts.size());
        stringBuilder.append("\nTotal spending: ").append(formatter.format(totalSpending)).append(" RWF\n");
        stringBuilder.append("\n==== DEPARTMENT SPENDING REPORT ====");
        for (Map.Entry<String, List<Employee<T>>> entry : groupedDepts.entrySet()) {
            String dept = entry.getKey();
            List<Employee<T>> employees = entry.getValue();
            int headCount = employees.size();
            double totalSalary = employees
                    .stream()
                    .mapToDouble(Employee::getSalary)
                    .sum();

            stringBuilder.append("\n\nDEPARTMENT: ").append(dept);
            stringBuilder.append("\n----------------------");
            stringBuilder.append("\nHead count: ").append(headCount);
            stringBuilder.append("\nTotal salaries: ").append(formatter.format(totalSalary)).append(" RWF");
        }

        stringBuilder.append("\n\n==== EXTENSIVE SALARY REPORT ====\n");
        employeeMap.forEach((key, value) ->
                stringBuilder.append("\n> ")
                        .append(value.getName())
                        .append(" -> ")
                        .append(formatter.format(value.getSalary()))
                        .append(" RWF"));

        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }

    // method to refresh the UI table view
    public void reset() {
        employeesList.setAll(employeeMap.values());
    }
}
