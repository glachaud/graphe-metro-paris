import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<Edge> {
  Map<Edge, Double> base;

  public ValueComparator(Map<Edge, Double> base) {
    this.base = base;
  }

  // Note: this comparator imposes orderings that are inconsistent with
  // equals.
  public int compare(Edge u, Edge v) {
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