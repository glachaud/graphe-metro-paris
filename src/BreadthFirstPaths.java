import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by guillaumelachaud on 3/29/17.
 */
public class BreadthFirstPaths<T> {
  private Map<Node<T>, Boolean> marked;
  private Map<Node<T>, Node<T>> edgeTo;
  private Map<Node<T>, Double> distTo;
  private final Node s;
  Map<Node<T>, Map<Node<T>, Edge<T>>> lastNodes;
  Map<Edge<T>, Double> edgeEccentricity;
  Map<Edge<T>, Boolean> edgesVisited;

  public BreadthFirstPaths(GraphList G, Node s) {
    marked = new HashMap<>();
    edgeTo = new HashMap<>();
    distTo = new HashMap<>();
    lastNodes = new HashMap<>();
    edgeEccentricity = new HashMap<>();
    edgesVisited = new HashMap<>();
    this.s = s;
    bfs(G, s);
  }

  private void bfs(GraphList G, Node s) {
    Queue<Node> queue = new LinkedList<>();
    Map<Node<T>, Edge<T>> lastNode = new HashMap<>();
    marked.put(s, true);
    lastNodes.put(s, lastNode);
    Double distance = 0d;
    distTo.put(s, distance);
    queue.add(s);

    Iterator<Edge<T>> edgeIterator9 = G.getAllEdges().iterator();
    while(edgeIterator9.hasNext()){
      Edge<T> edge9 = edgeIterator9.next();
      edgesVisited.put(edge9, false);
    }


    while (!queue.isEmpty()) {
      Node<T> v = queue.remove();
      Iterator<Edge<T>> iterator = v.getNeighbors().iterator();
      while (iterator.hasNext()) {
        lastNode = new HashMap<>();
        Edge<T> edgeIteration = iterator.next();
        Node<T> nodeIteration = edgeIteration.getHead();
        if (!marked.containsKey(nodeIteration)) {
          if (!distTo.containsKey(nodeIteration)) {
            distTo.put(nodeIteration, distTo.get(v) + 1);
          }
          edgeTo.put(nodeIteration, v);
          marked.put(nodeIteration, true);
          queue.add(nodeIteration);
        }
        Iterator<Edge<T>> edgeIterator = nodeIteration.getNeighbors().iterator();
        while(edgeIterator.hasNext()) {
          Edge<T> edge = edgeIterator.next();
          Node<T> node = edge.getHead();
          Edge<T> reversedEdge = edge;
          Iterator<Edge<T>> edgeIterator1 = node.getNeighbors().iterator();
          while(edgeIterator1.hasNext()) {
            reversedEdge = edgeIterator1.next();
            if(reversedEdge.getTail() == edge.getHead() && reversedEdge.getHead() == edge.getTail()){
              break;
            }
          }
          if(distTo.containsKey(node) && distTo.get(node) < distTo.get(nodeIteration)) {
            lastNode.put(node, reversedEdge);
          }

        }
        lastNodes.put(nodeIteration, lastNode);
      }
    }
    computeEdgeEccentricity();
  }

  private void updateEdgeEccentricity(Node<T> node, Double number) {

    Double distance = distTo.get(node);
    if(distance == 0) {
    } else if (distance == 1) {
      Set<Node<T>> nodes = lastNodes.get(node).keySet();
      Double size = (double) nodes.size();
      if(size == 1) {
        size = number;
      }
      Iterator<Node<T>> nodeIterator = nodes.iterator();
      while(nodeIterator.hasNext()) {
        Node<T> tNode = nodeIterator.next();
        Edge<T> edge = lastNodes.get(node).get(tNode);
        if (edgeEccentricity.containsKey(edge)) {
          edgeEccentricity.put(edge, edgeEccentricity.get(edge) + 1/size);
          edgesVisited.put(edge,true);
        } else {
          edgeEccentricity.put(edge, 1/size);
          edgesVisited.put(edge,true);
        }
      }
    } else if (distance > 1) {
      Set<Node<T>> nodes = lastNodes.get(node).keySet();
      Double size = (double) nodes.size();
      if(size == 1) {
        size = number;
      }
      Iterator<Node<T>> nodeIterator = nodes.iterator();
      while(nodeIterator.hasNext()) {
        Node<T> tNode = nodeIterator.next();
        Edge<T> edge = lastNodes.get(node).get(tNode);
        if (edgeEccentricity.containsKey(edge)) {
          edgeEccentricity.put(edge, edgeEccentricity.get(edge) + (1 / size));
          edgesVisited.put(edge,true);
        } else {
          edgeEccentricity.put(edge, 1/size);
          edgesVisited.put(edge,true);
        }
        updateEdgeEccentricity(tNode, size);
      }
    }
  }

  private void computeEdgeEccentricity() {
    Set<Node<T>> nodeSet = lastNodes.keySet();
    Iterator<Node<T>> nodeIterator = nodeSet.iterator();
    while(nodeIterator.hasNext()) {
      Node<T> node = nodeIterator.next();
      updateEdgeEccentricity(node, 1d);
    }

  }
  public boolean hasPathTo(Node v) {
    if (marked.containsKey(v)) {
      return marked.get(v);
    }
    return false;
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

  public Map<Node<T>, Map<Node<T>, Edge<T>>> getLastNodes() {
    return lastNodes;
  }

  public Map<Edge<T>, Boolean> getEdgesVisited() {
    return edgesVisited;
  }

  public Map<Edge<T>, Double> getEdgeEccentricity() {
    return edgeEccentricity;
  }

  public static void main(String[] args) throws java.io.IOException {
    // TO DO: Generic Search, interface to implement that
    GraphList graph = new GraphList("src/graph-test.txt", " ");
    Node firstNode = graph.getNode("1");
    BreadthFirstPaths bfsPaths = new BreadthFirstPaths(graph, firstNode);
//    System.out.println(bfsPaths.hasPathTo(graph.getNode("7")));
    Map<Node, Double> distTo = bfsPaths.getDistTo();
    Set<Node> nodeSet = distTo.keySet();
    Iterator<Node> nodeIterator = nodeSet.iterator();
    /*while (nodeIterator.hasNext()) {
      Node node = nodeIterator.next();
      System.out.println(node.getNodeName() + ": " + distTo.get(node));
    }*/

    Stack<Node> nodeStack = bfsPaths.pathTo(graph.getNode("4"));
    /*while (!nodeStack.isEmpty()) {
      System.out.println(nodeStack.pop().getNodeName());
    }*/

    Map<Edge, Double> edgeEccentricity = new HashMap<>();
    Iterator<Node> nodeIterator1 = graph.getAdjList().iterator();
    Map<Node, BreadthFirstPaths> breadthFirstPaths = new HashMap<>();
    while (nodeIterator1.hasNext()) {
      Node node = nodeIterator1.next();
      breadthFirstPaths.put(node, new BreadthFirstPaths(graph, node));
    }
    Set<Node> nodeSet1 = breadthFirstPaths.keySet();
    Iterator<Node> nodeIterator2 = nodeSet1.iterator();
    while (nodeIterator2.hasNext()) {
      Node node = nodeIterator2.next();
//      System.out.println(node.getNodeName());
      BreadthFirstPaths breadthFirstPaths1 = breadthFirstPaths.get(node);
      Map<Edge, Double> edgeEccentricities = breadthFirstPaths1.getEdgeEccentricity();
      Set<Edge> edges = edgeEccentricities.keySet();
      Iterator<Edge> edgeIterator = edges.iterator();
      while (edgeIterator.hasNext()) {
        Edge edge = edgeIterator.next();
        if(edgeEccentricity.containsKey(edge)) {
          edgeEccentricity.put(edge, edgeEccentricity.get(edge) + edgeEccentricities.get(edge));
        } else {
          edgeEccentricity.put(edge, edgeEccentricities.get(edge));
        }
//        System.out.println("edge: " + edge + ", eccentricity: " + edgeEccentricities.get(edge));
      }
    }
    Set<Edge> edges = edgeEccentricity.keySet();
    Iterator<Edge> edgeIterator = edges.iterator();
    while (edgeIterator.hasNext()) {
      Edge edge = edgeIterator.next();
      System.out.println("edge: " + edge + ", eccentricity: " + edgeEccentricity.get(edge));
    }
    System.out.println(bfsPaths.getEdgesVisited());
  }
}
