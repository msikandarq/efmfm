package com.newtglobal.eFmFmFleet.eFmFmFleet;

public class RadialGeofence extends Geofence
{
  Geocode centre;
  double radius;

  public RadialGeofence(Geocode centre, double radius)
  {
    super();
    this.centre = centre;
    this.radius = radius;
  }

  @Override
  public boolean isInGeofence()
  {
    return (Geocode.distance(centre, vehicleLocation) < radius);
  }
}
