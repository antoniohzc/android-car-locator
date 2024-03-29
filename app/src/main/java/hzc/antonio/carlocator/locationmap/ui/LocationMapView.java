package hzc.antonio.carlocator.locationmap.ui;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationMapView {
    void enableAddButton();
    void disableAddButton();

    void setCarLocation(CarLocation carLocation);

    void onCarLocationNotSaved();
    void onCarLocationAdded();
    void onCarLocationNotAdded(CarLocation storedCarLocation);
    void onCarLocationsError(String error);
}
