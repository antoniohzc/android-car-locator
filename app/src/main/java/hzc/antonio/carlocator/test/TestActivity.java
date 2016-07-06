package hzc.antonio.carlocator.test;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.entities.CarLocation;

public class TestActivity extends AppCompatActivity
        implements
        // Activity
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        // Fragment
        OnMapReadyCallback {

    @BindView(R.id.container) RelativeLayout container;

    // Activity
    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private boolean resolvingError = false;
    private static final int RESOLVE_LOCATION_ERROR_REQUEST_CODE = 0;
    private static final int PERMISSIONS_LOCATION_REQUEST_CODE = 1;
    private static final int PERMISSIONS_LOCATION_REQUEST_CODE_2 = 2;

    // Fragment
    private GoogleMap map;
    // private Marker marker;
    private CarLocation carLocation;
    // private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        setupGoogleApiClient();

        // Fragment - onActivityCreated
        // FragmentManager fm = getChildFragmentManager();
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        super.onDestroy();
    }


    //region lastKnownLocation - ConnectionCallbacks / Listeners
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
        getLastKnownLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Activity
        if (requestCode == PERMISSIONS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            }
        }
        // Fragment
        else if (requestCode == PERMISSIONS_LOCATION_REQUEST_CODE_2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
                map.setMyLocationEnabled(true);
            }
        }
    }

    private void getLastKnownLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        if (LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable()) {
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            // Snackbar.make(container, lastKnownLocation.toString(), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(container, R.string.test_error_location_notavailable, Snackbar.LENGTH_SHORT).show();
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
        } else if (connectionResult.hasResolution()) {
            resolvingError = true;
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_LOCATION_ERROR_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
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


    // Fragment
    public void showCarLocation(CarLocation carLocation) {
        // viene de EventBus - Firebase
        carLocation.setLatitude(37.26265659);
        carLocation.setLongitude(-6.92656875);
        // // // // //

        this.carLocation = carLocation;

        LatLng location = new LatLng(carLocation.getLatitude(), carLocation.getLongitude());
        map.clear();
        map.addMarker(new MarkerOptions().position(location));

        animateCamera(location);
    }

    public void updateCarLocation(LatLng location) {
        // String text = location.latitude + " - " + location.longitude;
        // Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();

        carLocation.setLatitude(location.latitude);
        carLocation.setLongitude(location.longitude);

        map.clear();
        map.addMarker(new MarkerOptions().position(location));

        animateCamera(location);

        // Quitar lo de arriba:
        // presenter.updateCarLocation()
        // En el repository,
        // se comprueba si ya esta guardada
        // y devuelve varios eventos al presenter:
        // - UPDATED_AND_NOT_SAVED_TO_LIST -> view.showCarLocation()
        // - UPDATED_AND_ALREADY_SAVED_TO_LIST -> view.showCarLocation() + view.showAlreadySaved()
        // - ERROR ->
    }

    @OnClick(R.id.btnAddToList)
    public void addToList() {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // presenter.subscribe();
        // setupGoogleMap(googleMap);

        map = googleMap;
        // cambiar this por getActivity()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_LOCATION_REQUEST_CODE_2);
            }
            return;
        }
        map.setMyLocationEnabled(true);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                handleOnMapLongClick(latLng);
            }
        });

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                handleOnMyLocationButtonClick();
                return true;
            }
        });

        // // // //
        showCarLocation(new CarLocation());
        // // // //
    }




    // Fragment
    @OnClick(R.id.btnCarLocation)
    public void handleShowCarLocation() {
        LatLng location = new LatLng(carLocation.getLatitude(), carLocation.getLongitude());
        animateCamera(location);
    }

    @OnClick(R.id.btnSaveCurrentLocation)
    public void handleSaveCurrentLocation() {
        LatLng location = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        updateCarLocation(location);
    }

    private void handleOnMapLongClick(LatLng location) {
        updateCarLocation(location);
    }

    private void handleOnMyLocationButtonClick() {
        if (lastKnownLocation != null) {
            LatLng location = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            animateCamera(location);
        }
    }

    private void animateCamera(LatLng location) {
        if (map != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location)
                    .zoom(17)
                    .bearing(0)
                    .tilt(40)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
