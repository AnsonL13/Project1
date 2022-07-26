package dungeonmania;

import java.io.Serializable;

public interface Item extends Serializable{
    public String getType();
    public String getId();
}
