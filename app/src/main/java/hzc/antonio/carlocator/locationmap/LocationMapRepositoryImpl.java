package hzc.antonio.carlocator.locationmap;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.libs.GreenRobotEventBusSingleton;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationmap.events.LocationMapEvent;

public class LocationMapRepositoryImpl implements LocationMapRepository {
    private EventBus eventBus;

//    public LocationMapRepositoryImpl(EventBus eventBus) {
//        this.eventBus = eventBus;
//    }

    public LocationMapRepositoryImpl() {
        this.eventBus = GreenRobotEventBusSingleton.getInstance();
    }

    @Override
    public void getCarLocation() {

    }

    @Override
    public void updateCarLocation(CarLocation carLocation) {

    }

    @Override
    public void addToList(CarLocation carLocation) {

    }


    private void post(int type, CarLocation carLocation) {
        post(type, carLocation, null);
    }

    private void post(int type, CarLocation carLocation, String error) {
        LocationMapEvent event = new LocationMapEvent();
        event.setType(type);
        event.setCarLocation(carLocation);
        event.setError(error);
        eventBus.post(event);
    }
}
