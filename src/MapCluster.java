import java.util.*;

/**
 * Created by guillaumelachaud on 6/6/17.
 */
public class MapCluster {

  public static Map<Edge<Station>, Double> getUnweightedCluster(GraphList<Station> metroGraph) {
    Map<Edge<Station>, Double> edgeEccentricity = new HashMap<>();

    Iterator<Node<Station>> stations = metroGraph.getAdjList().iterator();
    Map<Node<Station>, BreadthFirstPaths> breadthFirstPaths = new HashMap<>();
    while (stations.hasNext()) {
      Node<Station> station = stations.next();
      breadthFirstPaths.put(station, new BreadthFirstPaths(metroGraph, station));
    }
    Set<Node<Station>> bfsKeys = breadthFirstPaths.keySet();
    Iterator<Node<Station>> bfsKeysIterator = bfsKeys.iterator();

    while (bfsKeysIterator.hasNext()) {
      Node<Station> bfsKey = bfsKeysIterator.next();
      BreadthFirstPaths bfsPaths = breadthFirstPaths.get(bfsKey);
      Map<Edge<Station>, Double> edgeEccentricities = bfsPaths.getEdgeEccentricity();
      Set<Edge<Station>> edges = edgeEccentricities.keySet();
      Iterator<Edge<Station>> edgeIterator = edges.iterator();
      while (edgeIterator.hasNext()) {
        Edge<Station> edge = edgeIterator.next();
        if(edgeEccentricity.containsKey(edge)) {
          edgeEccentricity.put(edge, edgeEccentricity.get(edge) + edgeEccentricities.get(edge));
        } else {
          edgeEccentricity.put(edge, edgeEccentricities.get(edge));
        }
      }
    }

    return edgeEccentricity;
  }

  public static Map<Edge<Station>, Double> getWeigthedCluster(GraphList<Station> metroGraph){

    Map<Edge<Station>, Double> mapCluster = new HashMap<>();

    List<Edge<Station>> listEdge= metroGraph.getAllEdges();
    Double init = new Double(0);
    List<Node<Station>> listeNode = metroGraph.getAdjList();

    ArrayList<Node<Station>> arrayList = metroGraph.getAdjList();
    Iterator<Node<Station>> iterator = arrayList.iterator();
    HashMap<Node<Station>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
    while (iterator.hasNext()) {
      Node<Station> nodeNode = iterator.next();
      nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(metroGraph, nodeNode));
    }

    for(Edge<Station> edge : listEdge){
      mapCluster.put(edge, init);
    }

    iterator = arrayList.iterator();

    while(iterator.hasNext()){
      Node<Station> node = iterator.next();
      DijkstraSP<Station> dijkstra = nodeDijkstraSPHashMap.get(node);

      for(Node<Station> nodeTest : listeNode){

        if(!nodeTest.equals(node)){

          Edge edgeTest = dijkstra.getEdgeTo().get(nodeTest);

          mapCluster.put(edgeTest, mapCluster.get(edgeTest)+1);

          while(!node.equals(edgeTest.tail)){

            edgeTest = dijkstra.getEdgeTo().get(edgeTest.tail);

            mapCluster.put(edgeTest, mapCluster.get(edgeTest)+1);

          }
        }
      }
    }

    return mapCluster;
  }
}
