package com.grupo07.preventedstores.model.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Singleton class to manipulate the stores database connection
 */
public class StoreDatabaseConnection {

    private static StoreDatabaseConnection instance = null;
    private FirebaseDatabase connection = null;

    /**
     * Private singleton constructor
     */
    private StoreDatabaseConnection() {
        this.connection = FirebaseDatabase.getInstance();
    }

    /**
     * Public singleton constructor, creates an returns a class instance
     * if there isn't already one
     * @return instance of StoreDatabaseConnection
     */
    public static StoreDatabaseConnection getInstance() {
        if (instance == null)
            instance = new StoreDatabaseConnection();
        return instance;
    }

    /**
     * Returns a database reference to an specific path for object crud manipulation
     * @param path database path to the object or section requested
     * @return database reference of the given path
     */
    public DatabaseReference getReference(String path) {
        return connection.getReference(path);
    }
}
