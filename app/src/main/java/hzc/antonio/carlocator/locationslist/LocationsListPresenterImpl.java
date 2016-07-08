package hzc.antonio.carlocator.locationslist;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationslist.events.LocationsListEvent;
import hzc.antonio.carlocator.locationslist.ui.LocationsListView;

public class LocationsListPresenterImpl implements LocationsListPresenter {
    private EventBus eventBus;
    private LocationsListView view;
    private LocationsListInteractor interactor;

    public LocationsListPresenterImpl(EventBus eventBus, LocationsListView view, LocationsListInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void getCarLocations() {
        if (view != null) {
            view.hideList();
            view.showProgress();
        }
        interactor.getCarLocations();
    }

    @Override
    public void updateCarLocationMakeCurrent(CarLocation carLocation) {
        if (view != null) {
            view.hideList();
            view.showProgress();
        }
        interactor.updateCarLocationMakeCurrent(carLocation);
    }

    @Override
    public void removeCarLocation(CarLocation carLocation) {
        if (view != null) {
            view.hideList();
            view.showProgress();
        }
        interactor.removeCarLocation(carLocation);
    }

    @Override
    public void onEventMainThread(LocationsListEvent event) {
        if (view != null) {
            view.showList();
            view.hideProgress();

            String error = event.getError();
            if (error != null) {
                view.onCarLocationsError(error);
            }
            else {
                switch (event.getType()) {
                    case LocationsListEvent.ON_SET_CAR_LOCATIONS:
                        view.setCarLocations(event.getCarLocations());
                        break;
                    case LocationsListEvent.ON_CAR_LOCATION_UPDATED:
                        view.onCarLocationUpdated(event.getCarLocations().get(0));
                        break;
                    case LocationsListEvent.ON_CAR_LOCATION_REMOVED:
                        view.onCarLocationRemoved(event.getCarLocations().get(0));
                        break;
                    case LocationsListEvent.ON_EMPTY_LIST:
                        view.onEmptyList();
                        break;
                }
            }
        }
    }
}
