package cs455.overlay.dijkstra;
import java.util.*;
public class Vertex implements Comparable<Vertex> {

    public String name;
    public int minDist = Integer.MAX_VALUE;
    public Vertex prev;
    public ArrayList<Edge> adjacentEdges;

    public Vertex(String name) {
        this.name = name;
        adjacentEdges = new ArrayList<>();
    }
    public void addEdge(Edge e) {
        adjacentEdges.add(e);
    }

    // for use in the PriorityQueue
    public int compareTo(Vertex o) {
        return Integer.compare(minDist, o.minDist);
    }

    // use name to distinguish between vertices
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!this.name.equals(((Vertex)o).name)) return false;
        return true;
    }
}
