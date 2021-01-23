package za.co.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

import za.co.weather.dialogs.PermissionCallback;
import za.co.weather.nav.HomeFrag;
import za.co.weather.nav.LocationFrag;
import za.co.weather.nav.MenuFrag;
import za.co.weather.policies.PrivacyPolicyFrag;
import za.co.weather.utils.ConstantUtils;
import za.co.weather.utils.DTUtils;
import za.co.weather.utils.DialogUtils;
import za.co.weather.utils.FragmentUtils;
import za.co.weather.utils.GeneralUtils;
import za.co.weather.utils.SharedPreferencesUtils;
import za.co.weather.utils.WSCallsUtilsTaskCaller;


public class MainActivity extends AppCompatActivity implements WSCallsUtilsTaskCaller
{
    private final String TAG = "MainActivity";

    private ImageButton btnHome;
    private ImageButton btnAddFavourites;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUI();

        displayPrivacyPolicy();

    }

    private void displayPrivacyPolicy()
    {
        try
        {
            JSONObject jsonObject = SharedPreferencesUtils.get(this, SharedPreferencesUtils.PRIVACY_POLICY_ACCEPTANCE);
            if(jsonObject == null)
            {
                setNavIcons(false, false, false);

                PrivacyPolicyFrag privacyPolicyFrag = new PrivacyPolicyFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), privacyPolicyFrag, R.id.fragContainer, getSupportActionBar(), "Privacy Policy", true, false, true, null);
            }else
            {
                setNavIcons(true, false, false);

                HomeFrag homeFrag = new HomeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);
            }


        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: MainActivity - displayPrivacyPolicy"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.permissions:
            {
                DialogUtils.createAlertPermission(this, "Location Permission", "Do you want to enable location permission for Weather?", true, new PermissionCallback() {
                    @Override
                    public void checkPermission(boolean ischeckPermission) {
                        if(ischeckPermission)
                        {
                            GeneralUtils.openAppSettingsScreen(getApplicationContext());
                        }
                    }
                }).show();

                break;
            }
            default:
            {
                //unknown
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void wireUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        addBtnHomeListener();
        addBtnLocationsListener();
        addBtnMenuListener();

    }

    private void addBtnHomeListener()
    {
        this.btnHome = (ImageButton) findViewById(R.id.btnHome);
        this.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(true, false, false);

                HomeFrag homeFrag = new HomeFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), homeFrag, R.id.fragContainer, getSupportActionBar(), "Home", true, false, true, null);
            }
        });
    }

    private void addBtnMenuListener()
    {
        this.btnMenu = (ImageButton)findViewById(R.id.btnMenu);
        this.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, false, true);

                MenuFrag menuFrag = new MenuFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), menuFrag, R.id.fragContainer, getSupportActionBar(), "Menu", true, false, true, null);
            }
        });
    }

    private void addBtnLocationsListener()
    {
        this.btnAddFavourites = (ImageButton) findViewById(R.id.btnAddFavourites);
        this.btnAddFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavIcons(false, true, false);

                LocationFrag locationFrag = new LocationFrag();
                FragmentUtils.startFragment(getSupportFragmentManager(), locationFrag, R.id.fragContainer, getSupportActionBar(), "Add to favourites", true, false, true, null);
            }
        });
    }


    public void setNavIcons(boolean home, boolean locations, boolean menu)
    {
        if(home)
        {
            this.btnHome.setBackgroundResource(R.drawable.ic_home_blue_24dp);
        }else
        {
            this.btnHome.setBackgroundResource(R.drawable.ic_home_white_24dp);
        }

        if(locations)
        {
            this.btnAddFavourites.setBackgroundResource(R.drawable.ic_favorite_blue_24dp);
        }else
        {
            this.btnAddFavourites.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
        }

        if(menu)
        {
            this.btnMenu.setBackgroundResource(R.drawable.ic_menu_blue_24dp);
        }else
        {
            this.btnMenu.setBackgroundResource(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getCallingContext() {
        return this;
    }

    @Override
    public void taskCompleted(String response, int reqCode, boolean isOffline) {
        if(response != null)
        {

        }
    }
}
