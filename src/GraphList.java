import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphList<T> {
  private int nodeCount;
  private int edgeCount;
  private ArrayList<Node<T>> adj;

  public GraphList() {
    nodeCount = 0;
    edgeCount = 0;
    adj = new ArrayList<Node<T>>();
  }


  public void addNode(Node node) {
    adj.add(node);
    nodeCount++;
  }

  public void addEdge(Node u, Node v) {
    addWeightedEdge(u, v, 1d);
  }

  public void addWeightedEdge(Node u, Node v, Double weight) {
    Edge newEdge = new Edge(u, v, weight);
    if (adj.indexOf(u) > -1 && adj.indexOf(v) > -1) {
      Iterator<Edge<T>> edgeIterator = u.getNeighbors().iterator();
      Boolean exists = false;
      while (edgeIterator.hasNext()) {
        Edge<T> edge = edgeIterator.next();
        if (edge.equals(newEdge)) {
          exists = true;
        }
      }
      if (!exists) {
        adj.get(adj.indexOf(u)).addEdge(newEdge);
        edgeCount++;
      }
    } else {
      System.out.println("This edge couldn't be added to the graph");
    }

  }


  public Node getNode(T nodeName) {
    Iterator<Node<T>> iterator = adj.iterator();
    Node node;
    while (iterator.hasNext()) {
      node = iterator.next();
      if (node.getNodeName().equals(nodeName)) {
        return node;
      }
    }
    return null;
  }

  public T[] getNodeNeighbors(Node node) {
    int neighborIndex = 0;
    List<Edge> neighbors = node.getNeighbors();
    T[] nodeNeighbors = (T[]) new Object[neighbors.size()];
    Node[] nodes;
    Iterator<Edge> iterator = neighbors.iterator();
    Edge edge;
    while (iterator.hasNext()) {
      edge = iterator.next();
      nodes = edge.getNodes();
      if (!nodes[0].equals(node)) {
        nodeNeighbors[neighborIndex] = (T) nodes[0].getNodeName();
        neighborIndex++;
      } else {
        nodeNeighbors[neighborIndex] = (T) nodes[1].getNodeName();
        neighborIndex++;
      }
    }
    return nodeNeighbors;
  }


  public void printGraph() {
    Iterator<Node<T>> firstIterator = adj.iterator();
    Node nodePasser;
    while (firstIterator.hasNext()) {
      nodePasser = firstIterator.next();
      System.out.print(nodePasser.getNodeName() + ": ");
      System.out.println(Arrays.toString(getNodeNeighbors(nodePasser)));
    }

  }

  public ArrayList<Edge<T>> getAllEdges() {
    ArrayList<Edge<T>> graphEdges = new ArrayList<>();
    Iterator<Node<T>> nodeIterator = adj.iterator();
    while (nodeIterator.hasNext()) {
      Node nodeIteration = nodeIterator.next();
      Iterator<Edge<T>> edgeIterator = nodeIteration.getNeighbors().iterator();
      while (edgeIterator.hasNext()) {
        Edge<T> edgeIteration = edgeIterator.next();
        graphEdges.add(edgeIteration);
      }
    }

    return graphEdges;
  }

  public int getNodeCount() {
    return nodeCount;
  }

  public int getEdgeCount() {
    return edgeCount;
  }

  public ArrayList<Node<T>> getAdjList() {
    return adj;
  }

  public static void main(String[] args) throws java.io.IOException {
  }
}
