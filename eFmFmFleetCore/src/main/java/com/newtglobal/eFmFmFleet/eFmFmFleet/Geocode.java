package com.newtglobal.eFmFmFleet.eFmFmFleet;

public class Geocode
{
  private double latitude;
  private double longitude;

  public Geocode(double latitude, double longitude)
  {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Geocode(String geocode)
  {
    String [] latlong = geocode.split(",");
    this.latitude = Double.parseDouble(latlong[0].trim());
    this.longitude = Double.parseDouble(latlong[1].trim());
  }

  public double getLat()
  {
    return this.latitude;
  }

  public double getLong()
  {
    return this.longitude;
  }

  public static double distance(Geocode G1, Geocode G2)
  {
    double dLat = toRadians(Math.abs(G1.latitude - G2.latitude));
    double dLong = toRadians(Math.abs(G1.longitude - G2.longitude));
    double a = Math.pow(Math.sin(dLat/2),2) + Math.cos(toRadians(G1.latitude))
        *Math.cos(toRadians(G2.latitude))*Math.pow(Math.sin(dLong/2),2);
    double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    double radius = 6371000;
    return radius*c;
  }

  public static double toRadians(double degrees)
  {
    return degrees*Math.PI/180;
  }
}
