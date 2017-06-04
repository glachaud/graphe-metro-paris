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
public class MetroNode {

  Integer id;

  String nodeName;

  List metros;

  Double lat;

  Double lng;

  public MetroNode(Integer id, String nodeName, List metros, Double lat, Double lng) {
    this.id = id;
    this.nodeName = nodeName;
    this.metros = metros;
    this.lat = lat;
    this.lng= lng;
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

  @Override
  public String toString() {
    return nodeName;
  }
}