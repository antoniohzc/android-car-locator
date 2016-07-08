package hzc.antonio.carlocator.locationslist;

import hzc.antonio.carlocator.entities.CarLocation;

public class LocationsListInteractorImpl implements LocationsListInteractor {
    private LocationsListRepository repository;

    public LocationsListInteractorImpl(LocationsListRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCarLocations() {
        repository.getCarLocations();
    }

    @Override
    public void updateCarLocationMakeCurrent(CarLocation carLocation) {
        repository.updateCarLocationMakeCurrent(carLocation);
    }

    @Override
    public void removeCarLocation(CarLocation carLocation) {
        repository.removeCarLocation(carLocation);
    }
}
