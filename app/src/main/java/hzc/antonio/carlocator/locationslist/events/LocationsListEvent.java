package hzc.antonio.carlocator.locationslist.events;

import java.util.List;

import hzc.antonio.carlocator.entities.CarLocation;

public class LocationsListEvent {
    private int type;
    private List<CarLocation> carLocations;
    private String error;

    public static final int ON_SET_CAR_LOCATIONS = 0;
    public static final int ON_CAR_LOCATION_UPDATED = 1;
    public static final int ON_CAR_LOCATION_REMOVED = 2;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CarLocation> getCarLocations() {
        return carLocations;
    }

    public void setCarLocations(List<CarLocation> carLocations) {
        this.carLocations = carLocations;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
