package hzc.antonio.carlocator.locationmap;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationMapRepository {
    void getCarLocation();
    void updateCarLocation(CarLocation carLocation);
    void addToList(CarLocation carLocation);
}
