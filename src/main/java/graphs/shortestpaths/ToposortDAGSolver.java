package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();

        Set<V> visited = new HashSet<>();
        List<V> postOrder = new ArrayList<>();

        dfsPostOrder(graph, start, visited, postOrder);

        Collections.reverse(postOrder);

        distTo.put(start, 0.0);

        for (V node : postOrder) {
            for (Edge<V> edge : graph.neighbors(node)) {
                V neighbor = edge.to;
                double newDistance = distTo.get(node) + edge.weight;

                if (newDistance < distTo.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    distTo.put(neighbor, newDistance);
                    edgeTo.put(neighbor, edge);
                }
            }
        }
    }

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.
     */
    private void dfsPostOrder(Graph<V> graph, V node, Set<V> visited, List<V> result) {
        visited.add(node);
        for (Edge<V> edge : graph.neighbors(node)) {
            if (!visited.contains(edge.to)) {
                dfsPostOrder(graph, edge.to, visited, result);
            }
        }
        result.add(node);  // Add the node to the result after processing its neighbors
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
