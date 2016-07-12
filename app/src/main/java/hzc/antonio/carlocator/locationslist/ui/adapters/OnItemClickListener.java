package hzc.antonio.carlocator.locationslist.ui.adapters;

import hzc.antonio.carlocator.entities.CarLocation;

public interface OnItemClickListener {
    void onShareClick(CarLocation carLocation);
    void onDisplayStreetViewClick(CarLocation carLocation);
    void onLaunchNavigationClick(CarLocation carLocation);
    void onRemoveClick(CarLocation carLocation);
    void onMakeCurrentClick(CarLocation carLocation);
}
