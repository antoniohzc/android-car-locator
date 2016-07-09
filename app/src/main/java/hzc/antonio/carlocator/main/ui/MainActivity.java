package hzc.antonio.carlocator.main.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.locationmap.ui.LocationMapFragment;
import hzc.antonio.carlocator.locationslist.ui.LocationsListFragment;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.login.ui.LoginActivity;
import hzc.antonio.carlocator.main.MainPresenter;
import hzc.antonio.carlocator.main.ui.adapters.MainSectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements MainView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.lblUser) TextView lblUser;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Inject
    MainPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    MainSectionsPagerAdapter adapter;

    private CarLocatorApp app;
    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private boolean resolvingError = false;
    private boolean requestingLocationUpdates = false;
    private static final int RESOLVE_LOCATION_ERROR_REQUEST_CODE = 0;
    private static final int PERMISSIONS_LOCATION_REQUEST_CODE = 1;


    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        app = (CarLocatorApp) getApplication();
        setupInjection();
        setupNavigation();
        setupGoogleApiClient();

        presenter.onCreate();
    }

    private void setupInjection() {
        String[] titles = new String[]{getString(R.string.main_tab_title_map), getString(R.string.main_tab_title_list)};
        Fragment[] fragments = new Fragment[]{new LocationMapFragment(), new LocationsListFragment()};

        app.getMainComponent(this, titles, fragments, getSupportFragmentManager()).inject(this);

    }

    private void setupNavigation() {
        String email = sharedPreferences.getString(app.getShPrefEmailKey(), getString(R.string.main_appbar_user));
        lblUser.setText(email);
        setSupportActionBar(toolbar);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && !requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_LOCATION_ERROR_REQUEST_CODE) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
    }
    //endregion


    public Location getLastKnownLocation() {
        return this.lastKnownLocation;
    }


    //region Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        presenter.logout();
        sharedPreferences.edit().clear().commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    //endregion


    //region GoogleApiClient callbacks
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_LOCATION_REQUEST_CODE);
            }
            return;
        }
        setLastKnownLocation();
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLastKnownLocation();
            }
        }
    }

    private void setLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable()) {
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        else {
            showSnackBar(R.string.main_error_location_notavailable);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (resolvingError) {
            return;
        }
        else if (connectionResult.hasResolution()) {
            resolvingError = true;
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_LOCATION_ERROR_REQUEST_CODE);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            resolvingError = true;
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), RESOLVE_LOCATION_ERROR_REQUEST_CODE).show();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        requestingLocationUpdates = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            requestingLocationUpdates = false;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
    }
    //endregion


    //region View
    private void showSnackBar(String msg) {
        Snackbar.make(viewPager, msg, Snackbar.LENGTH_SHORT).show();

    }

    private void showSnackBar(int strResource) {
        Snackbar.make(viewPager, strResource, Snackbar.LENGTH_SHORT).show();
    }
    //endregion
}
