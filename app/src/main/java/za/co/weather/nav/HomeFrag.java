package za.co.weather.nav;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import za.co.weather.R;
import za.co.weather.objs.Position;
import za.co.weather.objs.Weather;
import za.co.weather.utils.ConstantUtils;
import za.co.weather.utils.DTUtils;
import za.co.weather.utils.LocationUtils;
import za.co.weather.utils.LocationUtilsCaller;
import za.co.weather.utils.StringUtils;
import za.co.weather.utils.WSCallsUtils;
import za.co.weather.utils.WSCallsUtilsTaskCaller;

public class HomeFrag extends Fragment implements WSCallsUtilsTaskCaller, LocationUtilsCaller
{
    private final int REQ_CODE_GET_WEATHER = 100;
    private final int REQ_CODE_GET_WEATHER_FORECAST = 101;

    private TextView txtTemperature;
    private TextView txtDescription;

    private TextView txtMinTemp;
    private TextView txtCurrentTemp;
    private TextView txtMaxTemp;

    private TextView txtDayOne;
    private ImageView imgDayOneTemp;
    private TextView txtDayOneTemp;

    private TextView txtDayTwo;
    private ImageView imgDayTwoTemp;
    private TextView txtDayTwoTemp;

    private TextView txtDayThree;
    private ImageView imgDayThreeTemp;
    private TextView txtDayThreeTemp;

    private TextView txtDayFour;
    private ImageView imgDayFourTemp;
    private TextView txtDayFourTemp;

    private Position position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        this.position = new Position();

        LocationUtils locationUtilsGPS = new LocationUtils(this, LocationManager.GPS_PROVIDER);

        wireUI(view);

        return view;
    }

    private void wireUI(View view)
    {
        this.txtTemperature = (TextView) view.findViewById(R.id.txtTemperature);
        this.txtDescription = (TextView) view.findViewById(R.id.txtDescription);

        this.txtMinTemp = (TextView) view.findViewById(R.id.txtMinTemp);
        this.txtMaxTemp = (TextView) view.findViewById(R.id.txtMaxTemp);
        this.txtCurrentTemp = (TextView) view.findViewById(R.id.txtCurrentTemp);

        this.txtDayOne = (TextView) view.findViewById(R.id.txtDayOne);
        this.imgDayOneTemp = (ImageView) view.findViewById(R.id.imgDayOneTemp);
        this.txtDayOneTemp = (TextView) view.findViewById(R.id.txtDayOneTemp);

        this.txtDayTwo = (TextView) view.findViewById(R.id.txtDayTwo);
        this.imgDayTwoTemp = (ImageView) view.findViewById(R.id.imgDayTwoTemp);
        this.txtDayTwoTemp = (TextView) view.findViewById(R.id.txtDayTwoTemp);

        this.txtDayThree = (TextView) view.findViewById(R.id.txtDayThree);
        this.imgDayThreeTemp = (ImageView) view.findViewById(R.id.imgDayThreeTemp);
        this.txtDayThreeTemp = (TextView) view.findViewById(R.id.txtDayThreeTemp);

        this.txtDayFour = (TextView) view.findViewById(R.id.txtDayFour);
        this.imgDayFourTemp = (ImageView) view.findViewById(R.id.imgDayFourTemp);
        this.txtDayFourTemp = (TextView) view.findViewById(R.id.txtDayFourTemp);
    }

    private void displayUI(Position position)
    {
        if(position != null)
        {
            if(position.getWeather() != null)
            {
                if(position.getWeather().getTemp() != null)
                {
                    this.txtTemperature.setText(position.getWeather().getTemp() + "" + (char)0x00B0);
                    this.txtCurrentTemp.setText(position.getWeather().getTemp() + "" + (char)0x00B0);
                }else
                {
                    this.txtTemperature.setText("N/A");
                }

                if(position.getWeather().getDescription() != null)
                {
                    this.txtDescription.setText(position.getWeather().getDescription());
                }else
                {
                    this.txtDescription.setText("N/A");
                }

                if(position.getWeather().getMinTemp() != null)
                {
                    this.txtMinTemp.setText(position.getWeather().getMinTemp() + "" + (char)0x00B0);
                }else
                {
                    this.txtMinTemp.setText("N/A");
                }

                if(position.getWeather().getMaxTemp() != null)
                {
                    this.txtMaxTemp.setText(position.getWeather().getMaxTemp() + "" + (char)0x00B0);
                }else
                {
                    this.txtMaxTemp.setText("N/A");
                }

            }

            if(position.getForecast() != null && position.getForecast().size() > 0)
            {
                for(int i = 0; i < position.getForecast().size(); i++)
                {
                    Weather weather = position.getForecast().get(i);
                    String day = DTUtils.getDayFromDate(weather.getDate());

                    if(i == 0)
                    {
                        //day one
                        this.txtDayOne.setText(day);
                        this.txtDayOneTemp.setText(weather.getMaxTemp() + "" + (char)0x00B0);
                    }else if(i == 1)
                    {
                        //day two
                        this.txtDayTwo.setText(day);
                        this.txtDayTwoTemp.setText(weather.getMaxTemp() + "" + (char)0x00B0);
                    }else if(i == 2)
                    {
                        //day three
                        this.txtDayThree.setText(day);
                        this.txtDayThreeTemp.setText(weather.getMaxTemp() + "" + (char)0x00B0);
                    }else if(i == 3)
                    {
                        //day four
                        this.txtDayFour.setText(day);
                        this.txtDayFourTemp.setText(weather.getMaxTemp() + "" + (char)0x00B0);
                    }
                }
            }
        }
    }

    private void getWeatherByLatLng(double lat, double lng)
    {
        WSCallsUtils.get(this, StringUtils.WEATHER_URL + "?lat="+lat+"&lon="+lng+"&units=metric&appid="+ ConstantUtils.WEATHER_API_KEY, REQ_CODE_GET_WEATHER);
        WSCallsUtils.get(this, StringUtils.WEATHER_FORECAST_URL + "?lat="+lat+"&lon="+lng+"&units=metric&appid="+ ConstantUtils.WEATHER_API_KEY, REQ_CODE_GET_WEATHER_FORECAST);
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == REQ_CODE_GET_WEATHER)
            {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    this.position.populate(jsonObject);

                    displayUI(this.position);

                }catch(JSONException e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: HomeFrag - taskCompleted"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_GET_WEATHER_FORECAST)
            {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    this.position.populateForcast(jsonObject);

                    displayUI(this.position);

                }catch(JSONException e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: HomeFrag - taskCompleted"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }
        }else
        {

        }
    }

    @Override
    public void currentLocationUpdate(Location location)
    {
        if(location != null)
        {
            getWeatherByLatLng(location.getLatitude(), location.getLongitude());
        }
    }
}
