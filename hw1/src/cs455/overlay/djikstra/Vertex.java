import java.util.*;
public class Vertex implements Comparable<Vertex> {
    public String name;
    public Edge[] edges;
    public int minDistance = Integer.MAX_VALUE;
    public Vertex predecessor;
    public Vertex(String name) { this.name = name; }

    public int compareTo(Vertex other) {
        return this.name.equals(other.name);
    }
    public String toString { return this.name; }
}
