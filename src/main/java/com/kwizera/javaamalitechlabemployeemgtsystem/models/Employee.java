package com.kwizera.javaamalitechlabemployeemgtsystem.models;

public class Employee<T> implements Comparable<Employee<T>> {
    private T employeeId;
    private String name;
    private String department;
    private double salary;
    private double performanceRating;
    private int yearsOfExperience;
    private boolean isActive;

    Employee(T employeeId, String name, String department, double salary, double performanceRating, int yearsOfExperience, boolean isActive) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.performanceRating = performanceRating;
        this.yearsOfExperience = yearsOfExperience;
        this.isActive = isActive;
    }

    @Override
    public int compareTo(Employee<T> other) {
        return other.yearsOfExperience - this.yearsOfExperience;
    }


    // getters
    public T getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public boolean isActive() {
        return isActive;
    }

    // setters
    public void setEmployeeId(T employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
