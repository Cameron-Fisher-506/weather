package za.co.weather.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationUtils {

    private static final String TAG = "LocationUtils";

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Context context;

    private LocationUtilsCaller locationUtilsCaller;

    public LocationUtils(LocationUtilsCaller locationUtilsCaller, String provider)
    {
        this.context = locationUtilsCaller.getContext();
        this.locationUtilsCaller = locationUtilsCaller;

        startLocationServices(provider);
    }

    public void startLocationServices(String provider)
    {
        if(locationManager == null)
        {
            initializeLocationServices();

            startLocationManager(provider);

        }
    }

    private void initializeLocationServices() {

        if(this.context != null)
        {
            if (locationManager == null) {
                locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
            }

            if (locationListener == null)
            {
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                       locationUtilsCaller.currentLocationUpdate(location);
                    }

                    public void onProviderDisabled(String provider) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                };
            }
        }

    }

    private void startLocationManager(String provider)
    {
        try
        {
            locationManager.requestLocationUpdates(provider, 1, 5, locationListener);
        } catch (SecurityException e)
        {
            Log.e(TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - startLocationManager" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    public static String getAddress(Context context, LatLng latLng) {
        String toReturn = null;

        try
        {
            if(context != null && latLng != null)
            {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());

                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                toReturn  = addresses.get(0).getAddressLine(0);
            }
        }catch(Exception e)
        {
            Log.e(TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - getAddress" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static String getCity(Context context, LatLng latLng)
    {
        String toReturn = null;

        try
        {
            if(context != null && latLng != null)
            {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());

                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                toReturn = addresses.get(0).getLocality();

            }
        }catch(Exception e)
        {
            Log.e(TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - getAddress" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static Double meterToKilometer(Long meters)
    {
        Double toReturn = null;

        if(meters != null)
        {
            toReturn = meters/1000.0;
        }

        return toReturn;
    }

}

