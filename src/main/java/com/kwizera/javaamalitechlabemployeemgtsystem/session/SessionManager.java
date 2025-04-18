package com.kwizera.javaamalitechlabemployeemgtsystem.session;

import com.kwizera.javaamalitechlabemployeemgtsystem.models.EmployeeDatabase;

public class SessionManager<T> {
    private EmployeeDatabase<T> database;
    private static SessionManager<?> instance;

    public static <T> SessionManager<T> getInstance() {
        if (instance == null) {
            instance = new SessionManager<T>();
        }
        return (SessionManager<T>) instance;
    }

    public void setDatabase(EmployeeDatabase<T> db) {
        database = db;
    }

    public EmployeeDatabase<T> getDatabase() {
        return database;
    }
}
