package hzc.antonio.carlocator.locationmap;

import org.greenrobot.eventbus.Subscribe;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.libs.GreenRobotEventBus;
import hzc.antonio.carlocator.libs.GreenRobotEventBusSingleton;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationmap.events.LocationMapEvent;
import hzc.antonio.carlocator.locationmap.ui.LocationMapView;

public class LocationMapPresenterImpl implements LocationMapPresenter {
    private EventBus eventBus;
    private LocationMapView view;
    private LocationMapInteractor interactor;

//    public LocationMapPresenterImpl(EventBus eventBus, LocationMapView view, LocationMapInteractor interactor) {
//        this.eventBus = eventBus;
//        this.view = view;
//        this.interactor = interactor;
//    }

    public LocationMapPresenterImpl(LocationMapView view) {
        this.eventBus = GreenRobotEventBusSingleton.getInstance();
        this.view = view;
        this.interactor = new LocationMapInteractorImpl();
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
    public void getCarLocation() {
        interactor.getCarLocation();
    }

    @Override
    public void updateCarLocation(CarLocation carLocation) {
        interactor.updateCarLocation(carLocation);
    }

    @Override
    public void addToList(CarLocation carLocation) {
        interactor.addToList(carLocation);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LocationMapEvent event) {
        if (view != null) {
            String error = event.getError();
            if (error != null) {
                view.onCarLocationsError(error);
            }
            else {
                switch (event.getType()) {
                    case LocationMapEvent.ON_NOT_SAVED_CAR_LOCATION:
                        view.onCarLocationNotSaved();
                        break;
                    case LocationMapEvent.ON_SET_CAR_LOCATION:
                        setCarLocation(event.getCarLocation());
                        break;
                    case LocationMapEvent.ON_ADDED_TO_LIST:
                        view.onCarLocationAdded();
                        view.disableAddButton();
                        break;
                    case LocationMapEvent.ON_NOT_ADDED_TO_LIST:
                        view.onCarLocationNotAdded(event.getCarLocation());
                        break;
                }
            }
        }
    }

    private void setCarLocation(CarLocation carLocation) {
        if (carLocation.isFavourite()) {
            view.disableAddButton();
        }
        else {
            view.enableAddButton();
        }

        view.setCarLocation(carLocation);
    }
}
