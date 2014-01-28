import java.util.*;
public class Edge {
    private Vertex v_x, v_y; // connecting vertices
    public int weight;

    public Edge(Vertex v0, Vertex v1, int weight) {
        this.v_x = v0;
        this.v_y = v1;
        this.weight = weight;
    }

}
