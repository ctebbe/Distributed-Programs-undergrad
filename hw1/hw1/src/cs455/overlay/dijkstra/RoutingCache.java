package cs455.overlay.dijkstra;
import java.util.*;
public class RoutingCache {

    private HashMap<String, String[]> shortestPathMap   = null; // the graph used for this routing cache
    private HashMap<String, Vertex> vertexMap           = null; // the vertices used for this routing cache
    private Vertex source                               = null; // the source vertex used for this routing cache

    // calculatedGraph is the vertex graph with shortest path from source to all other vertices calculated
    public RoutingCache(Vertex source, HashMap<String, Vertex> vertexMap) {
        this.source = source;
        this.vertexMap = vertexMap;

        // calculate all the shortest paths
        this.shortestPathMap = new HashMap<>();
        for(Map.Entry<String, Vertex> entry : vertexMap.entrySet()) {
            shortestPathMap.put(entry.getKey(), getShortestPathTo(entry.getValue()).toArray(new String[0]));
        }
    }

    // return the in-order routing plan from source to destination vertices
    public String[] getMessageRoutingPlanTo(String destVertex) {
        return shortestPathMap.get(destVertex);
    }

    public String getSourceKey() { return this.source.name; }

    // follow prev pointers back to find shortest path from source to destination
    private ArrayList<String> getShortestPathTo(Vertex destination) {
        ArrayList<String> shortestPath = new ArrayList<>();

        for(Vertex v = destination; !v.equals(this.source); v = v.prev) {
            shortestPath.add(v.name);
        }

        Collections.reverse(shortestPath);
        return shortestPath;
    }

    public String toString() {
        for(Vertex v : vertexMap.values().toArray(new Vertex[0])) {
            if(v.equals(source)) continue;
            System.out.println("Shortest path to:" + v.name);
            for(String next : shortestPathMap.get(v.name)) {
                System.out.println("\t"+vertexMap.get(next).getPrevEdge());
            }
        }
        return null;
    }
}
