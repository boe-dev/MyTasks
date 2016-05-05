package de.boe_dev.mytasks.ui.model;

/**
 * Created by ben on 05.05.16.
 */
public class Material {

    private String taskId, name;
    // private boolean checked;

    public Material(){

    }

    public Material(String taskId, String name) {
        this.taskId = taskId;
        this.name = name;
        // this.checked = checked;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    //public boolean isChecked() {
        //return checked;
    //}
}
