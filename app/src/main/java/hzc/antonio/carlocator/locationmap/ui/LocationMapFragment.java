package hzc.antonio.carlocator.locationmap.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
public class LocationMapFragment extends Fragment implements LocationMapView {

    @BindView(R.id.container) RelativeLayout container;

    LocationMapPresenter presenter;

    private CarLocatorApp app;
    private CarLocation carLocation;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_map, container, false);
        ButterKnife.bind(this, view);
        presenter.getCarLocation();
        return view;
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


    @Override
    public void enableAddButton() {

    }

    @Override
    public void disableAddButton() {

    }

    @Override
    public void setCarLocation(CarLocation carLocation) {

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
}
