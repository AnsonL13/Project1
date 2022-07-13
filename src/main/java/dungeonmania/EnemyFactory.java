package dungeonmania;

public class EnemyFactory {
    private int spiderSpawnRate;
    private int zombieSpawnRate;
    private int spiderTickCounter;
    private int zombieTickCounter;

    public EnemyFactory(int spiderSpawnRate, int zombieSpawnRate) {
        this.spiderSpawnRate = spiderSpawnRate;
        this.zombieSpawnRate = zombieSpawnRate;
        this.spiderTickCounter = 0;
        this.zombieTickCounter = 0;
    }
}
