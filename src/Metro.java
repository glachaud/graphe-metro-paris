import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by guillaumelachaud on 5/24/17.
 */
public class Metro {
  GraphList<Station> metroGraph;

  public Metro(String fileName, Boolean isWeighted) {
    metroGraph = new GraphList();
    try {

      ObjectMapper mapper = new ObjectMapper();

      // read JSON from a file
      Map<String, Object> map = mapper.readValue(
              new File(fileName),
              new TypeReference<Map<String, Object>>() {
              });

      createNodes(map);
      createEdges(map, isWeighted);

    } catch (
            JsonGenerationException e)

    {
      e.printStackTrace();
    } catch (
            JsonMappingException e)

    {
      e.printStackTrace();
    } catch (
            IOException e)

    {
      e.printStackTrace();
    }


  }

  private void createNodes(Map<String, Object> map) {
    Station metroNode;
    Node<Station> node;

    LinkedHashMap linkedHashMap = (LinkedHashMap) map.get("stations");
//    System.out.println(linkedHashMap);
    Iterator iterator = linkedHashMap.values().iterator();
    while (iterator.hasNext()) {
      LinkedHashMap linkedHashMap1 = (LinkedHashMap) iterator.next();
      String type = linkedHashMap1.get("type").toString();
      LinkedHashMap lignes = (LinkedHashMap) linkedHashMap1.get("lignes");
      ArrayList metros = (ArrayList) lignes.get("metro");
      if (metros != null) {
        Integer id = Integer.parseInt(linkedHashMap1.get("num").toString());
        Double lat = Double.parseDouble(linkedHashMap1.get("lat").toString());
        Double lng = Double.parseDouble(linkedHashMap1.get("lng").toString());
        Boolean isHub = Boolean.parseBoolean(linkedHashMap1.get("isHub").toString());
        String nom = linkedHashMap1.get("nom").toString();
        metroNode = new Station(id, nom, metros, lat, lng);
        node = new Node(metroNode);
        metroGraph.addNode(node);
      }
    }
  }

  private void createEdges(Map<String, Object> map, Boolean isWeighted) {
    ArrayList arrayList = (ArrayList) map.get("routes");
    Iterator<LinkedHashMap> route = arrayList.iterator();
    while (route.hasNext()) {
      LinkedHashMap linkedHashMap = route.next();
      if (linkedHashMap.get("type").equals("metro")) {
        String ligne = linkedHashMap.get("ligne").toString();
        ArrayList arrets = (ArrayList) linkedHashMap.get("arrets");
        Iterator iterator = arrets.iterator();
        if (iterator.hasNext()) {
          Node<Station> tail = getNode(Integer.parseInt(iterator.next().toString()));
          while (iterator.hasNext()) {
            Integer arret = Integer.parseInt(iterator.next().toString());
            Node<Station> head = getNode(arret);
            Edge<Station> edge = new Edge(ligne, tail, head);
            if (isWeighted) {
              metroGraph.addWeightedEdge(tail, head, getDistance(tail, head));
            } else {
              metroGraph.addWeightedEdge(tail, head, 1d);
            }
            tail = head;
          }
        }
      }
    }
  }


  public Node<Station> getNode(Integer id) {
    Iterator<Node<Station>> iterator = metroGraph.getAdjList().iterator();
    Node<Station> node;
    while (iterator.hasNext()) {
      node = iterator.next();
      if (node.getNode().getId().equals(id)) {
        return node;
      }
    }
    return null;
  }

  public Node<Station> getNode(String name) {
    Iterator<Node<Station>> iterator = metroGraph.getAdjList().iterator();
    Node<Station> node;
    while (iterator.hasNext()) {
      node = iterator.next();
      if (node.getNode().getNodeName().equals(name)) {
        return node;
      }
    }
    return null;
  }

  public static Double getDistance(Node<Station> tail, Node<Station> head) {
    Double deglen = 110.25;
    Double x = head.getNode().getLat() - tail.getNode().getLat();
    Double y = (head.getNode().getLng() - tail.getNode().getLng()) * Math.cos(Math.toRadians(tail.getNode().getLat()));
    return deglen * Math.sqrt(x * x + y * y);
  }

  public GraphList<Station> getMetroGraph() {
    return metroGraph;
  }

  public void setMetroGraph(GraphList<Station> metroGraph) {
    this.metroGraph = metroGraph;
  }

  public Stack<Node<Station>> getLongestShortestPath() {

    Double distance = 0d;
    Stack<Node<Station>> edgeTo = new Stack();

    ArrayList<Node<Station>> arrayList = metroGraph.getAdjList();
    Iterator<Node<Station>> iterator = arrayList.iterator();
    HashMap<Node<Station>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<Station> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(metroGraph, nodeNode));
    }

    Iterator<DijkstraSP> dijkstraSPIterator = nodeDijkstraSPHashMap.values().iterator();
    while (dijkstraSPIterator.hasNext()) {
      DijkstraSP dijkstraSP = dijkstraSPIterator.next();
      Iterator<Node> keyIterator = dijkstraSP.getDistTo().keySet().iterator();
      while (keyIterator.hasNext()) {
        Node<Station> node = keyIterator.next();
        Double dijsktraDistance = dijkstraSP.distTo(node);
        if (dijsktraDistance > distance) {
          distance = dijsktraDistance;
          edgeTo = dijkstraSP.shortestPathTo(node);
        }
      }
    }
    System.out.println(distance);

    return edgeTo;
  }

  /*public Map<Edge<Station>, Double> getEdgesEccentricity() {
    Map<Edge<Station>, Double> edgeDoubleMap = new HashMap<Edge<Station>, Double>();

    ArrayList<Node<Station>> arrayList = metroGraph.getAdjList();
    Iterator<Node<Station>> iterator = arrayList.iterator();
    HashMap<Node<Station>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<Station> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(metroGraph, nodeNode));
    }

    Iterator<DijkstraSP> dijkstraSPIterator = nodeDijkstraSPHashMap.values().iterator();
    while (dijkstraSPIterator.hasNext()) {
      DijkstraSP dijkstraSP = dijkstraSPIterator.next();
      Iterator<Edge> edgeIterator = dijkstraSP.getEdgeTo().keySet().iterator();
      while(edgeIterator.hasNext()) {
        Edge edge = edgeIterator.next();
        if(edgeDoubleMap.containsKey(edge)) {
          edgeDoubleMap.put(edge, edgeDoubleMap.get(edge)+1);
        } else {
          edgeDoubleMap.put(edge, 1d);
        }
      }
    }

    return edgeDoubleMap;
  }*/

  public static void main(String[] args) {
    Metro metro = new Metro("src/reseau.json", false);
    GraphList<Station> graph = metro.getMetroGraph();
//    Iterator<Node<Station>> iterator = metroGraph.getAdjList().iterator();
//    while (iterator.hasNext()) {
//      Node<Station> nodeNode = iterator.next();
//      Station metroNode = nodeNode.getNode();
//      System.out.println(metroNode.getId() + ": " + nodeNode.getNodeName());
//    }
    DijkstraSP dijkstraSP = new DijkstraSP(graph, metro.getNode("Mairie d'Issy"));

//    Stack<Node<Station>> nodeStack = dijkstraSP.shortestPathTo(metro.getNode("Bercy"));
//    while (!nodeStack.isEmpty()) {
//      System.out.println(nodeStack.pop().getNodeName());
//    }
//    System.out.println(dijkstraSP.distTo(metro.getNode("Bercy")));

    ArrayList<Node<Station>> arrayList = metro.getMetroGraph().getAdjList();
    Iterator<Node<Station>> iterator = arrayList.iterator();
    HashMap<Node<Station>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<Station> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(graph, nodeNode));
    }

//    DijkstraSP dijkstraSP1 = nodeDijkstraSPHashMap.get(metro.getNode("Bercy"));
//    Stack<Node<Station>> nodeStack1 = dijkstraSP1.shortestPathTo(metro.getNode("Marcel Sembat"));
//    while (!nodeStack1.isEmpty()) {
//      System.out.println(nodeStack1.pop().getNodeName());
//    }
//    System.out.println(dijkstraSP1.distTo(metro.getNode("Marcel Sembat")));


//    Stack<Node<Station>> nodeStack2 = metro.getLongestShortestPath();
//    while (!nodeStack2.isEmpty()) {
//      System.out.println(nodeStack2.pop().getNodeName());
//    }


    System.out.println("--------------");
    DijkstraSP dijkstraSP1 = nodeDijkstraSPHashMap.get(metro.getNode("Porte d'Auteuil"));
    Stack<Node<Station>> nodeStack1 = dijkstraSP1.shortestPathTo(metro.getNode("Pointe du Lac"));
    Double distance = 0d;


    Stack<Node<Station>> nodeStack2 = metro.getLongestShortestPath();
    while (!nodeStack2.isEmpty()) {
      distance += 1;
      Node<Station> node = nodeStack2.pop();
      System.out.println(node.getNodeName());
    }



    Map<Edge, Double> edgeEccentricity = new HashMap<>();


    Iterator<Node<Station>> nodeIterator1 = graph.getAdjList().iterator();
    Map<Node, BreadthFirstPaths> breadthFirstPaths = new HashMap<>();
    while (nodeIterator1.hasNext()) {
      Node node = nodeIterator1.next();
      breadthFirstPaths.put(node, new BreadthFirstPaths(graph, node));
    }
    Set<Node> nodeSet1 = breadthFirstPaths.keySet();
    Iterator<Node> nodeIterator2 = nodeSet1.iterator();
    try{
      PrintWriter writer = new PrintWriter("console-output.txt", "UTF-8");
      while (nodeIterator2.hasNext()) {
        Node node = nodeIterator2.next();
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
        }
      }
      writer.close();

      ValueComparator bvc = new ValueComparator(edgeEccentricity);
      TreeMap<Edge, Double> sorted_map = new TreeMap<Edge, Double>(bvc);
      sorted_map.putAll(edgeEccentricity);
      PrintWriter writer1 = new PrintWriter("edge-eccentricities.txt", "UTF-8");
      writer1.println("results: " + sorted_map);
      writer1.close();

    } catch (IOException e) {
      // do something
    }
  }

}
class ValueComparator implements Comparator<Edge> {
  Map<Edge, Double> base;

  public ValueComparator(Map<Edge, Double> base) {
    this.base = base;
  }

  // Note: this comparator imposes orderings that are inconsistent with
  // equals.
  public int compare(Edge u, Edge v) {
    if (base.get(u) >= base.get(v)) {
      return -1;
    } else {
      return 1;
    } // returning 0 would merge keys
  }
}
