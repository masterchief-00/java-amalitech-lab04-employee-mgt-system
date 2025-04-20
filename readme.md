# LAB: Employee Management System
## Overview
A simple employee management system built using JavaFX and core Java using key OOP concepts and Java collection framework and generics. The project reinforces the use of Lists, Maps, Generics, Iterators, Comparators, Comparable, and Streams while efficiently handling employee records.

## Features
All features are implemented to be accessed via console(some of them) and GUI designed using JavaFX. They include:
- Listing employees
- Creating employee
- Removing employee
- Updating employee using
- Searching employee by name
- Filtering and sorting by department, salary, performance rating and experience.
- Generating reports (GUI and console).
- A "not bad" user interface😊

## Stack
- Java 21
- JavaFX (Scenebuilder,FXML)
- Maven (for dependency management)

## Business logic implementation summary
- A generic `Employee` class, where the `employeeId` attribute can be any type to enforce <b>Code reusability</b>.
- `EmployeeDatabase` generic class employs the use of Maps with `employeeMap` as a make-shift database for the program with methods like `addEmployee(Employee<T> employee)` and `removeEmployee(T employeeId)` performing create and delete operations, respectively.
- Methods such as `getEmployeeBySearchTerm(String searchTerm)` and `sortByPerformance()`  use <b>Stream API</b> to perform searching, filtering and sorting operations.
- The `sortByExperience()` method implements `Comparators` to sort employees accordingly to their years of experience. 

## The user interface implementation summary
- Simple designs
- The user interface has 2 pages, the landing page and the homepage.
- and 2 pop up windows(not dialog boxes but the `Modality.WINDOW_MODAL` type). One performs create employee activities while the other performs reporting activities.
- Uses error labels, feedback/warning/error alerts, confirmation dialog boxes and <b>Regex expressions</b> to enforce input validation along a decent user experience.

## How to run
- Clone the project
- Open in intelliJ IDEA or another JavaFX-Compatible IDE
- Maven adds dependencies automatically
- Run the `Main` class
- 👍