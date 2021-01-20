package za.co.weather.utils;

import android.content.Context;
import android.location.Location;

public interface LocationUtilsCaller
{
    public void currentLocationUpdate(Location location);
    public Context getContext();
}
