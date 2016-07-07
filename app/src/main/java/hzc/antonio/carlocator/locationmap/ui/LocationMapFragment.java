package hzc.antonio.carlocator.locationmap.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.locationmap.LocationMapPresenter;
import hzc.antonio.carlocator.locationmap.LocationMapPresenterImpl;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationMapFragment extends Fragment implements LocationMapView, OnMapReadyCallback {

    @BindView(R.id.container) RelativeLayout container;

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
        setupInjection();
        presenter.onCreate();
    }

    private void setupInjection() {
        app = (CarLocatorApp) getActivity().getApplication();
        presenter = new LocationMapPresenterImpl(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setupGoogleMap(googleMap);
        presenter.getCarLocation();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    //endregion











    @OnClick({R.id.btnCarLocation, R.id.btnAddToList, R.id.btnSaveCurrentLocation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCarLocation:
                break;
            case R.id.btnAddToList:
                break;
            case R.id.btnSaveCurrentLocation:
                break;
        }
    }


    private void handleOnMyLocationButtonClick() {

    }

    private void handleOnMapLongClick(LatLng latLng) {

    }


    @Override
    public void setCarLocation(CarLocation carLocation) {

    }


    @Override
    public void enableAddButton() {

    }

    @Override
    public void disableAddButton() {

    }

    @Override
    public void onCarLocationUpdated() {

    }

    @Override
    public void onCarLocationAdded() {

    }

    @Override
    public void onCarLocationNotAdded() {

    }

    @Override
    public void onCarLocationsError(String error) {

    }



    private void setupGoogleMap(GoogleMap googleMap) {
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

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                handleOnMyLocationButtonClick();
                return true;
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


}
