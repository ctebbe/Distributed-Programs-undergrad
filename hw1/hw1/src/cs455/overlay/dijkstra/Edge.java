package cs455.overlay.dijkstra;
import java.util.*;

public class Edge {
    public Vertex v0, v1;
    public int weight;

    public Edge(Vertex v0, Vertex v1, int weight) {
        this.v0 = v0;
        this.v1 = v1;
        this.weight = weight;

        // add this edge to both vertices
        this.v0.addEdge(this);
        this.v1.addEdge(this);
    }
    public Vertex getOtherVertex(Vertex notThisOne) {
        if(v0.equals(notThisOne)) return v1;
        return v0;
    }
    public String toString() {
        return v0.name + " --- " + weight + " --- " + v1.name;
    }
}
