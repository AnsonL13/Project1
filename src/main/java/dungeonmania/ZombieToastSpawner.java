package dungeonmania;

import java.util.List;

import dungeonmania.enemy.Enemy;
import dungeonmania.enemy.ZombieToast;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends EnemyFactory {
    public ZombieToastSpawner(int attack, int health, int spawnRate, Position position) {
        super(attack, health, spawnRate, position);
    }

    @Override
    public Enemy spawn() {
        if (super.getNextSpawn() == 1 && super.getSpawnRate() != 0) {
            super.setNextSpawn();
            Position spawnerPos = super.getPosition();
            List<Position> adj = spawnerPos.getAdjacentPositions();

            for (Position pos : adj) {
                if (super.canSpawn(pos)) {
                    return new ZombieToast(super.getEnemyHealth(), super.getEnemyAttack(), pos);
                }
            }
        } 
        
        if (super.getSpawnRate() != 0) {
            super.minusNextSpawn();
        }
        return null;
    }
    
}
