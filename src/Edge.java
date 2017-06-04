public class Edge<T> implements Comparable<Edge> {
  String ligne;
  Node<T> tail;
  Node<T> head;
  double weight;

  public Edge(Node u, Node v) {
    this(u, v, 1);
  }

  public Edge(Node u, Node v, double weight) {
    this.tail = u;
    this.head = v;
    this.weight = weight;
  }

  public Edge(String ligne, Node u, Node v) {
    this.ligne = ligne;
    this.tail = u;
    this.head = v;
  }

  public Edge(String ligne, Node u, Node v, double weight) {
    this.ligne = ligne;
    this.tail = u;
    this.head = v;
    this.weight = weight;
  }

  public String getLigne() {
    return ligne;
  }

  public Node<T> getTail() {
    return tail;
  }

  public Node<T> getHead() {
    return head;
  }

  public double getWeight() {
    return weight;
  }

  public Node<T>[] getNodes() {
    Node[] nodes = new Node[2];
    nodes[0] = tail;
    nodes[1] = head;
    return nodes;
  }

  @Override
  public String toString() {
    return "Tail: " + tail.getNodeName() + " - Head: " + head.getNodeName() + " - Weight: " + weight;
  }

  // "Note: this class has a natural ordering that is inconsistent with equals."
  @Override
  public int compareTo(Edge edge) {
    return (this.getWeight() > edge.getWeight() ? 1 : -1);
  }

}
