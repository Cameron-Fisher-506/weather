package za.co.weather.nav;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import za.co.weather.utils.ConstantUtils;

public class LocationFrag extends Fragment implements OnMapReadyCallback
{
    private GoogleMap map;
    private Marker marker;

    private EditText edTxtLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_location, container, false);

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
                    locate();
                }

                return false;
            }
        });

    }

    private void locate()
    {
        String searchString = edTxtLocation.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses = new ArrayList<Address>();

        try {
            addresses = geocoder.getFromLocationName(searchString, 1);
            if(addresses != null && addresses.size() > 0)
            {
                Address address = addresses.get(0);

                if(address != null)
                {
                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 10, address.getAddressLine(0));
                }

            }
        }catch(IOException e)
        {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;
        this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.map_in_night));

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
