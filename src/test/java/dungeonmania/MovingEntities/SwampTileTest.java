package dungeonmania.MovingEntities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    @DisplayName("Test swamp tile slowing down a spider with random movement")
    public void testSwampOnSpiderMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_1SwampTileSpiderTest", "c_spiderTest_basicMovement");
        assertEquals(1, getEntities(initDungonRes, "spider").size());

        Position initialPosition = new Position(9, 10);

        assertEquals(initialPosition, getEntities(initDungonRes, "spider").get(0).getPosition());

        // Spider goes onto swamp tile
        DungeonResponse res = dmc.tick(Direction.RIGHT);

        Position position = new Position(9, 9);

        assertEquals(position, getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT);

        // check spider is stuck on tile
        assertEquals(position, getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(position, getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.RIGHT);

        position = new Position(10, 9);

        assertEquals(position, getEntities(res, "spider").get(0).getPosition());
    } 

    @Test
    @DisplayName("Test swamp tile slowing down a mercenary finding the player")
    public void testSwampOnMercenaryMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_1SwampTileOnMercenaryTest", "c_spiderTest_basicMovement");
        assertEquals(1, getEntities(initDungonRes, "mercenary").size());

        Position initialPosition = new Position(8, 1);

        assertEquals(initialPosition, getEntities(initDungonRes, "mercenary").get(0).getPosition());

        // Mercenary goes onto swamp tile
        DungeonResponse res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        Position position = new Position(6, 1);

        assertEquals(position, getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);

        // Check mercenary is stuck on swamp tile
        assertEquals(position, getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);

        position = new Position(5, 1);

        // Check mercenary moved, swamp tile timer finished
        assertEquals(position, getEntities(res, "mercenary").get(0).getPosition());
    } 

    @Test
    @DisplayName("Test zombie move")
    public void testZombieRandomMove() {
        Position intial = new Position(0, 0);
        ZombieToast zombie = new ZombieToast("0", 5, 5, intial);
        zombie.setPotionStatus(false, true);
        List<Entity> swamp = new ArrayList<Entity>();
        swamp.add(new SwampTile("1", "swamp_tile",  new Position(-1, 0), false, 2));

        // move onto swamp
        zombie.move(new Position(5,0), swamp);
        assertEquals(new Position(-1, 0), zombie.getPosition());

        zombie.move(new Position(5,0), swamp);
        assertEquals(new Position(-1, 0), zombie.getPosition());
        assertEquals(zombie.getId(), "0");
    } 
}
