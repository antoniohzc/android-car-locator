package hzc.antonio.carlocator.locationslist;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationsListInteractor {
    void getCarLocations();
    void updateCarLocationMakeCurrent(CarLocation carLocation);
    void removeCarLocation(CarLocation carLocation);
}
