import java.util.*;

/**
 * Created by Arnaud on 05/06/2017.
 */
public class BordelArnaud {

    public static Map<Edge, Double> testWeigthedCluster(GraphList<Station> metroGraph){

        Map<Edge, Double> mapCluster = new HashMap<>();

        List<Edge> listEdge= metroGraph.getAllEdges();
        Double init = new Double(0);
        List<Node<Station>> listeNode = metroGraph.getAdjList();

        ArrayList<Node<Station>> arrayList = metroGraph.getAdjList();
        Iterator<Node<Station>> iterator = arrayList.iterator();
        HashMap<Node<Station>, DijkstraSP> nodeDijkstraSPHashMap = new HashMap<>();
        while (iterator.hasNext()) {
            Node<Station> nodeNode = iterator.next();
            nodeDijkstraSPHashMap.put(nodeNode, new DijkstraSP(metroGraph, nodeNode));
        }

        for(Edge edge : listEdge){
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
