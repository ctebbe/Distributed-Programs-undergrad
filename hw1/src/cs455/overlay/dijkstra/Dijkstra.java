package cs455.overlay.dijkstra;
import java.util.*;
import java.util.regex.*;
public class Dijkstra {

    public static RoutingCache generateRoutingPlan(String sourceString, String[] linkWeights) {

        // generate a graph from the linkWeights
        HashMap<String, Vertex> vertexMap = parseLinkWeights(linkWeights);

        // run dijkstra's to find the shortest path to all other vertices from source vertex
        Vertex source = vertexMap.get(sourceString);
        dijkstraAlgorithm(source);

        return new RoutingCache(source, vertexMap);
    }

    private static HashMap<String, Vertex> parseLinkWeights(String[] linkWeights) {
        HashMap<String, Vertex> vertexMap = new HashMap<>();
        for(String link : linkWeights) {

            // [0] -> v0, [1] -> v1, [2] -> link weight
            String[] tokens = link.split(Pattern.quote(" "));

            // create new vertices from the link and put them in the map if needed
            if(!vertexMap.containsKey(tokens[0])) vertexMap.put(tokens[0], new Vertex(tokens[0]));
            if(!vertexMap.containsKey(tokens[1])) vertexMap.put(tokens[1], new Vertex(tokens[1]));

            // add the bi-directional edge of this link
            new Edge(vertexMap.get(tokens[0]), vertexMap.get(tokens[1]), Integer.parseInt(tokens[2]));
        }
        return vertexMap;
    }

    // generic dijkstra's to find shortest path from source to all other vertices
    private static void dijkstraAlgorithm(Vertex source) {
        source.minDist = 0;
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        queue.add(source);
        while(!queue.isEmpty()) {
            Vertex u = queue.poll();
            for(Edge e : u.adjacentEdges) {
                Vertex v = e.getOtherVertex(u);
                int distThruU = u.minDist + e.weight;
                if(distThruU < v.minDist) {
                    queue.remove(v); // mimic decrease in priority
                    v.minDist = distThruU;
                    v.prev = u;
                    queue.add(v);
                }
            }
        }
    }

    // *** TESTING GRAPHS ***
    public static final String[] bigTestLinks = new String[] {
        "129.82.46.205:53793 129.82.46.225:43346 1",
        "129.82.46.205:53793 129.82.46.216:56631 2",
        "129.82.46.205:53793 129.82.46.211:44932 5",
        "129.82.46.205:53793 129.82.46.197:34075 10",
        "129.82.46.225:43346 129.82.46.216:56631 8",
        "129.82.46.225:43346 129.82.46.190:33798 8",
        "129.82.46.225:43346 129.82.46.211:44932 7",
        "129.82.46.216:56631 129.82.46.190:33798 5",
        "129.82.46.216:56631 129.82.46.233:51382 4",
        "129.82.46.190:33798 129.82.46.233:51382 1",
        "129.82.46.190:33798 129.82.46.198:45597 10",
        "129.82.46.233:51382 129.82.46.198:45597 6",
        "129.82.46.233:51382 129.82.46.194:54648 3",
        "129.82.46.198:45597 129.82.46.194:54648 1",
        "129.82.46.198:45597 129.82.46.207:43883 7",
        "129.82.46.194:54648 129.82.46.207:43883 2",
        "129.82.46.194:54648 129.82.46.197:34075 3",
        "129.82.46.207:43883 129.82.46.197:34075 5",
        "129.82.46.207:43883 129.82.46.211:44932 6",
        "129.82.46.197:34075 129.82.46.211:44932 10"
    };
    public static final String[] smallTestLinks = new String[] {
        "129.82.46.205:53793 129.82.46.211:44932 1",
        "129.82.46.205:53793 129.82.46.216:56631 5",
        "129.82.46.216:56631 129.82.46.211:44932 10"
    };
    public static void main(String[] args) {
        RoutingCache rc = generateRoutingPlan("129.82.46.216:56631", bigTestLinks);
        for(String s : rc.getMessageRoutingPlanTo("129.82.46.211:44932")) {
            System.out.println(s);
        }
    }

}
