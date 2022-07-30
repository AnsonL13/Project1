package dungeonmania;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Direction;

public class Animation implements Serializable{
    private Dungeon dungeon;
    private HashMap<String, Integer> configMap;
    public Animation(Dungeon dungeon, HashMap<String, Integer> configMap) {
        this.dungeon = dungeon;
        this.configMap = configMap;
    }
    public AnimationQueue moveAnimation(Direction movementDirection){
        if(movementDirection == Direction.RIGHT) {
            return new AnimationQueue("tick_right", dungeon.getPlayer().getId(), Arrays.asList(
                "translate-x -1, over 0s", "translate-x 1, over 1s"
            ), false, -1);
        } else if(movementDirection == Direction.LEFT) {
            return new AnimationQueue("tick_left", dungeon.getPlayer().getId(), Arrays.asList(
                "translate-x 1, over 0s", "translate-x -1, over 1s"
            ), false, -1);
        } else if(movementDirection == Direction.UP) {
            return new AnimationQueue("tick_up", dungeon.getPlayer().getId(), Arrays.asList(
                "translate-y 1, over 0s", "translate-y -1, over 1s"
            ), false, -1);
        } else if(movementDirection == Direction.DOWN) {
            return new AnimationQueue("tick_down", dungeon.getPlayer().getId(), Arrays.asList(
                "translate-y -1, over 0s", "translate-y 1, over 1s"
            ), false, -1);
        }
        else return null;
    }


    public String getHealthString(){
        double cur_health = dungeon.getPlayer().getPlayerHealth();
        double return_value = cur_health / configMap.get("player_health");
        return Double.toString(return_value);
    }
}
