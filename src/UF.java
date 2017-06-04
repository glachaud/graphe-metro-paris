import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by guillaumelachaud on 5/17/17.
 */
public class UF<T> {
  private Map<Node<T>,Node<T>> id;
  private int count;

  public UF(GraphList G) {
    count = 0;
    id = new HashMap<Node<T>,Node<T>>();
    Iterator<Node<T>> nodeIterator = G.getAdjList().iterator();
    while(nodeIterator.hasNext()){
      Node node = nodeIterator.next();
      id.put(node, node);
      count++;
    }
  }
  public int count() {
    return count;
  }

  public boolean connected(Node p, Node q) {
    return find(p) == find(q);
  }

  public Node find(Node p) {
    while(p != id.get(p)){
      p = id.get(p);
    }
    return p;
  }

  public void union(Node p, Node q) {
    Node pRoot = find(p);
    Node qRoot = find(q);
    if (pRoot.equals(qRoot)){
      return;
    }
    id.put(pRoot, qRoot);
    count--;
  }
}
