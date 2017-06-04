import java.util.*;

/**
 * Created by guillaumelachaud on 4/19/17.
 */
public class DijkstraSP<T> {

  private Map<Node, Edge> edgeTo;
  private Map<Node, Double> distTo;
  PriorityQueue<Node<T>> pq;
  private final Node s;


  public DijkstraSP(GraphList G, Node<T> s) {

    edgeTo = new HashMap<>();
    distTo = new HashMap<>();
    pq = new PriorityQueue<>();
    this.s = s;

    if (!verifyNonNegative(G)) {
      System.out.println("The graph contains negative edges");
    } else {

      Iterator<Node<T>> iterator = G.getAdjList().iterator();
      while (iterator.hasNext()) {
        Node<T> nodeIteration = iterator.next();
        distTo.put(nodeIteration, Double.POSITIVE_INFINITY);
      }
      distTo.put(s, 0.0);

      pq.add(s);
      int i = 0;
      while (!pq.isEmpty()) {
        relax(G, pq.poll());
      }
    }
  }

  private void relax(GraphList G, Node v) {
    Iterator<Edge> edgeIterator = v.getNeighbors().iterator();
    while (edgeIterator.hasNext()) {
      Edge edgeIteration = edgeIterator.next();
      Node<T> head = edgeIteration.getHead();
      if (distTo.get(head) > distTo.get(v) + edgeIteration.getWeight()) {
        distTo.put(head, distTo.get(v) + edgeIteration.getWeight());
        edgeTo.put(head, edgeIteration);
        if (!pq.contains(head)) {
          pq.add(head);
        }
      }
    }
  }

  public double distTo(Node v) {
    return distTo.get(v);
  }

  public boolean hasPathTo(Node v) {
    return distTo.get(v) < Double.POSITIVE_INFINITY;
  }

  public Stack<Node> shortestPathTo(Node v) {
    if (!hasPathTo(v)) {
      return null;
    }
    Stack<Node> path = new Stack<Node>();
    Node currentNode = v;
    while (currentNode != s) {
      path.push(currentNode);
      currentNode = edgeTo.get(currentNode).getTail();
    }
    path.push(s);
    return path;
  }

  public Node getPreviousNode(Node v) {
    return edgeTo.get(v).getTail();
  }

  public boolean verifyNonNegative(GraphList G) {
    Iterator<Edge> edgeIterator = G.getAllEdges().iterator();
    while (edgeIterator.hasNext()) {
      if (edgeIterator.next().getWeight() < 0) {
        return false;
      }
    }
    return true;
  }

  public Map<Node, Edge> getEdgeTo() {
    return edgeTo;
  }

  public void setEdgeTo(Map<Node, Edge> edgeTo) {
    this.edgeTo = edgeTo;
  }

  public Map<Node, Double> getDistTo() {
    return distTo;
  }

  public void setDistTo(Map<Node, Double> distTo) {
    this.distTo = distTo;
  }

  public PriorityQueue<Node<T>> getPq() {
    return pq;
  }

  public void setPq(PriorityQueue<Node<T>> pq) {
    this.pq = pq;
  }

  public Node getS() {
    return s;
  }

  public static void main(String[] args) throws java.io.IOException {
    GraphList graph = new GraphList("src/facebook_combined.txt", " ", true);
    DijkstraSP graphComponent = new DijkstraSP(graph, graph.getNode("0"));
    Stack<Node> nodeStack = graphComponent.shortestPathTo(graph.getNode("2223"));
    while (!nodeStack.isEmpty()) {
      System.out.println(nodeStack.pop().getNodeName());
    }
    System.out.println("The distance is: " + graphComponent.distTo(graph.getNode("2223")));

  }
}
