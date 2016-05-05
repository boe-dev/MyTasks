package de.boe_dev.mytasks.ui.model;

/**
 * Created by ben on 05.05.16.
 */
public class SubTask {

    private String taskId, name;

    public SubTask(){

    }

    public SubTask(String taskId, String name) {
        this.taskId = taskId;
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }
}
