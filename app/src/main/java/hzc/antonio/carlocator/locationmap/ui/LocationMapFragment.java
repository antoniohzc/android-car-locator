package hzc.antonio.carlocator.locationmap.ui;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.locationmap.LocationMapPresenter;
import hzc.antonio.carlocator.main.ui.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationMapFragment extends Fragment implements LocationMapView, OnMapReadyCallback {

    @BindView(R.id.container) RelativeLayout container;
    @BindView(R.id.btnAddToList) Button btnAddToList;

    @Inject
    LocationMapPresenter presenter;

    private CarLocatorApp app;
    private GoogleMap map;
    private CarLocation carLocation;
    private static final int PERMISSIONS_LOCATION_REQUEST_CODE = 1;

    public LocationMapFragment() { }


    //region Fragment lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (CarLocatorApp) getActivity().getApplication();
        setupInjection();

        presenter.onCreate();
    }

    private void setupInjection() {
        app.getLocationMapComponent(this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && map != null) {
             presenter.getCarLocation();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupGoogleMap(googleMap);
        presenter.getCarLocation();
        handleShowCarLocation();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
    //endregion


    private LatLng getLastKnownLocation() {
        Location fusedLocation = ((MainActivity)getActivity()).getLastKnownLocation();

        if (fusedLocation == null) {
            return null;
        }
        else {
            double lat = fusedLocation.getLatitude();
            double lng = fusedLocation.getLongitude();
            return new LatLng(lat, lng);
        }
    }


    //region User action handlers
    @OnClick(R.id.btnCarLocation)
    public void handleShowCarLocation() {
        if (carLocation != null && map != null) {
            animateCamera(carLocation.getLatLng());
        }
    }

    @OnClick(R.id.btnSaveCurrentLocation)
    public void handleSaveCurrentLocation() {
        LatLng location = getLastKnownLocation();
        if (location != null) {
            updateCarLocation(location);
        }
        else {
            Snackbar.make(container, getString(R.string.main_error_location_notavailable), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void handleOnMapLongClick(LatLng location) {
        updateCarLocation(location);
    }

    private void updateCarLocation(LatLng location) {
        carLocation = new CarLocation();
        carLocation.setLatLng(location);

        presenter.updateCarLocation(carLocation);
    }

    @OnClick(R.id.btnAddToList)
    public void handleAddToList() {
        if (map != null && carLocation != null) {



            presenter.addToList(carLocation);
        }
    }

    private void animateCamera(LatLng location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(17)
                .bearing(0)
                .tilt(40)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    //endregion


    //region Events from presenter
    @Override
    public void setCarLocation(CarLocation carLocation) {
        this.carLocation = carLocation;
        LatLng location = carLocation.getLatLng();

        map.clear();
        map.addMarker(new MarkerOptions().position(location));

        // animateCamera(location);
    }

    @Override
    public void onCarLocationNotSaved() {
        LatLng location = getLastKnownLocation();

        if (location != null) {
            updateCarLocation(location);
        }
        else {
            updateCarLocation(new LatLng(0, 0));
        }
    }

    @Override
    public void enableAddButton() {
        btnAddToList.setEnabled(true);
    }

    @Override
    public void disableAddButton() {
        btnAddToList.setEnabled(false);
    }

    @Override
    public void onCarLocationAdded() {
        String msg = getString(R.string.locationmap_notice_added);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCarLocationNotAdded(CarLocation storedCarLocation) {
        String msg = String.format(getString(R.string.locationmap_notice_notadded), storedCarLocation.getTimestamp());
        Snackbar.make(container, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCarLocationsError(String error) {

    }
    //endregion


    //region Setup map
    private void setupGoogleMap(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_LOCATION_REQUEST_CODE);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (map != null) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    map.setMyLocationEnabled(true);
                }
            }
        }
    }
    //endregion
}
