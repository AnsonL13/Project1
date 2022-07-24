package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.util.Position;


public class DijkstrasAlgo {
    private GraphNode adj_list[][] = new GraphNode[10][10];
    private Map<GraphNode, GraphNode> previous = new HashMap<>();
    private Map<Portal, Portal> portals = new HashMap<>();

    public DijkstrasAlgo () {}
    public void generateMap (List<Entity> entities) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                adj_list[i][j] = new GraphNode(1, new Position(i, j));
            }
        }
        for (Entity entity: entities) {
            if (entity instanceof Wall || entity instanceof Boulder || entity instanceof Door) {
                Position block = entity.getPosition();
                int x = block.getX();
                int y = block.getY();
                adj_list[x][y].setBlocked();
            } else if (entity instanceof SwampTile) {
                Position block = entity.getPosition();
                int x = block.getX();
                int y = block.getY();
                SwampTile swampTile = (SwampTile) entity;
                adj_list[x][y].setCost(swampTile.getMovementFactor());
            } else if (entity instanceof Portal) {
                // TODO
                Position block = entity.getPosition();
                int x = block.getX();
                int y = block.getY();
                adj_list[x][y].setCost(3);
            }
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

    public void printPath(Position entity, Position player) {
        GraphNode node = adj_list[player.getX()][player.getY()];
        Position pos = node.getPos();

        System.out.println("End");
        while (!pos.equals(entity)) {
            System.out.printf("[%d,%d]\n", pos.getX(), pos.getY());
            node = previous.get(node);
            pos = node.getPos();
        }
        System.out.println("Start");

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


    public Map<GraphNode, Double> DijstrasPositionAlgo (Position entity) {
        Map<GraphNode, Double> dist = new HashMap<>();
        Set<GraphNode> queue = new TreeSet<GraphNode>();

        int startX = entity.getX();
        int startY = entity.getY();

        double inf = Double.POSITIVE_INFINITY;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == startX && j == startY) { 
                    dist.put(adj_list[i][j], 0.0);
                    queue.add(adj_list[i][j]);
                } else {
                    dist.put(adj_list[i][j], inf);
                    queue.add(adj_list[i][j]);
                }
            }
        }

        while(!queue.isEmpty()) {
            GraphNode min = null;
            double minDis = Double.POSITIVE_INFINITY;

            for (GraphNode node: queue) {
                if (minDis > dist.get(node)) {
                    minDis = dist.get(node);
                    min = node;
                }
            }

            queue.remove(min);

            Position getAdj = min.getPos();
            for (Position adj : getAdj.getAdjacentPositions()) {
                int x = adj.getX();
                int y = adj.getY();
                GraphNode node = adj_list[x][y];
                if ((node.getCost() + dist.get(min) < dist.get(node)) && !node.isBlocked()) { // and valid pos
                    //queue.add(node);
                    dist.replace(node, node.getCost() + dist.get(min));
                }
            }
        }
        return dist;
    }

    public void DijstrasPosition (Position entity) {
        Map<GraphNode, Double> dist = new HashMap<>();
        Queue<GraphNode> queue = new PriorityQueue<GraphNode>();

        int startX = entity.getX();
        int startY = entity.getY();

        double inf = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
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
                    System.out.printf("[%d,%d]\n", pos.getPos().getX(), pos.getPos().getY());
                    System.out.println(dist.get(pos));

                }
                Double newCost = connect.getCost() + dist.get(pos);

                if ( newCost < dist.get(connect)) { // and valid pos
                    queue.add(connect);
                    if (connect.getPos().getX() == 4 && connect.getPos().getY() == 5) {
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
        System.out.println(count);
    }

    public Position nextPos() {
        return null;
    }

    public List<GraphNode> getCard(GraphNode node) {
        List<GraphNode> cardList = new ArrayList<GraphNode>();
        int x = node.getPos().getX();
        int y = node.getPos().getY();
        if (x + 1 < 10) cardList.add(adj_list[x + 1][y]);
        if (x - 1 >= 0) cardList.add(adj_list[x - 1][y]);
        if (y + 1 < 10) cardList.add(adj_list[x][y + 1]);
        if (y - 1 >= 0) cardList.add(adj_list[x][y - 1]);
    
        return cardList.stream().filter(e -> ! e.isBlocked()).collect(Collectors.toList());
    }

    public static void main(String argv[]) {
        DijkstrasAlgo check = new DijkstrasAlgo();
        List<Entity> entity = new ArrayList<Entity>();
        entity.add(new Wall("1", "wall", new Position(4, 4), false));
        entity.add(new Wall("1", "wall", new Position(4, 3), false));
        entity.add(new Wall("1", "wall", new Position(3, 5), false));



        check.generateMap(entity);
        check.printMap();

        check.DijstrasPosition(new Position(3, 4));
        check.printPath(new Position(3, 4), new Position(5,4));
    }
    
}

