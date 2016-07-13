package hzc.antonio.carlocator.locationslist.ui;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hzc.antonio.carlocator.CarLocatorApp;
import hzc.antonio.carlocator.R;
import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.libs.base.ImageLoader;
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
    @BindView(R.id.lblEmptyList) TextView lblEmptyList;

    @Inject
    LocationsListPresenter presenter;
    @Inject
    LocationsListAdapter adapter;
    @Inject
    ImageLoader imageLoader;

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


    public RecyclerView getRecyclerView() {
        return (RecyclerView) getView().findViewById(R.id.recyclerView);
    }


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

    @Override
    public void showMessageEmptyList() {
        lblEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMessageEmptyList() {
        lblEmptyList.setVisibility(View.GONE);
    }


    //region User action handlers
    @Override
    public void onShareClick(CarLocation carLocation) {
        Intent intent = Util.getShareIntent(carLocation, getString(R.string.shareintent_subject));
        String chooserTitle = getString(R.string.shareintent_title);
        startActivity(Intent.createChooser(intent, chooserTitle));
    }

    @Override
    public void onDisplayStreetViewClick(CarLocation carLocation) {
        Intent intent = Util.getStreetViewIntent(carLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onLaunchNavigationClick(CarLocation carLocation) {
        Intent intent = Util.getNavigateToIntent(carLocation);
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

    @Override
    public void onImageMapClick(CarLocation carLocation) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_img_map, null);

        ImageView imgMap = (ImageView) view.findViewById(R.id.imgMap);
        imageLoader.load(imgMap, Util.getImageMapUrl(carLocation, true));

        Dialog dialog = new Dialog(this.getContext(), R.style.TransparentDialog);
        dialog.setContentView(view);
        dialog.show();
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
    public void onCarLocationsError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }
    //endregion
}
