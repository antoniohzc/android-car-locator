package hzc.antonio.carlocator.locationmap.events;

import hzc.antonio.carlocator.entities.CarLocation;

public class LocationMapEvent {
    private int type;
    private CarLocation carLocation;
    private String error;

    public static final int ON_SET_CAR_LOCATION = 0;
    public static final int ON_NOT_SAVED_CAR_LOCATION = 1;
    public static final int ON_ADDED_TO_LIST = 2;
    public static final int ON_NOT_ADDED_TO_LIST = 3;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CarLocation getCarLocation() {
        return carLocation;
    }

    public void setCarLocation(CarLocation carLocation) {
        this.carLocation = carLocation;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
