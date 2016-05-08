package model;

/**
 * Created by ben on 05.05.16.
 */
public class SubTaskOrMaterial {

    private String name;
    private int type;
    private boolean done;

    public SubTaskOrMaterial(){
    }

    public SubTaskOrMaterial(String name, int type, boolean done) {
        this.name = name;
        this.type = type;
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public boolean isDone() {
        return done;
    }
}
