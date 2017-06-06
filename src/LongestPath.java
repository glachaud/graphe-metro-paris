import java.util.*;

/**
 * Created by guillaumelachaud on 6/6/17.
 */
public class LongestPath {

  public static Map<Stack<Node<Station>>, Double> getLongestPathWeighted(GraphList<Station> metroGraph) {

    Double distance = 0d;
    Stack<Node<Station>> longestPathStack = new Stack<>();
    Map<Stack<Node<Station>>, Double> longestPath = new HashMap<Stack<Node<Station>>, Double>();

    ArrayList<Node<Station>> allStations = metroGraph.getAdjList();
    Iterator<Node<Station>> stations = allStations.iterator();
    HashMap<Node<Station>, DijkstraSP> allDijkstraSPs = new HashMap<>();
    while (stations.hasNext()) {
      Node<Station> nodeNode = stations.next();
      allDijkstraSPs.put(nodeNode, new DijkstraSP(metroGraph, nodeNode));
    }

    Iterator<DijkstraSP> dijkstraSPs = allDijkstraSPs.values().iterator();
    while (dijkstraSPs.hasNext()) {
      DijkstraSP dijkstraSP = dijkstraSPs.next();
      Iterator<Node> dijkstraSPKeys = dijkstraSP.getDistTo().keySet().iterator();
      while (dijkstraSPKeys.hasNext()) {
        Node<Station> dijkstraSPKey = dijkstraSPKeys.next();
        Double dijsktraSPDistance = dijkstraSP.distTo(dijkstraSPKey);
        if (dijsktraSPDistance > distance) {
          distance = dijsktraSPDistance;
          longestPathStack = dijkstraSP.shortestPathTo(dijkstraSPKey);
        }
      }
    }
    longestPath.put(longestPathStack, distance);

    return longestPath;
  }

  public static Map<Stack<Node<Station>>, Double> getLongestPathUnweighted(GraphList<Station> metroGraph) {

    Double distance = 0d;
    Stack<Node<Station>> longestPathStack = new Stack<>();
    Map<Stack<Node<Station>>, Double> longestPath = new HashMap<Stack<Node<Station>>, Double>();

    ArrayList<Node<Station>> allStations = metroGraph.getAdjList();
    Iterator<Node<Station>> stations = allStations.iterator();
    HashMap<Node<Station>, BreadthFirstPaths> allBFSPaths = new HashMap<>();
    while (stations.hasNext()) {
      Node<Station> station = stations.next();
      allBFSPaths.put(station, new BreadthFirstPaths(metroGraph, station));
    }

    Iterator<BreadthFirstPaths> bfsPaths = allBFSPaths.values().iterator();
    while (bfsPaths.hasNext()) {
      BreadthFirstPaths bfsPath = bfsPaths.next();
      Iterator<Node> bfsPathKeys = bfsPath.getDistTo().keySet().iterator();
      while (bfsPathKeys.hasNext()) {
        Node<Station> bfsPathKey = bfsPathKeys.next();
        Double bfsDistance = (Double) bfsPath.getDistTo().get(bfsPathKey);
        if (bfsDistance > distance) {
          distance = bfsDistance;
          Node<Station> nodeTo = (Node<Station>) bfsPath.getEdgeTo().get(bfsPathKey);
          longestPathStack = bfsPath.shortestPathTo(bfsPathKey);
        }
      }
    }
    longestPath.put(longestPathStack, distance);

    return longestPath;
  }
}
