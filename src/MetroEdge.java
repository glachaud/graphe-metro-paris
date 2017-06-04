/**
 * Created by guillaumelachaud on 5/31/17.
 */
public class MetroEdge<T> {
  Edge<T> edge;
  String ligne;

  public Edge<T> getEdge() {
    return edge;
  }

  public void setEdge(Edge<T> edge) {
    this.edge = edge;
  }

  public String getLigne() {
    return ligne;
  }

  public void setLigne(String ligne) {
    this.ligne = ligne;
  }
}
