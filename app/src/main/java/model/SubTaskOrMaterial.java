package model;

/**
 * Created by ben on 05.05.16.
 */
public class SubTaskOrMaterial {

    private String name;
    private boolean subTask;

    public SubTaskOrMaterial(){
    }

    public SubTaskOrMaterial(String name, boolean subTask) {
        this.name = name;
        this.subTask = subTask;
    }

    public String getName() {
        return name;
    }

    public boolean isSubTask() {
        return subTask;
    }
}
