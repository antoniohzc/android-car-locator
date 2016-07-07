package hzc.antonio.carlocator.locationmap.events;

import hzc.antonio.carlocator.entities.CarLocation;

public class LocationMapEvent {
    private int type;
    private CarLocation carLocation;
    private String error;



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
