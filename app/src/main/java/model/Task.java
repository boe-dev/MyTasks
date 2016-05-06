package model;

import java.util.HashMap;

/**
 * Created by ben on 05.05.16.
 */
public class Task {

    private String listName, createdUser;
    private HashMap<String, Object> timestampLastChanged, timestampCreated;

    public Task(){
    }

    public Task(String listName, String createdUser, HashMap<String, Object> timestampLastChanged) {
        this.listName = listName;
        this.createdUser = createdUser;
        this.timestampCreated = timestampLastChanged;
    }

    public String getListName() {
        return listName;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }
}
