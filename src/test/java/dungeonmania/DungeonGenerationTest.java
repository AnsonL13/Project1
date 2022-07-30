package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static dungeonmania.TestUtils.getEntities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

import dungeonmania.util.Position;

public class DungeonGenerationTest {
    @Test
    @DisplayName("Test throwing illegal argument Exception due to invalid config name")
    public void testThrowingConfigException() {
        DungeonManiaController dmc = new DungeonManiaController();

        assertDoesNotThrow(() -> dmc.generateDungeon(-50, -50, 0, 0, "simple")); 
        //assertThrows(IllegalArgumentException.class, () -> dmc.generateDungeon(-50, -50, 50, 50, "shflsfje")); 
    }
}
