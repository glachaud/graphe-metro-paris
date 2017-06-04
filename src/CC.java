import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by guillaumelachaud on 4/25/17.
 */
public class CC<T> {
  private Map<Node, Boolean> marked;
  private Map<Node, Integer> id;
  private int count;

  public CC(GraphList G) {
    count = 0;
    marked = new HashMap<>();
    id = new HashMap<>();
    ArrayList<Node<T>> adj = G.getAdjList();
    Iterator<Node<T>> iterator = adj.iterator();
    while (iterator.hasNext()) {
      Node<T> nodeIteration = iterator.next();
      if (marked.isEmpty()) {
        dfs(G, nodeIteration);
        count++;
      }
      else if(!marked.containsKey(nodeIteration)){
        dfs(G, nodeIteration);
        count++;
      }
    }
  }

  private void dfs(GraphList G, Node v) {
    marked.put(v, true);
    id.put(v, count);
    Iterator<Node<T>> iterator = v.getListNeighbors().iterator();
    while (iterator.hasNext()) {
      Node nodeIteration = iterator.next();
      if (!marked.containsKey(nodeIteration)) {
        dfs(G, nodeIteration);
      }
    }
  }

  public boolean isConnected(Node v, Node w) {
    return id.get(v) == id.get(w);
  }

  public Map<Node, Integer> getId(){
    return id;
  }
  public int getCount() {
    return count;
  }


  public static void main(String[] args) throws java.io.IOException{
    GraphList graph = new GraphList("src/graph-DFS-BFS.txt", " ");
    CC graphComponent = new CC(graph);
    System.out.println(graphComponent.getCount());
  }
}
