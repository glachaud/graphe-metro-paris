import java.util.*;

/**
 * Created by guillaumelachaud on 3/29/17.
 */
public class BreadthFirstPaths<T> {
  private Map<Node<T>, Boolean> marked;
  private Map<Node<T>, Node<T>> edgeTo;
  private Map<Node<T>, Double> distTo;
  private final Node s;
  Map<Node<T>, Map<Node<T>, Edge<T>>> previousNodes;
  Map<Edge<T>, Double> edgeEccentricity;

  public BreadthFirstPaths(GraphList G, Node s) {
    marked = new HashMap<>();
    edgeTo = new HashMap<>();
    distTo = new HashMap<>();
    previousNodes = new HashMap<>();

    edgeEccentricity = new HashMap<>();
    this.s = s;
    bfs(G, s);
  }

  private void bfs(GraphList G, Node s) {
    Queue<Node> queue = new LinkedList<>();
    Map<Node<T>, Edge<T>> lastNode = new HashMap<>();
    marked.put(s, true);
    previousNodes.put(s, lastNode);
    Double distance = 0d;
    distTo.put(s, distance);
    queue.add(s);


    while (!queue.isEmpty()) {
      Node<T> v = queue.remove();
      Iterator<Edge<T>> neighbors = v.getNeighbors().iterator();
      while (neighbors.hasNext()) {
        lastNode = new HashMap<>();
        Edge<T> neighboringEdge = neighbors.next();
        Node<T> neighbor = neighboringEdge.getHead();
        if (!marked.containsKey(neighbor)) {
          if (!distTo.containsKey(neighbor)) {
            distTo.put(neighbor, distTo.get(v) + 1);
          }
          edgeTo.put(neighbor, v);
          marked.put(neighbor, true);
          queue.add(neighbor);
        }
        Iterator<Edge<T>> edges = neighbor.getNeighbors().iterator();
        while (edges.hasNext()) {
          Edge<T> edge = edges.next();
          Node<T> node = edge.getHead();
          Edge<T> reversedEdge = edge;
          Iterator<Edge<T>> edgeReverserIterator = node.getNeighbors().iterator();
          while (edgeReverserIterator.hasNext()) {
            reversedEdge = edgeReverserIterator.next();
            if (reversedEdge.getTail() == edge.getHead() && reversedEdge.getHead() == edge.getTail()) {
              break;
            }
          }
        }
        if (!lastNode.containsKey(v) && distTo.get(v) < distTo.get(neighbor)) {
          lastNode.put(v, neighboringEdge);
        }
        if (previousNodes.containsKey(neighbor)) {
          Map<Node<T>, Edge<T>> mergeMap = new HashMap<>();
          mergeMap.putAll(previousNodes.get(neighbor));
          mergeMap.putAll(lastNode);
          previousNodes.put(neighbor, mergeMap);
        } else {
          previousNodes.put(neighbor, lastNode);
        }
      }
    }
    computeEdgeEccentricity();
  }


  private void updateEdgeEccentricity(Node<T> station, Double number) {

    Double distance = distTo.get(station);
    if (distance == 0) {
    } else if (distance == 1) {
      Set<Node<T>> nodes = previousNodes.get(station).keySet();
      Double size = (double) nodes.size() * number;
      Iterator<Node<T>> nodeIterator = nodes.iterator();
      while (nodeIterator.hasNext()) {
        Node<T> node = nodeIterator.next();
        Edge<T> edge = previousNodes.get(station).get(node);
        if (edgeEccentricity.containsKey(edge)) {
          edgeEccentricity.put(edge, edgeEccentricity.get(edge) + 1 / size);
        } else {
          edgeEccentricity.put(edge, 1 / size);
        }
      }
    } else if (distance > 1) {
      Set<Node<T>> nodes = previousNodes.get(station).keySet();
      Double size = (double) nodes.size() * number;
      Iterator<Node<T>> nodeIterator = nodes.iterator();
      while (nodeIterator.hasNext()) {
        Node<T> node = nodeIterator.next();
        Edge<T> edge = previousNodes.get(station).get(node);
        if (distTo.get(node) < distTo.get(station)) {
          if (edgeEccentricity.containsKey(edge)) {
            edgeEccentricity.put(edge, edgeEccentricity.get(edge) + (1 / size));
          } else {
            edgeEccentricity.put(edge, 1 / size);
          }
          updateEdgeEccentricity(node, size);
        }
      }
    }

  }

  private void computeEdgeEccentricity() {
    Set<Node<T>> nodeSet = previousNodes.keySet();
    Map<Node<T>, Boolean> hasBeenVisited = new HashMap<>();
    Iterator<Node<T>> nodeIterator = nodeSet.iterator();
    int i = 0;
    while (nodeIterator.hasNext()) {
      Node<T> node = nodeIterator.next();
      if (!hasBeenVisited.containsKey(node)) {
        updateEdgeEccentricity(node, 1d);
        hasBeenVisited.put(node, true);
      }
    }
  }

  public boolean hasPathTo(Node v) {
    return distTo.get(v) < Double.POSITIVE_INFINITY;
  }

  public Stack<Node> pathTo(Node v) {
    if (!hasPathTo(v)) {
      return null;
    }
    Stack<Node> path = new Stack<Node>();
    Node currentNode = v;
    while (currentNode != s) {
      path.push(currentNode);
      currentNode = edgeTo.get(currentNode);
    }
    path.push(s);
    return path;
  }

  public Map<Node<T>, Double> getDistTo() {
    return distTo;
  }


  public Map<Node<T>, Map<Node<T>, Edge<T>>> getPreviousNodes() {
    return previousNodes;
  }

  public Map<Node<T>, Node<T>> getEdgeTo() {
    return edgeTo;
  }


  public Map<Edge<T>, Double> getEdgeEccentricity() {
    return edgeEccentricity;
  }

  public static void main(String[] args) throws java.io.IOException {
  }
}
