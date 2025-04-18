package com.kwizera.javaamalitechlabemployeemgtsystem.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {
    private Map<T, Employee<T>> employeeMap;
    private ObservableList<Employee<T>> employeesList = FXCollections.observableArrayList();

    public EmployeeDatabase() {
        this.employeeMap = new HashMap<>();
    }

    public boolean addEmployee(Employee<T> employee) {
        if (!employeeMap.containsKey(employee.getEmployeeId())) {
            employeeMap.put(employee.getEmployeeId(), employee);
            employeesList.add(employee);
            return true;
        } else {
            return false;
        }
    }

    public void removeEmployee(T employeeId) {
        if (employeeMap.remove(employeeId) == null) {
            System.out.println("Employee with " + employeeId + " was not found");
        }
    }

    public void updateEmployeeDetails(T employeeId, String field, Object newValue) {
        Employee<T> employee = employeeMap.get(employeeId);

        if (employee == null) {
            System.out.println("Employee with " + employeeId + " was not found");
            return;
        }

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
        }
    }

    public ObservableList<Employee<T>> getAllEmployees() {
        return employeesList;
    }

    public List<Employee<T>> getEmployeesByDepartment(String department) {
        return employeeMap.values()
                .stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> getEmployeeBySearchTerm(String searchTerm) {
        return employeeMap.values()
                .stream()
                .filter(e -> e.getName().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> getEmployeeBySalaryRange(double minSalary, double maxSalary) {
        return employeeMap
                .values()
                .stream()
                .filter(e -> e.getSalary() >= minSalary && e.getSalary() <= maxSalary)
                .collect(Collectors.toList());
    }

    // note to self: iterator to be used from the method invoking this one
    public List<Employee<T>> getEmployeeByPerformanceRating(double minRating) {
        return employeeMap
                .values()
                .stream()
                .filter(e -> e.getPerformanceRating() >= minRating)
                .collect(Collectors.toList());
    }

    // supposed to be EmployeeSalaryComparator
    public List<Employee<T>> sortBySalary() {
        return employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .collect(Collectors.toList());
    }

    // supposed to be EmployeePerformanceComparator [currently in ascending order, needs to be reversed]
    public List<Employee<T>> sortByPerformance() {
        return employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Employee::getPerformanceRating))
                .collect(Collectors.toList());
    }

    public void giveRaiseToTopPerformers(double thresholdScore, double raiseRate) {
        employeeMap
                .values()
                .stream()
                .filter(e -> e.getPerformanceRating() >= thresholdScore)
                .forEach(e -> {
                    double newSalary = e.getSalary() * (1 + raiseRate / 100);
                    e.setSalary(newSalary);
                });
    }

    public double calculateAverageSalaryByDepartment(String department) {
        return employeeMap
                .values()
                .stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    // [currently in ascending order, needs to be reversed]
    public List<Employee<T>> getTopEarners(Integer N) {
        return employeeMap
                .values()
                .stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .limit(N)
                .collect(Collectors.toList());
    }
}
