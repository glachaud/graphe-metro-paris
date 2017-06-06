import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by guillaumelachaud on 5/24/17.
 */
public class Station {

  Integer id;

  String nodeName;

  List<String> metros;

  Double lat;

  Double lng;

  Map<String, List<String>> lineNeighbors;

  public Station(Integer id, String nodeName, List<String> metros, Double lat, Double lng) {
    this.id = id;
    this.nodeName = nodeName;
    this.metros = metros;
    this.lat = lat;
    this.lng= lng;
    this.lineNeighbors = new HashMap<>();
    Iterator<String> lines = metros.iterator();
    List<String> newLineNeigbors = new ArrayList<>();
    while(lines.hasNext()) {
      String line = lines.next();
      lineNeighbors.put(line, newLineNeigbors);
    }
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(String nodeName) {
    this.nodeName = nodeName;
  }

  public List getMetros() {
    return metros;
  }

  public void setMetros(List metros) {
    this.metros = metros;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public static void main(String[] args) {

  }

  public void addLineNeighbor(String line, Node<Station> u) {
    List<String> lines = new ArrayList<>();
    if(!this.lineNeighbors.get(line).isEmpty()) {
      lines = this.lineNeighbors.get(line);
      lines.add(u.getNodeName());
    } else {
      lines.add(u.getNodeName());
    }
    this.lineNeighbors.put(line, lines);
  }


  public List<String> getLink(Station u) {
    List<String> links = new ArrayList<>();
    Iterator<String> iterator = this.metros.iterator();
    while(iterator.hasNext()) {
      String line = iterator.next();
      if(u.getMetros().contains(line) && this.lineNeighbors.get(line).contains(u.getNodeName())) {
        links.add(line);
      }
    }
    return links;
  }


  public Map<String, List<String>> getLineNeighbors() {
    return lineNeighbors;
  }

  @Override
  public String toString() {
    return nodeName;
  }
}