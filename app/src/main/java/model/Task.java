package model;

import java.util.HashMap;

/**
 * Created by ben on 05.05.16.
 */
public class Task {

    private String listName, createdUser;
    private HashMap<String, Object> timestampLastChanged, timestampCreated;
    double latitude, longitude;

    public Task(){
    }

    public Task(String listName, String createdUser, double latitude, double longitude, HashMap<String, Object> timestampLastChanged) {
        this.listName = listName;
        this.createdUser = createdUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampCreated = timestampLastChanged;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }
}
