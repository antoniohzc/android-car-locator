package hzc.antonio.carlocator.locationslist.di;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationslist.LocationsListInteractor;
import hzc.antonio.carlocator.locationslist.LocationsListInteractorImpl;
import hzc.antonio.carlocator.locationslist.LocationsListPresenter;
import hzc.antonio.carlocator.locationslist.LocationsListPresenterImpl;
import hzc.antonio.carlocator.locationslist.LocationsListRepository;
import hzc.antonio.carlocator.locationslist.LocationsListRepositoryImpl;
import hzc.antonio.carlocator.locationslist.ui.LocationsListView;
import hzc.antonio.carlocator.locationslist.ui.adapters.LocationsListAdapter;
import hzc.antonio.carlocator.locationslist.ui.adapters.OnItemClickListener;

@Module
public class LocationsListModule {
    private LocationsListView view;
    private OnItemClickListener onItemClickListener;

    public LocationsListModule(LocationsListView view, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides @Singleton
    LocationsListView providesLocationsListView() {
        return this.view;
    }

    @Provides @Singleton
    LocationsListPresenter providesLocationsListPresenter(EventBus eventBus, LocationsListView view, LocationsListInteractor interactor) {
        return new LocationsListPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    LocationsListInteractor providesLocationsListInteractor(LocationsListRepository repository) {
        return new LocationsListInteractorImpl(repository);
    }

    @Provides @Singleton
    LocationsListRepository providesLocationsListRepository(EventBus eventBus) {
        return new LocationsListRepositoryImpl(eventBus);
    }

    @Provides @Singleton
    LocationsListAdapter providesLocationsListAdapter(Context context, Util util, List<CarLocation> dataset, OnItemClickListener onItemClickListener) {
        return new LocationsListAdapter(context, util, dataset, onItemClickListener);
    }

    @Provides @Singleton
    OnItemClickListener providesOnItemClickListener() {
        return this.onItemClickListener;
    }

    @Provides @Singleton
    List<CarLocation> providesCarLocationsList() {
        return new ArrayList<CarLocation>();
    }
}
