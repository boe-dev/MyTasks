package model;

import java.util.HashMap;

/**
 * Created by benny on 15.05.16.
 */
public class User {

    private String name, eMail;
    private HashMap<String, Object> timestampJoined;

    public User() {
    }

    public User(String name, String eMail, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.eMail = eMail;
        this.timestampJoined = timestampJoined;
    }

    public String getName() {
        return name;
    }

    public String geteMail() {
        return eMail;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }
}
