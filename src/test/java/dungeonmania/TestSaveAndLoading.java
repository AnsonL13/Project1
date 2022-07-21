package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.TestUtils.getPlayer;

public class TestSaveAndLoading {

    @Test
    @DisplayName("When player pushes the boulder to right,save game.")
    public void testLoad() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderGoal_multiple", "c_bombTest_placeBombRadius2");
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 1), false);
        DungeonResponse saveGame = dmc.saveGame("d_boulderGoal_multiple");
        DungeonResponse loadGame = dmc.loadGame("d_boulderGoal_multiple");
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();
        assertEquals(expectedPlayer, actualPlayer);

    }
    
}
