package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.util.Position;


public class DijkstrasAlgo {
    private GraphNode adj_list[][] = new GraphNode[40][40];
    private Map<GraphNode, GraphNode> previous = new HashMap<>();
    private Map<String, List<Portal>> portals = new HashMap<String, List<Portal>>();
    private Position entity;

    public DijkstrasAlgo (Position entity) {
        this.entity = entity.translateBy(20, 20);
    }

    public void generateMap (List<Entity> entities) {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                adj_list[i][j] = new GraphNode(1, new Position(i, j));
            }
        }
        for (Entity entity: entities) {
            if (entity instanceof Wall || entity instanceof Boulder || entity instanceof Door) {
                Position block = entity.getPosition().translateBy(20,20);
                int x = block.getX();
                int y = block.getY();
                adj_list[x][y].setBlocked();
            } else if (entity instanceof SwampTile) {
                Position block = entity.getPosition().translateBy(20,20);
                int x = block.getX();
                int y = block.getY();
                SwampTile swampTile = (SwampTile) entity;
                adj_list[x][y].setCost(swampTile.getMovementFactor());
            } else if (entity instanceof Portal) {
                // TODO
                Portal currPortal = (Portal) entity;
                String colour = currPortal.getColour();
                if (portals.containsKey(colour)) {
                    List<Portal> match = portals.get(colour);
                    match.add(currPortal);
                } else {
                    List<Portal> match = new ArrayList<Portal>();
                    match.add(currPortal);
                    portals.put(colour, match);
                }

            }
        }
        for (Map.Entry<String, List<Portal>> match : portals.entrySet()) {

        }
    }

    public void printMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Position block = adj_list[i][j].getPos();
                int x = block.getX();
                int y = block.getY();
                if (!adj_list[i][j].isBlocked()) {
                    System.out.printf("[%d,%d]", x,y);
                } else {
                    System.out.printf("[X,X]", x,y);

                }
                
            }
            System.out.println("\n");
        }
    }

    public void printPath(Position player) {
        player = player.translateBy(20,20);

        GraphNode node = adj_list[player.getX()][player.getY()];
        Position pos = node.getPos();

        System.out.println("End");
        if (previous.get(node) == null) return;
        while (!pos.equals(entity)) {
            System.out.printf("[%d,%d]\n", pos.translateBy(-20,-20).getX(), pos.translateBy(-20,-20).getY());
            node = previous.get(node);
            pos = node.getPos();
        }
        System.out.println("Start");

    }

    public Position getNextPos(Position player) {
        player = player.translateBy(20,20);
        GraphNode node = adj_list[player.getX()][player.getY()];
        Position pos = node.getPos();
        Position prev = null;

        // entity is blocked
        if (previous.get(node) == null) return entity.translateBy(-20,-20);

        while (!pos.equals(entity)) {
            prev = pos;
            node = previous.get(node);
            pos = node.getPos();
        }
        return prev.translateBy(-20,-20); // TODO
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

    public void DijstrasPosition () {
        Map<GraphNode, Double> dist = new HashMap<>();
        Queue<GraphNode> queue = new PriorityQueue<GraphNode>();

        int startX = entity.getX();
        int startY = entity.getY();

        double inf = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (i == startX && j == startY) { // check if need to check for blocke nodes
                    dist.put(adj_list[i][j], 0.0);
                    previous.put(adj_list[i][j], null);
                } else if (! adj_list[i][j].isBlocked()) {
                    dist.put(adj_list[i][j], inf);
                    previous.put(adj_list[i][j], null);
                }
            }
        }
       // dist.replace(new Node(1, new Position(1, 2)), 0.0); //replace with entity

        //queue.add(new Node(1, new Position(1, 2)));
        //System.out.printf("[%d,%d]\n", startX, startY);

        queue.add(adj_list[startX][startY]);
        GraphNode pos = null;

        int count = 0;
        while(!queue.isEmpty()) {
            pos = queue.poll();
            if (dist.get(pos) == Integer.MAX_VALUE) {
                System.out.println("broken");
				break;
			}
            count++;
            for (GraphNode connect : getCard(pos)) {
                if (count == 0) {
                    //System.out.printf("[%d,%d]\n", pos.getPos().getX(), pos.getPos().getY());
                    //System.out.println(dist.get(pos));

                }
                Double newCost = connect.getCost() + dist.get(pos);

                if ( newCost < dist.get(connect) && !connect.isBlocked()) { // and valid pos
                    queue.add(connect);
                    if (connect.getPos().getX() == 1 && connect.getPos().getY() == 5) {
                        //System.out.println(pos.getPos().getX());
                        //System.out.println(pos.getPos().getY());
                        //System.out.printf("New: %f Old: %f\n", newCost, dist.get(connect));
    
                    }
                    dist.replace(connect, newCost);
                    previous.replace(connect, pos);
                }

                //count+= connect.getCost();


            }
        }
        //System.out.println(count);
    }

    public Position nextPos() {
        return null;
    }

    public List<GraphNode> getCard(GraphNode node) {
        List<GraphNode> cardList = new ArrayList<GraphNode>();
        int x = node.getPos().getX();
        int y = node.getPos().getY();
        if (x + 1 < 40) cardList.add(adj_list[x + 1][y]);
        if (x - 1 >= 0) cardList.add(adj_list[x - 1][y]);
        if (y + 1 < 40) cardList.add(adj_list[x][y + 1]);
        if (y - 1 >= 0) cardList.add(adj_list[x][y - 1]);
    
        return cardList.stream().filter(e -> ! e.isBlocked()).collect(Collectors.toList());
    }

    public static void main(String argv[]) {
        DijkstrasAlgo check = new DijkstrasAlgo(new Position(2, -1));
        List<Entity> entity = new ArrayList<Entity>();
        entity.add(new Wall("1", "wall", new Position(1, 2), false));
        entity.add(new SwampTile("1", "swamp_time", new Position(1, -1), false, 5));
        entity.add(new SwampTile("1", "swamp_time", new Position(1, -2), false, 5));
        entity.add(new SwampTile("1", "swamp_time", new Position(2, -2), false, 4));
        entity.add(new SwampTile("1", "swamp_time", new Position(2, 0), false, 6));
        entity.add(new SwampTile("1", "swamp_time", new Position(2, 1), false, 5));

        check.generateMap(entity);
        check.printMap();

        check.DijstrasPosition();
        Position play = new Position(1,1);
        check.printPath(play); //need to translate this
        Position next = check.getNextPos(play);
        System.out.printf("[%d,%d]\n", next.getX(), next.getY());

    }
    
}

