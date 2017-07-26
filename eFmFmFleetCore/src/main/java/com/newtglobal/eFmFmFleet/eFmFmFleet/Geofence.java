package com.newtglobal.eFmFmFleet.eFmFmFleet;

public abstract class Geofence
{
  protected boolean state;
  protected Geocode vehicleLocation;

  public abstract boolean isInGeofence();

  public Geofence()
  {
    this.state = true;
    this.vehicleLocation = null;
  }

  public boolean getState()
  {
    return state;
  }

  public void setVehicleLocation(Geocode location)
  {
    this.vehicleLocation = location;
  }
}
