package cs455.overlay.dijkstra;
import java.util.*;

public class Edge {
    public Vertex v0, v1;
    public int weight;

    public Edge(Vertex v0, Vertex v1, int weight) {
        this.v0 = v0;
        this.v1 = v1;
        this.weight = weight;
    }
}
