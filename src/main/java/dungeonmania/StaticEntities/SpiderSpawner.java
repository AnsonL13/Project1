package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.InteractableEntity;
import dungeonmania.Player;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

import java.util.List;
import java.util.Random;

public class SpiderSpawner implements StaticEntity, InteractableEntity{
    
    private String id;
    private String type;
    private Position position;
    private boolean isInteractable;

    public SpiderSpawner(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    public Position spawn(List<Entity> entities) {
        Random random = new Random();
        int i = 15;
        
        int randomX = random.nextInt(i);
        int randomY = random.nextInt(i);
        return new Position(randomX, randomY);
         
        /* 
        switch(randomPos) {
            case 0:
                spawnPositions.add(spawner.translateBy(Direction.UP));
                break;
            case 1:
                spawnPositions.add(spawner.translateBy(Direction.DOWN));
                break;
            case 2:
                spawnPositions.add(spawner.translateBy(Direction.LEFT));
                break;
            case 3:
                spawnPositions.add(spawner.translateBy(Direction.RIGHT));
                break;
        */
    }

    public boolean canSpawn(Position position, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof MovingEntity && entity.getPosition().equals(position)) {
                return false;
            }
            // check boulder?
        }
        return true;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean interactActionCheck(Player player) {
        return false;
    }
}
