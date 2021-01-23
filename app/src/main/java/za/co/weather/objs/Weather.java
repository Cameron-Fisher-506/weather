package za.co.weather.objs;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.weather.utils.ConstantUtils;
import za.co.weather.utils.DTUtils;
import za.co.weather.utils.LocationUtils;

public class Weather
{
    public static final String CLOUDS = "Clouds";
    public static final String RAIN = "Rain";
    public static final String CLEAR = "Clear";

    private String main;
    private String description;
    private String sunrise;
    private String sunset;
    private Long temp;
    private Long minTemp;
    private Long maxTemp;
    private Long pressure;
    private Long humidity;
    private Long feelsLike;
    private Double visibility;
    private Double windSpeed;
    private Long windDegrees;
    private String date;
    private String timestamp;

    public Weather()
    {

    }

    public Weather(String main, String description, String sunrise, String sunset, Long temp, Long minTemp, Long maxTemp, Long pressure, Long humidity, Long feelsLike, Double visibility, Double windSpeed, Long windDegrees, String date, String timestamp) {
        this.main = main;
        this.description = description;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.feelsLike = feelsLike;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDegrees = windDegrees;
        this.date = date;
        this.timestamp = timestamp;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Long getTemp() {
        return temp;
    }

    public void setTemp(Long temp) {
        this.temp = temp;
    }

    public Long getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Long minTemp) {
        this.minTemp = minTemp;
    }

    public Long getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Long maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Long getPressure() {
        return pressure;
    }

    public void setPressure(Long pressure) {
        this.pressure = pressure;
    }

    public Long getHumidity() {
        return humidity;
    }

    public void setHumidity(Long humidity) {
        this.humidity = humidity;
    }

    public Long getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Long feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Long getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(Long windDegrees) {
        this.windDegrees = windDegrees;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void populate(JSONObject jsonObject)
    {
        try
        {
            if(jsonObject.has("weather"))
            {
                JSONArray weathers = jsonObject.getJSONArray("weather");
                if(weathers != null && weathers.length() > 0)
                {
                    JSONObject weather = weathers.getJSONObject(0);
                    this.main = weather.has("main") ? weather.getString("main") : null;
                    this.description = weather.has("description") ? weather.getString("description") : null;
                }

            }

            if(jsonObject.has("sys"))
            {
                JSONObject sys = jsonObject.getJSONObject("sys");
                this.sunrise = sys.has("sunrise") ? DTUtils.getTimeFromDecimal(sys.getLong("sunrise")) : null;
                this.sunset = sys.has("sunset") ? DTUtils.getTimeFromDecimal(sys.getLong("sunset")) : null;
            }

            if(jsonObject.has("main"))
            {
                JSONObject main = jsonObject.getJSONObject("main");
                this.temp = main.has("temp") ? Math.round(main.getDouble("temp")) : null;
                this.minTemp = main.has("temp_min") ? Math.round(main.getDouble("temp_min")) : null;
                this.maxTemp = main.has("temp_max") ? Math.round((main.getDouble("temp_max"))) : null;
                this.feelsLike = main.has("feels_like") ? Math.round(main.getDouble("feels_like")) : null;
                this.pressure = main.has("pressure") ? main.getLong("pressure") : null;
                this.humidity = main.has("humidity") ? main.getLong("humidity") : null;

            }

            this.visibility = jsonObject.has("visibility") ? LocationUtils.meterToKilometer(jsonObject.getLong("visibility")) : null;
            this.date = jsonObject.has("dt") ? DTUtils.getDateFromDecimal(jsonObject.getLong("dt")) : null;
            this.timestamp = jsonObject.has("dt") ? DTUtils.getDateTimeFromDecimal(jsonObject.getLong("dt")) : null;

            if(jsonObject.has("wind"))
            {
                JSONObject wind = jsonObject.getJSONObject("wind");
                this.windSpeed = wind.has("speed") ? wind.getDouble("speed") : null;
                this.windDegrees = wind.has("deg") ? wind.getLong("deg") : null;
            }
        }catch(JSONException e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: Weather - populate"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }
}
