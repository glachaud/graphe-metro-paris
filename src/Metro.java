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


  public static void main(String[] args) {
    Metro metro = new Metro("src/reseau.json", true);
    GraphList<Station> graph = metro.getMetroGraph();

    Stack<Node<Station>> nodeStack = LongestPath.getLongestPathWeighted(graph);
    Iterator<Node<Station>> nodeIterator = nodeStack.iterator();
    while(nodeIterator.hasNext()) {
      Node<Station> node = nodeIterator.next();
      System.out.println(node.getNodeName());
    }



    Map<Edge<Station>, Double> edgeEccentricityUnweighted = MapCluster.getUnweightedCluster(metro.getMetroGraph());
    ValueComparator bvc = new ValueComparator(edgeEccentricityUnweighted);
    TreeMap<Edge<Station>, Double> sorted_map = new TreeMap<Edge<Station>, Double>(bvc);
    sorted_map.putAll(edgeEccentricityUnweighted);
    Edge[] edgesUnweighted = Arrays.copyOf(sorted_map.keySet().toArray(), sorted_map.keySet().size(), Edge[].class);
    Double[] eccentricitiesUnweighted = Arrays.copyOf(sorted_map.values().toArray(), sorted_map.keySet().size(), Double[].class);

    Map<Edge<Station>, Double> edgeEccentricityWeighted = MapCluster.getWeigthedCluster(graph);
    ValueComparator bvc2 = new ValueComparator(edgeEccentricityWeighted);
    TreeMap<Edge<Station>, Double> sorted_map2 = new TreeMap<Edge<Station>, Double>(bvc2);
    sorted_map2.putAll(edgeEccentricityWeighted);
    Edge[] edgesWeighted = Arrays.copyOf(sorted_map2.keySet().toArray(), sorted_map2.keySet().size(), Edge[].class);
    Double[] eccentricitesWeighted = Arrays.copyOf(sorted_map2.values().toArray(), sorted_map2.keySet().size(), Double[].class);

    try {
      PrintWriter writerUnweighted = new PrintWriter("edge-eccentricity-unweighted.txt", "UTF-8");
      for (int i = 0; i < edgesUnweighted.length; i++) {
        writerUnweighted.println(edgesUnweighted[i] + ": " + eccentricitiesUnweighted[i]);
      }
      writerUnweighted.close();


      PrintWriter writerWeighted = new PrintWriter("edge-eccentricity-weighted.txt", "UTF-8");

      for (int i = 0; i < edgesWeighted.length; i++) {
        writerWeighted.println(edgesWeighted[i] + ": " + eccentricitesWeighted[i]);
      }
      writerWeighted.close();

    } catch (IOException e) {
      // do something
    }

  }
}
