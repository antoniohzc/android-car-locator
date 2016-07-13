package hzc.antonio.carlocator.locationslist.ui;

import java.util.List;

import hzc.antonio.carlocator.entities.CarLocation;

public interface LocationsListView {
    void showList();
    void hideList();
    void showProgress();
    void hideProgress();
    void showMessageEmptyList();
    void hideMessageEmptyList();

    void setCarLocations(List<CarLocation> list);
    void onCarLocationRemoved(CarLocation carLocation);
    void onCarLocationsError(String error);
}
