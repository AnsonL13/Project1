package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
public class ExceptionTest {
    @Test
    @DisplayName("Test tick with a sword excpetion")
    public void testZombieBattleWithSword() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_exceptionsTest", "c_spiderTest_basicMovement");
        assertEquals(1, countEntityOfType(initialResponse, "sword"));
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        EntityResponse actualKey = getEntities(initialResponse, "sword").get(0);
        controller.tick(Direction.LEFT);
        assertThrows(IllegalArgumentException.class, () -> controller.tick(actualKey.getId()));

    }

    @Test
    @DisplayName("Test interact id not exist")
    public void testInteractNonExistent() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse initialResponse = controller.newGame("d_exceptionsTest", "c_spiderTest_basicMovement");
        assertEquals(1, countEntityOfType(initialResponse, "sword"));
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        EntityResponse actualKey = getEntities(initialResponse, "sword").get(0);
        controller.tick(Direction.LEFT);
        assertThrows(IllegalArgumentException.class, () -> controller.interact(actualKey.getId()));

    }
}
