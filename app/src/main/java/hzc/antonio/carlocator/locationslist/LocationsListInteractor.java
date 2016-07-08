package hzc.antonio.carlocator.locationslist;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.locationslist.events.LocationsListEvent;

public interface LocationsListInteractor {
    void getCarLocations();
    void updateCarLocationMakeCurrent(CarLocation carLocation);
    void removeCarLocation(CarLocation carLocation);
}
