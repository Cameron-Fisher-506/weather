package za.co.weather.objs;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.weather.utils.ConstantUtils;
import za.co.weather.utils.DTUtils;

public class Position
{
    private String latitude;
    private String longitude;
    private String city;
    private Weather weather;
    private List<Weather> forecast;

    public Position()
    {
        this.weather = new Weather();
        this.forecast = new ArrayList<Weather>();
    }

    public Position(String latitude, String longitude, String city, Weather weather) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.weather = weather;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public List<Weather> getForecast() {
        return forecast;
    }

    public void setForecast(List<Weather> forecast) {
        this.forecast = forecast;
    }

    public void populate(JSONObject jsonObject)
    {
        try
        {
            if(jsonObject.has("coord"))
            {
                JSONObject coord = jsonObject.getJSONObject("coord");
                this.latitude = (coord.has("lat") ? Double.toString(coord.getDouble("lat")) : null);
                this.longitude = (coord.has("lon") ? Double.toString(coord.getDouble("lon")) : null);
            }

            this.city = (jsonObject.has("name") ? jsonObject.getString("name") : null);
            this.weather.populate(jsonObject);
        }catch(JSONException e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: Position - populate"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    public void populateForcast(JSONObject jsonObject)
    {
        try
        {
            if(jsonObject.has("list"))
            {
                JSONArray list = jsonObject.getJSONArray("list");
                if(list != null && list.length() > 0)
                {
                    for(int i = 0; i < ConstantUtils.FORECAST_DAYS; i++)
                    {
                        String forecastDate = DTUtils.getForecastDate(i+1);
                        for(int j = 0; j < list.length(); j++)
                        {
                            JSONObject listItem = list.getJSONObject(j);
                            if(listItem.has("dt_txt"))
                            {
                                String dt = listItem.getString("dt_txt");
                                if(dt.equals(forecastDate + " " + "00:00:00"))
                                {
                                    Weather weather = new Weather();
                                    weather.populate(listItem);

                                    this.forecast.add(weather);
                                }
                            }
                        }
                    }

                }
            }
        }catch(JSONException e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: Position - populate"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }
}
