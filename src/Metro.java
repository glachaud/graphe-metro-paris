import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by guillaumelachaud on 5/24/17.
 */
public class Metro {
  GraphList<MetroNode> G;

  public Metro(String fileName) {
    G = new GraphList();
    try {

      ObjectMapper mapper = new ObjectMapper();

      // read JSON from a file
      Map<String, Object> map = mapper.readValue(
              new File(fileName),
              new TypeReference<Map<String, Object>>() {
              });

      createNodes(map);
      createEdges(map);

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
    MetroNode metroNode;
    Node<MetroNode> node;

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
        metroNode = new MetroNode(id, nom, metros, lat, lng);
        node = new Node(metroNode);
        G.addNode(node);
      }
    }
  }

  private void createEdges(Map<String, Object> map) {
    ArrayList arrayList = (ArrayList) map.get("routes");
    Iterator<LinkedHashMap> route = arrayList.iterator();
    while (route.hasNext()) {
      LinkedHashMap linkedHashMap = route.next();
      if (linkedHashMap.get("type").equals("metro")) {
        ArrayList arrets = (ArrayList) linkedHashMap.get("arrets");
        Iterator iterator = arrets.iterator();
        if (iterator.hasNext()) {
          Node<MetroNode> tail = getNode(Integer.parseInt(iterator.next().toString()));
          while (iterator.hasNext()) {
            Integer arret = Integer.parseInt(iterator.next().toString());
            Node<MetroNode> head = getNode(arret);
            Edge<MetroNode> edge = new Edge(tail, head);
            G.addWeightedEdge(tail, head, getDistance(tail, head));
//            G.addWeightedEdge(tail, head, 1d);
            tail = head;
          }
        }
      }
    }
  }


  public Node<MetroNode> getNode(Integer id) {
    Iterator<Node<MetroNode>> iterator = G.getAdjList().iterator();
    Node<MetroNode> node;
    while (iterator.hasNext()) {
      node = iterator.next();
      if (node.getNode().getId().equals(id)) {
        return node;
      }
    }
    return null;
  }

  public Node<MetroNode> getNode(String name) {
    Iterator<Node<MetroNode>> iterator = G.getAdjList().iterator();
    Node<MetroNode> node;
    while (iterator.hasNext()) {
      node = iterator.next();
      if (node.getNode().getNodeName().equals(name)) {
        return node;
      }
    }
    return null;
  }

  public static Double getDistance(Node<MetroNode> tail, Node<MetroNode> head) {
    Double deglen = 110.25;
    Double x = head.getNode().getLat() - tail.getNode().getLat();
    Double y = (head.getNode().getLng() - tail.getNode().getLng())*Math.cos(Math.toRadians(tail.getNode().getLat()));
    return deglen * Math.sqrt(x * x + y * y);
  }

  public GraphList<MetroNode> getG() {
    return G;
  }

  public void setG(GraphList<MetroNode> g) {
    G = g;
  }

  public Stack<Node<MetroNode>> getLongestShortestPath() {

    Double distance = 0d;
    Stack<Node<MetroNode>> edgeTo = new Stack();

    ArrayList<Node<MetroNode>> arrayList = G.getAdjList();
    Iterator<Node<MetroNode>> iterator = arrayList.iterator();
    HashMap<Node<MetroNode>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<MetroNode> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(G, nodeNode));
    }

    Iterator<DijkstraSP> dijkstraSPIterator = nodeDijkstraSPHashMap.values().iterator();
    while (dijkstraSPIterator.hasNext()) {
      DijkstraSP dijkstraSP = dijkstraSPIterator.next();
      Iterator<Node> keyIterator = dijkstraSP.getDistTo().keySet().iterator();
      while (keyIterator.hasNext()) {
        Node<MetroNode> node = keyIterator.next();
        Double dijsktraDistance = dijkstraSP.distTo(node);
        if(dijsktraDistance > distance) {
          distance = dijsktraDistance;
          edgeTo = dijkstraSP.shortestPathTo(node);
        }
      }
    }
    System.out.println(distance);

    return edgeTo;
  }

  /*public Map<Edge<MetroNode>, Double> getEdgesEccentricity() {
    Map<Edge<MetroNode>, Double> edgeDoubleMap = new HashMap<Edge<MetroNode>, Double>();

    ArrayList<Node<MetroNode>> arrayList = G.getAdjList();
    Iterator<Node<MetroNode>> iterator = arrayList.iterator();
    HashMap<Node<MetroNode>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<MetroNode> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(G, nodeNode));
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
    Metro metro = new Metro("src/reseau.json");
    GraphList<MetroNode> G = metro.getG();
//    Iterator<Node<MetroNode>> iterator = G.getAdjList().iterator();
//    while (iterator.hasNext()) {
//      Node<MetroNode> nodeNode = iterator.next();
//      MetroNode metroNode = nodeNode.getNode();
//      System.out.println(metroNode.getId() + ": " + nodeNode.getNodeName());
//    }
    DijkstraSP dijkstraSP = new DijkstraSP(G, metro.getNode("Mairie d'Issy"));

//    Stack<Node<MetroNode>> nodeStack = dijkstraSP.shortestPathTo(metro.getNode("Bercy"));
//    while (!nodeStack.isEmpty()) {
//      System.out.println(nodeStack.pop().getNodeName());
//    }
//    System.out.println(dijkstraSP.distTo(metro.getNode("Bercy")));

    ArrayList<Node<MetroNode>> arrayList = metro.getG().getAdjList();
    Iterator<Node<MetroNode>> iterator = arrayList.iterator();
    HashMap<Node<MetroNode>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<MetroNode> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(G, nodeNode));
    }

//    DijkstraSP dijkstraSP1 = nodeDijkstraSPHashMap.get(metro.getNode("Bercy"));
//    Stack<Node<MetroNode>> nodeStack1 = dijkstraSP1.shortestPathTo(metro.getNode("Marcel Sembat"));
//    while (!nodeStack1.isEmpty()) {
//      System.out.println(nodeStack1.pop().getNodeName());
//    }
//    System.out.println(dijkstraSP1.distTo(metro.getNode("Marcel Sembat")));


//    Stack<Node<MetroNode>> nodeStack2 = metro.getLongestShortestPath();
//    while (!nodeStack2.isEmpty()) {
//      System.out.println(nodeStack2.pop().getNodeName());
//    }


    System.out.println("--------------");
    DijkstraSP dijkstraSP1 = nodeDijkstraSPHashMap.get(metro.getNode("Porte d'Auteuil"));
    Stack<Node<MetroNode>> nodeStack1 = dijkstraSP1.shortestPathTo(metro.getNode("Pointe du Lac"));
    Double distance = 0d;


    Stack<Node<MetroNode>> nodeStack2 = metro.getLongestShortestPath();
    while (!nodeStack2.isEmpty()) {
      distance += 1;
      Node<MetroNode> node = nodeStack2.pop();
      System.out.println(node.getNodeName());
    }

  }

}
