package com.newtglobal.eFmFmFleet.eFmFmFleet;

import java.util.ArrayList;
import java.util.List;

public class PolygonalGeofence extends Geofence
{
  private Geocode centre;
  private List<Geocode> vertices;

  public PolygonalGeofence()
  {
    super();
    this.centre = null;
    vertices = new ArrayList<Geocode>();
  }

  public PolygonalGeofence addVertex(Geocode vertex)
  {
    if (vertices.size() == 2) {
      double lat = 0, longi = 0;
      for (Geocode G : vertices) {
        lat += G.getLat();
        longi += G.getLong();
      }
      lat += vertex.getLat();
      longi += vertex.getLong();
      this.centre = new Geocode(lat/3, longi/3);
    }
    this.vertices.add(vertex);
    return this;
  }

  private double subtendedArea(Geocode G) throws Exception
  {
    double area = 0;
    int n = vertices.size();
    if (n < 3) throw new Exception("Polygonal geofence should" +
        " contain at least three points");
    //distance as a decameter
    for(int i = 0; i < n; i++) {
      Geocode V1 = vertices.get(i % n);
      Geocode V2 = vertices.get((i+1) % n);
      double a = Geocode.distance(G, V1)/10;
      double b = Geocode.distance(G, V2)/10;
      double c = Geocode.distance(V1, V2)/10;
      double s = (a+b+c)/2;
      area += Math.sqrt(s*(s-a)*(s-b)*(s-c));
    }
    return area;
  }

  @Override
  public boolean isInGeofence()
  {
    boolean result = false;
    try {
      double areaRatio = subtendedArea(vehicleLocation)/subtendedArea(centre);
      result = (areaRatio > 0.98 && areaRatio < 1.02);

    }
    catch(Exception e) {
    }
    return result;
  }
}
