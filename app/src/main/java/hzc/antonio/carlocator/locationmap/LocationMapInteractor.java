package hzc.antonio.carlocator.locationmap;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationMapInteractor {
    void getCarLocation();
    void updateCarLocation(CarLocation carLocation);
    void addToList(CarLocation carLocation);
}
