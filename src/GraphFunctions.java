import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GraphFunctions {
  public static void main(String[] args) {

  }

  public static HashMap<Node, Integer> countNumberOfNodes(String file, String splitDelimiter) throws java.io.IOException {
    List<String> lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
    String line;
    String[] edge;
    Node node;
    Iterator<String> lineIterator = lines.iterator();
    HashMap<Node, Integer> mappingOfVertices = new HashMap<>();
    int j =1;
    while (lineIterator.hasNext()) {
      line = lineIterator.next();
      line = line.replaceAll("\" ","-");
      line = line.replaceAll("\"","");
      edge = line.split(splitDelimiter);
      for (int i = 0; i < 2; i++) {
        node = new Node(edge[i]);
        if (!containsNode(mappingOfVertices, node)) {
          j++;
          mappingOfVertices.put(node, mappingOfVertices.size());
        }
      }
    }
    return mappingOfVertices;
  }

  public static boolean containsNode(HashMap<Node, Integer> mappingOfVertices, Node node) {
    Iterator<Node> iterator = mappingOfVertices.keySet().iterator();
    Node nodeIterator;
    while (iterator.hasNext()) {
      nodeIterator = iterator.next();
      if (nodeIterator.getNodeName().equals(node.getNodeName())) {
        return true;
      }
    }
    return false;
  }
}
