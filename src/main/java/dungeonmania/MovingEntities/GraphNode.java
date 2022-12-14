package dungeonmania.MovingEntities;

import dungeonmania.util.Position;

import java.io.Serializable;

public class GraphNode implements Comparable<GraphNode>, Serializable {
    private int cost;
    private boolean block;
    private Position pos;

    public GraphNode(int cost, Position pos) {
        this.cost = cost;
        this.pos = pos;
        this.block = false;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setBlocked() {
        this.block = true;
    }

    public Position getPos() {
        return pos;
    }

    public int getCost() {
        if (isBlocked()) return 100;
        return cost;
    }

    public boolean isBlocked() {
        return block;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        GraphNode other = (GraphNode) obj;
        if (this.getPos() != other.getPos()) return false;

        return true;
    }

	public int compareTo(GraphNode o) {
		if (this.cost < o.cost) {
			return -1;
        } else if (this.cost > o.cost) {
            return 1;
        }
        return 0;
	}

}

