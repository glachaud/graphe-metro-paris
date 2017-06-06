import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<Edge<Station>> {
  Map<Edge<Station>, Double> base;

  public ValueComparator(Map<Edge<Station>, Double> base) {
    this.base = base;
  }

  // Note: this comparator imposes orderings that are inconsistent with
  // equals.
  public int compare(Edge<Station> u, Edge<Station> v) {
    if (base.get(u) > base.get(v)) {
      return -1;
    } else if (base.get(u) < base.get(v)) {
      return 1;
    } else if (base.get(u) == base.get(v)) {
      if (u.getTail().equals(v.getTail()) && u.getHead().equals(v.getHead())) {
        return 0;
      }
    }
    return -1;
  }
}