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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.login.ui.LoginActivity;
import hzc.antonio.carlocator.main.MainPresenter;
import hzc.antonio.carlocator.main.ui.adapters.MainSectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements MainView,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.lblUser) TextView lblUser;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    MainPresenter presenter;
    SharedPreferences sharedPreferences;
    MainSectionsPagerAdapter adapter;

    private CarLocatorApp app;
    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private boolean resolvingError = false;
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

    }

    private void setupNavigation() {
        String email = sharedPreferences.getString(app.getSharedPrefEmailKey(), getString(R.string.main_appbar_user));
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
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
    //endregion

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
            Snackbar.make(viewPager, lastKnownLocation.toString(), Snackbar.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(viewPager, R.string.main_error_location_notavailable, Snackbar.LENGTH_SHORT).show();
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


    //region View
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
}
