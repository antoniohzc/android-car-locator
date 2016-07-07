package hzc.antonio.carlocator.locationmap.ui;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationMapView {
    void enableAddButton();
    void disableAddButton();

    void setCarLocation(CarLocation carLocation);

    void onCarLocationUpdated();
    void onCarLocationAdded();
    void onCarLocationNotAdded();
    void onCarLocationsError(String error);
}
