package com.kwizera.javaamalitechlabemployeemgtsystem.session;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;

// uses singleton pattern design. maintains one instance across the application at runtime
public class SessionManager<T> {
    private EmployeeDatabase<T> database;
    private static SessionManager<?> instance;

    // creates an instance(if there isn't one already) and/or returns it.
    public static <T> SessionManager<T> getInstance() {
        if (instance == null) {
            instance = new SessionManager<T>();
        }
        return (SessionManager<T>) instance;
    }

    // holds the employee database in the session while the program runs
    public void setDatabase(EmployeeDatabase<T> db) {
        database = db;
    }

    // returns employee database, allowing global access
    public EmployeeDatabase<T> getDatabase() {
        return database;
    }
}
