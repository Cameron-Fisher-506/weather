package za.co.weather.nav;

import android.Manifest;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;
import za.co.weather.R;
import za.co.weather.dialogs.PermissionCallback;
import za.co.weather.objs.Position;
import za.co.weather.objs.Weather;
import za.co.weather.utils.ConstantUtils;
import za.co.weather.utils.DTUtils;
import za.co.weather.utils.DialogUtils;
import za.co.weather.utils.GeneralUtils;
import za.co.weather.utils.LocationUtils;
import za.co.weather.utils.LocationUtilsCaller;
import za.co.weather.utils.StringUtils;
import za.co.weather.utils.WSCallsUtils;
import za.co.weather.utils.WSCallsUtilsTaskCaller;

public class HomeFrag extends Fragment implements WSCallsUtilsTaskCaller, LocationUtilsCaller
{
    private final int REQ_CODE_GET_WEATHER = 100;
    private final int REQ_CODE_GET_WEATHER_FORECAST = 101;

    private ConstraintLayout homeFragLayout;

    private ImageView imgWeather;

    private TextView txtCity;
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

    private TextView txtDayFive;
    private ImageView imgDayFiveTemp;
    private TextView txtDayFiveTemp;

    private TextView txtNotify;

    private Position position;

    private LocationUtils locationUtilsGPS;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        this.position = new Position();
        wireUI(view);

        if(!GeneralUtils.checkPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION))
        {
            DialogUtils.createAlertPermission(getContext(), "Location Permission", "Do you want to enable location permission for Weather?", true, new PermissionCallback() {
                @Override
                public void checkPermission(boolean ischeckPermission) {
                    if(ischeckPermission)
                    {
                        GeneralUtils.openAppSettingsScreen(getContext());
                    }
                }
            }).show();
        }

        if(getArguments() != null)
        {
            String strPosition = getArguments().getString("position");
            if(strPosition != null)
            {
                try
                {
                    JSONObject jsonObjectMovie = new JSONObject(strPosition);
                    String city = jsonObjectMovie.getString("city");
                    String latitude = jsonObjectMovie.getString("latitude");
                    String longitude = jsonObjectMovie.getString("longitude");

                    getWeatherByLatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                }catch (Exception e)
                {
                    String message = "\n\nError Message: " + e.getMessage() +
                            "\nMethod: onCreateView" +
                            "\nClass: HomeActivity" +
                            "\nCreatedTime: " + DTUtils.getCurrentDateTime();
                    Log.d(ConstantUtils.TAG, message);
                }
            }else
            {
                this.position = new Position();
                this.locationUtilsGPS = new LocationUtils(this, LocationManager.GPS_PROVIDER);
            }
        }else
        {
            this.locationUtilsGPS = new LocationUtils(this, LocationManager.GPS_PROVIDER);
        }


        return view;
    }


    private void wireUI(View view)
    {
        this.homeFragLayout = (ConstraintLayout) view.findViewById(R.id.homeFrag);

        this.txtNotify = (TextView) view.findViewById(R.id.txtNotify);
        this.txtNotify.setVisibility(View.GONE);

        this.imgWeather = (ImageView) view.findViewById(R.id.imgWeather);

        this.txtCity = (TextView) view.findViewById(R.id.txtCity);
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

        this.txtDayFive = (TextView) view.findViewById(R.id.txtDayFive);
        this.imgDayFiveTemp = (ImageView) view.findViewById(R.id.imgDayFiveTemp);
        this.txtDayFiveTemp = (TextView) view.findViewById(R.id.txtDayFiveTemp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(this.locationUtilsGPS != null)
        {
            this.locationUtilsGPS.stopLocationUpdates();
        }
    }

    private void displayUI(Position position)
    {
        if(position != null)
        {

            if(position.getCity() != null)
            {
                this.txtCity.setText(position.getCity());
            }else
            {
                this.txtCity.setText("--");
            }
            if(position.getWeather() != null)
            {
                if(position.getWeather().getTemp() != null)
                {
                    this.txtTemperature.setText(position.getWeather().getTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                    this.txtCurrentTemp.setText(position.getWeather().getTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                }else
                {
                    this.txtTemperature.setText("--");
                }

                if(position.getWeather().getMain() != null)
                {
                    if(getContext() != null)
                    {
                        if(position.getWeather().getMain().equals(Weather.CLEAR))
                        {
                            this.imgWeather.setImageResource(R.drawable.forest_sunny);
                            this.homeFragLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sunny));
                        }else if(position.getWeather().getMain().equals(Weather.RAIN))
                        {
                            this.imgWeather.setImageResource(R.drawable.forest_rainy);
                            this.homeFragLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.rainy));
                        }else if(position.getWeather().getMain().equals(Weather.CLOUDS))
                        {
                            this.imgWeather.setImageResource(R.drawable.forest_cloudy);
                            this.homeFragLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cloudy));
                        }
                    }
                }

                if(position.getWeather().getDescription() != null)
                {
                    this.txtDescription.setText(position.getWeather().getDescription());
                }else
                {
                    this.txtDescription.setText("--");
                }

                if(position.getWeather().getMinTemp() != null)
                {
                    this.txtMinTemp.setText(position.getWeather().getMinTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                }else
                {
                    this.txtMinTemp.setText("--");
                }

                if(position.getWeather().getMaxTemp() != null)
                {
                    this.txtMaxTemp.setText(position.getWeather().getMaxTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                }else
                {
                    this.txtMaxTemp.setText("--");
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
                        setWeatherImage(weather.getMain(), this.imgDayOneTemp);
                        this.txtDayOneTemp.setText(weather.getMaxTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                    }else if(i == 1)
                    {
                        //day two
                        this.txtDayTwo.setText(day);
                        setWeatherImage(weather.getMain(), this.imgDayTwoTemp);
                        this.txtDayTwoTemp.setText(weather.getMaxTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                    }else if(i == 2)
                    {
                        //day three
                        this.txtDayThree.setText(day);
                        setWeatherImage(weather.getMain(), this.imgDayThreeTemp);
                        this.txtDayThreeTemp.setText(weather.getMaxTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                    }else if(i == 3)
                    {
                        //day four
                        this.txtDayFour.setText(day);
                        setWeatherImage(weather.getMain(), this.imgDayFourTemp);
                        this.txtDayFourTemp.setText(weather.getMaxTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                    }else if(i == 4)
                    {
                        //day five
                        this.txtDayFive.setText(day);
                        setWeatherImage(weather.getMain(), this.imgDayFiveTemp);
                        this.txtDayFiveTemp.setText(weather.getMaxTemp() + "" + ConstantUtils.DEGREES_SYMBOL);
                    }
                }
            }
        }
    }

    private void setWeatherImage(String weather, ImageView view)
    {
        if(weather != null && view != null)
        {
            if(weather.equals(Weather.CLEAR))
            {
                view.setImageResource(R.drawable.clear);
            }else if(weather.equals(Weather.RAIN))
            {
                view.setImageResource(R.drawable.rain);
            }else if(weather.equals(Weather.CLOUDS))
            {
                view.setImageResource(R.drawable.partlysunny);
            }
        }
    }

    private void getWeatherByLatLng(double lat, double lng)
    {
        WSCallsUtils.get(this, StringUtils.WEATHER_URL + "?lat="+lat+"&lon="+lng+"&units=metric&appid="+ ConstantUtils.WEATHER_API_KEY, REQ_CODE_GET_WEATHER);
        WSCallsUtils.get(this, StringUtils.WEATHER_FORECAST_URL + "?lat="+lat+"&lon="+lng+"&units=metric&appid="+ ConstantUtils.WEATHER_API_KEY, REQ_CODE_GET_WEATHER_FORECAST);
    }

    @Override
    public Context getCallingContext() {
        return getContext();
    }

    @Override
    public void taskCompleted(String response, int reqCode, boolean isOffline)
    {
        if(isOffline)
        {
            this.txtNotify.setText("You are currently in cache mode.");
            this.txtNotify.setVisibility(View.VISIBLE);
        }else
        {
            this.txtNotify.setVisibility(View.GONE);
        }

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
