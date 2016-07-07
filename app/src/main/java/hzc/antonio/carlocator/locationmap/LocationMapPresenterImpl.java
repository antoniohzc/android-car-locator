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

        }
    }
}
