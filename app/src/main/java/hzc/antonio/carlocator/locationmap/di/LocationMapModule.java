package hzc.antonio.carlocator.locationmap.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationmap.LocationMapInteractor;
import hzc.antonio.carlocator.locationmap.LocationMapInteractorImpl;
import hzc.antonio.carlocator.locationmap.LocationMapPresenter;
import hzc.antonio.carlocator.locationmap.LocationMapPresenterImpl;
import hzc.antonio.carlocator.locationmap.LocationMapRepository;
import hzc.antonio.carlocator.locationmap.LocationMapRepositoryImpl;
import hzc.antonio.carlocator.locationmap.ui.LocationMapView;

@Module
public class LocationMapModule {
    private LocationMapView view;

    public LocationMapModule(LocationMapView view) {
        this.view = view;
    }

    @Provides @Singleton
    LocationMapView providesLocationMapView() {
        return this.view;
    }

    @Provides @Singleton
    LocationMapPresenter providesLocationMapPresenter(EventBus eventBus, LocationMapView view, LocationMapInteractor interactor) {
        return new LocationMapPresenterImpl(eventBus, view, interactor);
    }

    @Provides @Singleton
    LocationMapInteractor providesLocationMapInteractor(LocationMapRepository repository) {
        return new LocationMapInteractorImpl(repository);
    }

    @Provides @Singleton
    LocationMapRepository providesLocationMapRepository(EventBus eventBus, Util util) {
        return new LocationMapRepositoryImpl(eventBus, util);
    }
}
