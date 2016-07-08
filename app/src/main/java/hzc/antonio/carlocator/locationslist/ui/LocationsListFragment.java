package hzc.antonio.carlocator.locationslist.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.locationslist.LocationsListPresenter;
import hzc.antonio.carlocator.locationslist.ui.adapters.LocationsListAdapter;
import hzc.antonio.carlocator.locationslist.ui.adapters.OnItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationsListFragment extends Fragment implements LocationsListView, OnItemClickListener {
    @BindView(R.id.container) FrameLayout container;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @Inject
    LocationsListPresenter presenter;
    @Inject
    LocationsListAdapter adapter;

    public LocationsListFragment() { }

    //region Fragment lifecycle
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();
        presenter.onCreate();
    }

    private void setupInjection() {
        CarLocatorApp app = (CarLocatorApp) getActivity().getApplication();
        app.getLocationsListComponent(this, this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        presenter.getCarLocations();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            presenter.getCarLocations();
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
    //endregion


    @Override
    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


    //region User action handlers
    @Override
    public void onLaunchNavigationClick(CarLocation carLocation) {
        String uri = "google.navigation:q=" + carLocation.getLatitude() + "," + carLocation.getLongitude();
        sendIntent(uri);
    }

    @Override
    public void onDisplayStreetViewClick(CarLocation carLocation) {
        String uri = "google.streetview:cbll=" + carLocation.getLatitude() + "," + carLocation.getLongitude();
        sendIntent(uri);
    }

    private void sendIntent(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onRemoveClick(CarLocation carLocation) {
        presenter.removeCarLocation(carLocation);
    }

    @Override
    public void onMakeCurrentClick(CarLocation carLocation) {
        presenter.updateCarLocationMakeCurrent(carLocation);
    }
    //endregion


    //region Events from presenter
    @Override
    public void setCarLocations(List<CarLocation> list) {
        adapter.setCarLocations(list);
    }

    @Override
    public void onCarLocationUpdated(CarLocation carLocation) {
        presenter.getCarLocations();
    }

    @Override
    public void onCarLocationRemoved(CarLocation carLocation) {
        adapter.removeCarLocation(carLocation);
    }

    @Override
    public void onEmptyList() {
        String msg = getString(R.string.locationslist_notice_emptylist);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCarLocationsError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }
    //endregion
}