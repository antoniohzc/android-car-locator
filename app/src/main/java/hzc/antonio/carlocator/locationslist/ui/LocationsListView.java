package hzc.antonio.carlocator.locationslist.ui;

import java.util.List;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationsListView {
    void showList();
    void hideList();
    void showProgress();
    void hideProgress();

    void setCarLocations(List<CarLocation> list);
    void onCarLocationUpdated(CarLocation carLocation);
    void onCarLocationRemoved(CarLocation carLocation);
    void onEmptyList();
    void onCarLocationsError(String error);
}
