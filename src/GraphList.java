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

  public GraphList(boolean fromConsole) throws java.io.IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("How do you want to call your graph?");
    String file = scanner.nextLine();
    file = "src/" + file + ".txt";
    System.out.println("Is the graph directed? (y/n)");
    boolean isDirectedGraph = scanner.nextLine().equals("y");
    System.out.println("Enter the number of vertices");
    int nodeCount = Integer.parseInt(scanner.nextLine());
    System.out.println("Enter the number of edges");
    int edgeCount = Integer.parseInt(scanner.nextLine());
    PrintWriter writer = new PrintWriter(file, "UTF-8");
    String line;
    String splitDelimiter = " ";
    for (int i = 0; i < edgeCount; i++) {
      System.out.println("Enter the edges in the graph: <to> <from> <weight>(optional)");
      line = scanner.nextLine();
      writer.println(line);
    }
    writer.close();
    scanner.close();
    createGraph(file, splitDelimiter, isDirectedGraph);

  }

  public GraphList(int nodeCount) {
    this.nodeCount = nodeCount;
    this.edgeCount = 0;
    adj = new ArrayList<>();
  }

  public GraphList(String file, String splitDelimiter) throws java.io.IOException {
    this(file, splitDelimiter, false);
  }

  public GraphList(String file, String splitDelimiter, boolean isDirectedGraph) throws java.io.IOException {
    createGraph(file, splitDelimiter, isDirectedGraph);
  }

  public void createGraph(String file, String splitDelimiter, boolean isDirectedGraph) throws java.io.IOException {
    Map<Node, Integer> mappingOfVertices = GraphFunctions.countNumberOfNodes(file, splitDelimiter);
    this.nodeCount = mappingOfVertices.size();
    this.adj = new ArrayList<>();
    Set<Node> keySet = mappingOfVertices.keySet();
    Iterator<Node> iterator = keySet.iterator();
    Node node;
    while (iterator.hasNext()) {
      node = iterator.next();
      adj.add(node);
    }
    fillAdjList(file, splitDelimiter, isDirectedGraph);
  }


  public void fillAdjList(String file, String splitDelimiter, boolean isDirectedGraph) throws java.io.IOException {
    List<String> lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
    String line;
    String[] edge;
    Iterator<String> lineIterator = lines.iterator();
    Node<T> tail, head;
    Edge newEdge;
    double weight;
    int numberOfEdges = 0;
    while (lineIterator.hasNext()) {
      line = lineIterator.next();
      line = line.replaceAll("\" ", "-");
      line = line.replaceAll("\"", "");
      edge = line.split(splitDelimiter);
      tail = this.getNode((T) edge[0]);
      head = this.getNode((T) edge[1]);
      weight = 1;
      if (edge.length == 3) {
        edge[2] = edge[2].replaceAll("^\"|\"$", "");
        weight = Double.parseDouble(edge[2]);
      }
      newEdge = new Edge(tail, head, weight);
      adj.get(adj.indexOf(tail)).addEdge(newEdge);
      numberOfEdges++;
      if (!isDirectedGraph) {
        newEdge = new Edge(head, tail, weight);
        adj.get(adj.indexOf(head)).addEdge(newEdge);
        numberOfEdges++;
      }
    }
    this.edgeCount = numberOfEdges;
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
      while(edgeIterator.hasNext()) {
        Edge<T> edge = edgeIterator.next();
        if (edge.equals(newEdge)) {
          exists= true;
        }
      }
      if(!exists) {
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

  public ArrayList<Edge> getAllEdges(){
    ArrayList<Edge> graphEdges = new ArrayList<>();
    Iterator<Node<T>> nodeIterator = adj.iterator();
    while(nodeIterator.hasNext()){
      Node nodeIteration = nodeIterator.next();
      Iterator<Edge> edgeIterator = nodeIteration.getNeighbors().iterator();
      while(edgeIterator.hasNext()){
        Edge edgeIteration = edgeIterator.next();
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
    GraphList graph = new GraphList("src/karate_weighted.txt", "-");
    graph.printGraph();
    /*
    GraphList graph2 = new GraphList();
    graph2.printGraph();
    */
  }
}
