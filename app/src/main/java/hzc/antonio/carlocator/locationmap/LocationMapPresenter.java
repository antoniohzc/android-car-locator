package hzc.antonio.carlocator.locationmap;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.locationmap.events.LocationMapEvent;

public interface LocationMapPresenter {
    void onCreate();
    void onDestroy();

    void getCarLocation();
    void updateCarLocation(CarLocation carLocation);
    void addToList(CarLocation carLocation);

    void onEventMainThread(LocationMapEvent event);
}
