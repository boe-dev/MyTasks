package model;

/**
 * Created by ben on 05.05.16.
 */
public class SubTaskOrMaterial {

    private String name;
    private boolean isSubTask;

    public SubTaskOrMaterial(){

    }

    public SubTaskOrMaterial(String name, boolean isSubTask) {
        this.name = name;
        this.isSubTask = isSubTask;
    }

    public String getName() {
        return name;
    }

    public boolean isSubTask() {
        return isSubTask;
    }
}
