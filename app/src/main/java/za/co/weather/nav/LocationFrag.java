package za.co.weather.nav;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import za.co.weather.R;
import za.co.weather.objs.Position;
import za.co.weather.utils.ConstantUtils;
import za.co.weather.utils.DTUtils;
import za.co.weather.utils.DialogUtils;
import za.co.weather.utils.GeneralUtils;
import za.co.weather.utils.SQLiteUtils;

public class LocationFrag extends Fragment implements OnMapReadyCallback
{
    private GoogleMap map;
    private Marker marker;

    private EditText edTxtLocation;
    private ImageButton imgBtnSave;
    private ImageButton imgBtnSearch;

    private Address address;

    private SQLiteUtils sqLiteUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_location, container, false);

        this.sqLiteUtils = new SQLiteUtils(getContext());

        Places.initialize(getContext(), ConstantUtils.GOOGLE_API_KEY);

        wireUI(view);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

    }

    private void wireUI(View view)
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }

        this.edTxtLocation = (EditText) view.findViewById(R.id.edtxtLocation);
        this.edTxtLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    if(map != null)
                    {
                        map.clear();
                    }

                    locate();
                }

                return false;
            }
        });

        this.imgBtnSave = (ImageButton) view.findViewById(R.id.imgBtnSave);
        this.imgBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(address != null)
                {
                    sqLiteUtils.cacheFavourites(address.getAddressLine(0), Double.toString(address.getLatitude()), Double.toString(address.getLongitude()));
                    GeneralUtils.makeToast(getContext(),"Added to favourites");

                    if(map != null)
                    {
                        map.clear();
                        plotFavouriteLocations();
                    }
                }else
                {
                    DialogUtils.createAlertDialog(getContext(), "Add to favorites", "Please search for an address, city or zip code to add to favourites.",false).show();
                }
            }
        });

        this.imgBtnSearch = (ImageButton) view.findViewById(R.id.imgBtnSearch);
        this.imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(map != null)
                {
                    map.clear();
                }

                locate();
            }
        });

    }

    private void plotFavouriteLocations()
    {
        List<Position> positions = sqLiteUtils.getAllFavourites();

        if(positions != null && positions.size() > 0)
        {
            for(int i = 0; i < positions.size(); i++)
            {
                Position position = positions.get(i);

                if(this.map != null && position.getCity() != null && position.getLatitude() != null && position.getLongitude() != null)
                {
                    LatLng latLng = new LatLng(Double.parseDouble(position.getLatitude()),Double.parseDouble(position.getLongitude()));

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(position.getCity());

                    this.marker =  this.map.addMarker(markerOptions);
                    this.marker.showInfoWindow();
                }

            }
        }
    }

    private void locate()
    {
        String searchString = edTxtLocation.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(searchString, 1);
            if(addresses != null && addresses.size() > 0)
            {
                this.address = addresses.get(0);

                if(address != null)
                {
                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 10, address.getAddressLine(0));
                }

            }
        }catch(IOException e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: LocationFrag - locate"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;
        if(this.map != null)
        {
            this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.map_in_night));

            this.map.clear();
            plotFavouriteLocations();
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title)
    {
        if(this.map != null && latLng != null)
        {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to location user
                    .zoom(zoom)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            this.marker =  this.map.addMarker(markerOptions);
            this.marker.showInfoWindow();


        }

    }


}
