package dungeonmania.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;

import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.EnemyFactory;
import dungeonmania.Entity;
import dungeonmania.MovingEntities.Mercenary;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class zombieTest {
    @Test
    @DisplayName("Test zombie is spawned")
    public void testZombieSpawned() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // check for zombie after tick
        int zombieCount = countEntityOfType(actualDungonRes, "zombie");
        assertEquals(1, zombieCount);

    }  
    
    @Test
    @DisplayName("Test zombiespawner is created")
    public void testZombieSpawer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        assertEquals(1, getEntities(initDungonRes, "zombie_toast_spawner").size());
    } 

    @Test
    @DisplayName("Test zombiespawner spawns no zombie")
    public void testZombieSpawerNone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_spiderTest_basicMovement");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2), false);

        // move player downward
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

        // check for zombie after tick
        int zombieCount = countEntityOfType(actualDungonRes, "zombie");
        assertEquals(0, zombieCount);

    } 

    @Test
    @DisplayName("Test zombiespawner after 10 ticks")
    public void testZombieSpawerAfter10() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");

        // create the expected result
        initDungonRes = dmc.tick(Direction.RIGHT);
        // check for zombie after tick
        int zombieCount = countEntityOfType(initDungonRes, "zombie");
        assertEquals(0, zombieCount);

        for (int i = 0; i < 9; ++i) {
            initDungonRes = dmc.tick(Direction.LEFT);
            zombieCount = countEntityOfType(initDungonRes, "zombie");
            assertEquals(0, zombieCount);
    
        }
        initDungonRes = dmc.tick(Direction.RIGHT);
        zombieCount = countEntityOfType(initDungonRes, "zombie");
        assertEquals(1, zombieCount);


    } 

    @Test
    @DisplayName("Test zombiespawner spawn multiple zombies")
    public void testZombieSpawerMultiple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_simpleSpawn", "c_zombieTest_simpleSpawn");
        
        // move player downward
        initDungonRes = dmc.tick(Direction.DOWN);
        int zombieCount = countEntityOfType(initDungonRes, "zombieToast");
        assertEquals(1, zombieCount);

        initDungonRes = dmc.tick(Direction.DOWN);
        zombieCount = countEntityOfType(initDungonRes, "zombieToast");
        assertEquals(2, zombieCount);

        initDungonRes = dmc.tick(Direction.DOWN);
        zombieCount = countEntityOfType(initDungonRes, "zombieToast");
        assertEquals(3, zombieCount);
    } 

    //
    // White box unit testing
    //

    @Test
    @DisplayName("Test zombie is spawned")
    public void testZombieSpawnedUnit() {
        List<ZombieToastSpawner> spawners = new ArrayList<ZombieToastSpawner>();
        spawners.add(new ZombieToastSpawner("1", "", new Position(0, 0), true));

        // Call factory function
        EnemyFactory factory = new EnemyFactory(5, 5, 5, 5);
        factory.setSpawnRate(1, 1);
        List<Entity> newZombie = factory.spawn("1", spawners, new ArrayList<Entity>());
        
        // Check correct return
        ZombieToast expected = new ZombieToast("1", 5, 5, new Position(-1, -1));
        assertEquals(expected.getId(), newZombie.get(0).getId());
        assertEquals(expected.getPosition(), newZombie.get(0).getPosition());

    }  

    @Test
    @DisplayName("Test zombie is spawned multiple")
    public void testZombieSpawnedMultipleUnit() {
        List<ZombieToastSpawner> spawners = new ArrayList<ZombieToastSpawner>();
        spawners.add(new ZombieToastSpawner("1", "", new Position(0, 0), true));
        spawners.add(new ZombieToastSpawner("1", "", new Position(5, 5), true));
        spawners.add(new ZombieToastSpawner("1", "", new Position(-5, -5), true));

        assertEquals(3, spawners.size());
        
        // Call factory function
        EnemyFactory factory = new EnemyFactory(5, 5, 5, 5);
        factory.setSpawnRate(5, 1);
        List<Entity> newZombies = factory.spawn("1", spawners, new ArrayList<Entity>());
        assertEquals(3, newZombies.size());
        
        // Check correct return
        ZombieToast expected1 = new ZombieToast("1", 5, 5, new Position(-1, -1));
        ZombieToast expected2 = new ZombieToast("2", 5, 5, new Position(4, 4));
        ZombieToast expected3 = new ZombieToast("3", 5, 5, new Position(-6, -6));

        assertEquals(expected1.getId(), newZombies.get(0).getId());
        assertEquals(expected1.getPosition(), newZombies.get(0).getPosition());
        assertEquals(expected2.getId(), newZombies.get(1).getId());
        assertEquals(expected2.getPosition(), newZombies.get(1).getPosition());
        assertEquals(expected3.getId(), newZombies.get(2).getId());
        assertEquals(expected3.getPosition(), newZombies.get(2).getPosition());

    }  

    @Test
    @DisplayName("Test zombie move")
    public void testZombieRandomMove() {
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 5, 5, intial);
        zombie.move(new Position(5,5), new ArrayList<Entity>());

        assertNotEquals(zombie.getPosition(), new Position(0, 0));
        assertEquals(zombie.getId(), "0");
    } 

    @Test
    @DisplayName("Test zombie stuck move")
    public void testZombieRandomMoveStuck() {
        List<Entity> stuck = new ArrayList<Entity>();
        stuck.add(new Door("0", "door", new Position(5, 4), false, 0));
        stuck.add(new Boulder("0", "boulder", new Position(6, 5), false));
        stuck.add(new Wall("0", "Wall", new Position(5, 6), false));
        stuck.add(new Mercenary("0", "mercenary", new Position(4, 5), false, 1, 1, 1, 1, 1, 1));


        Position intial = new Position(5, 5);
        ZombieToast zombie = new ZombieToast("0", 5, 5, intial);
        zombie.move(new Position(0,0), stuck);

        assertEquals(new Position(5, 5), zombie.getPosition());
        assertEquals(zombie.getId(), "0");
    } 

    @Test
    @DisplayName("Test zombie run away move")
    public void testZombieRunAway() {
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 5, 5, intial);
        zombie.setInvincible(5);
        zombie.move(new Position(5,5), new ArrayList<Entity>());

        assertEquals(new Position(-1, 0), zombie.getPosition());
        assertEquals(zombie.getId(), "0");
    } 

    // TODO
    @Test
    @DisplayName("Test zombie no battle invisible")
    public void testZombieInvisible() {
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 5, 5, intial);
        zombie.setInvisible(5);
        zombie.move(new Position(1,5), new ArrayList<Entity>());

        assertEquals(new Position(-1, 0), zombie.getPosition());
        assertEquals(zombie.getId(), "0");
    } 
}
