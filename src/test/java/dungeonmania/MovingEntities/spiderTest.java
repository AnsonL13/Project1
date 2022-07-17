package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.EnemyFactory;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Exit;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class spiderTest {

    @Test
    @DisplayName("Test spider can spawn")
    public void testSpiderSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        assertEquals(1, getEntities(initDungonRes, "spider").size());
    } 

    @Test
    @DisplayName("Test spider move")
    public void testSpiderMove() {
        Position initial = new Position(0, 0);
        Spider spider = new Spider("0", initial, 5, 5);
        spider.move(new Position(0,0), new ArrayList<Entity>());

        assertEquals(spider.getPosition(), new Position(0, -1));
        assertEquals(spider.getId(), "0");
    } 

    @Test
    @DisplayName("Test spider stuck by boulder")
    public void testSpiderBoulder() {
        List<Entity> stuck = new ArrayList<Entity>();
        stuck.add(new Boulder("0", "boulder", new Position(6, 5), false));
        
       
        Position initial = new Position(5, 5);
        Spider spider = new Spider("0", initial, 5, 5);
        spider.move(new Position(0,0), stuck);

        assertEquals(new Position(5, 4), spider.getPosition());
        assertEquals(spider.getId(), "0");
    } 

    @Test
    @DisplayName("Test spider can traverse through walls, doors, switches, portals, exits")
    public void testSpiderTraverse() {
        List<Entity> stuck = new ArrayList<Entity>();
        stuck.add(new Wall("0", "Wall", new Position(5, 6), false));
        stuck.add(new Door("0", "door", new Position(5, 4), false, 0));
        stuck.add(new FloorSwitch("0", "floor_switch", new Position(6, 6), false));
        stuck.add(new Portal("0", "portal", new Position(6, 5), false, "blue"));
        stuck.add(new Exit("0", "exit", new Position(6, 4), false));

        Position initial = new Position(5, 5);
        Spider spider = new Spider("0", initial, 5, 5);
        spider.move(new Position(0,0), stuck);

        assertEquals(new Position(5, 4), spider.getPosition());
        assertEquals(spider.getId(), "0");
    }    
    
}