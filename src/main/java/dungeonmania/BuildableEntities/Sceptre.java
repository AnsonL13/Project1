package dungeonmania.BuildableEntities;

import dungeonmania.Item;

public class Sceptre implements Item{
    private String id;
    private String type;
    private boolean isInteractable;
    private int controlTime;

    public Sceptre(String id, String type, boolean isInteractable, int controlTime) {
        this.id = id;
        this.type = type;
        this.isInteractable = isInteractable;
        this.controlTime = controlTime;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getControlTime() {
        return controlTime;
    }
    
}
