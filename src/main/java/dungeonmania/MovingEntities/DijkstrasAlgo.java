package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Wall;

import java.util.List;

import org.w3c.dom.Node;

public class DijkstrasAlgo {

    public void generateMap(List<List<Node> > map, List<Entity> entities) {
        for (int i = 0; i < 40; i++) {

        }
        for (Entity entity: entities) {
            if (entity instanceof Wall) {}
        }

    /*function Dijkstras(grid, source):
    let dist be a Map<Position, Double>
    let prev be a Map<Position, Position>

    for each Position p in grid:
        dist[p] := infinity
        previous[p] := null
    dist[source] := 0

    let queue be a Queue<Position> of every position in grid
    while queue is not empty:
        u := next node in queue with the smallest dist
        for each cardinal neighbour v of u:
            if dist[u] + cost(u, v) < dist[v]:
                dist[v] := dist[u] + cost(u, v)
                previous[v] := u
    return previous
 */
    }
    
}

