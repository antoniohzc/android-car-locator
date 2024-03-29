package hzc.antonio.carlocator.locationmap.ui;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
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
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.domain.CustomAddressFromLocationCallback;
import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CustomAddress;
import hzc.antonio.carlocator.locationmap.LocationMapPresenter;
import hzc.antonio.carlocator.main.ui.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationMapFragment extends Fragment implements LocationMapView, OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    @BindView(R.id.container) RelativeLayout container;
    @BindView(R.id.btnAddToList) ImageButton btnAddToList;

    @Inject
    LocationMapPresenter presenter;
    @Inject
    Util util;

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

        if (carLocation != null) {
            moveCamera(carLocation.getLatLng());
        }
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

    private void moveCamera(LatLng location) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(17)
                .bearing(0)
                .tilt(40)
                .build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    //endregion


    //region Events from presenter
    @Override
    public void setCarLocation(final CarLocation carLocation) {
        this.carLocation = carLocation;
        LatLng location = carLocation.getLatLng();

        util.getCustomAddressFromLocation(carLocation.getLatitude(), carLocation.getLongitude(), new CustomAddressFromLocationCallback() {
            @Override
            public void onFinished(CustomAddress address) {
                carLocation.setAddress(address);
            }
        });

        map.clear();
        map.addMarker(new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker)));
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
        btnAddToList.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.btn_add_list));
    }

    @Override
    public void disableAddButton() {
        btnAddToList.setEnabled(false);
        btnAddToList.setImageDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.btn_add_list_disabled));
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


    //region InfoWindow
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

        final TextView lblCity = (TextView) view.findViewById(R.id.lblCity);
        final TextView lblZip = (TextView) view.findViewById(R.id.lblZip);
        final TextView lblStreet = (TextView) view.findViewById(R.id.lblStreet);

        CustomAddress address = carLocation.getAddress();
        if (address != null) {
            lblCity.setText(address.getCity());
            lblZip.setText("(" + address.getPostalCode() + " " + address.getProvince() + ")");
            lblStreet.setText(address.getStreet());
        }

        return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_action_buttons, null);
        ImageView btnShare = (ImageView) view.findViewById(R.id.btnShare);
        ImageView btnDisplayStreetView = (ImageView) view.findViewById(R.id.btnDisplayStreetView);
        ImageView btnLaunchNavigation = (ImageView) view.findViewById(R.id.btnLaunchNavigation);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Util.getShareIntent(carLocation, getString(R.string.shareintent_subject));
                String chooserTitle = getString(R.string.shareintent_title);
                startActivity(Intent.createChooser(intent, chooserTitle));
            }
        });

        btnDisplayStreetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Util.getStreetViewIntent(carLocation);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        btnLaunchNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Util.getNavigateToIntent(carLocation);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        Dialog dialog = new Dialog(this.getContext(), R.style.NoActionBarDialog);
        dialog.setContentView(view);
        dialog.show();
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

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LatLng location = getLastKnownLocation();
                if (location != null) {
                    animateCamera(location);
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                handleOnMapLongClick(latLng);
            }
        });

        map.setInfoWindowAdapter(this);
        map.setOnInfoWindowClickListener(this);

        map.setBuildingsEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
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
