package hzc.antonio.carlocator.locationmap;

import hzc.antonio.carlocator.entities.CarLocation;

public class LocationMapInteractorImpl implements LocationMapInteractor {
    private LocationMapRepository repository;

    public LocationMapInteractorImpl(LocationMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCarLocation() {
        repository.getCarLocation();
    }

    @Override
    public void updateCarLocation (CarLocation carLocation) {
        repository.updateCarLocation(carLocation);
    }

    @Override
    public void addToList(CarLocation carLocation) {
        repository.addToList(carLocation);
    }
}
