package hzc.antonio.carlocator.locationslist;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Arrays;
import java.util.List;

import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CarLocation_Table;
import hzc.antonio.carlocator.entities.CustomAddress;
import hzc.antonio.carlocator.entities.CustomAddress_Table;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationslist.events.LocationsListEvent;

public class LocationsListRepositoryImpl implements LocationsListRepository {
    private EventBus eventBus;

    public LocationsListRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getCarLocations() {
        List<CarLocation> list = SQLite.select()
                .from(CarLocation.class)
                .where(CarLocation_Table.favourite.eq(true))
                .orderBy(CarLocation_Table.current, false)
                .orderBy(CarLocation_Table.timestamp, false)
                .queryList();

        if (list.isEmpty()) {
            post(LocationsListEvent.ON_EMPTY_LIST);
        }
        else {
            for (CarLocation item : list) {
                CustomAddress address = getCustomAddress(item);
                item.setAddress(address != null ? address : Util.getCustomAddressDefaults());
            }

            post(LocationsListEvent.ON_SET_CAR_LOCATIONS, list);
        }
    }

    @Override
    public void updateCarLocationMakeCurrent(CarLocation carLocation) {
        CarLocation storedLocation = SQLite.select()
                .from(CarLocation.class)
                .where(CarLocation_Table.current.eq(true))
                .querySingle();

        if (storedLocation != null) {
            if (storedLocation.isFavourite()) {
                storedLocation.setCurrent(false);
                storedLocation.update();
            }
            else {
                storedLocation.delete();
                deleteCustomAddress(storedLocation);
            }
        }

        carLocation.setTimestamp(Util.generateTimestamp());
        carLocation.setCurrent(true);
        carLocation.update();

        getCarLocations();
    }

    @Override
    public void removeCarLocation(CarLocation carLocation) {
        carLocation.delete();
        deleteCustomAddress(carLocation);
        post(LocationsListEvent.ON_CAR_LOCATION_REMOVED, Arrays.asList(carLocation));

        List<CarLocation> list = SQLite.select()
                .from(CarLocation.class)
                .where(CarLocation_Table.favourite.eq(true))
                .queryList();

        if (list.isEmpty()) {
            post(LocationsListEvent.ON_EMPTY_LIST);
        }
    }

    private CustomAddress getCustomAddress(CarLocation carLocation) {
        CustomAddress address = SQLite.select()
                .from(CustomAddress.class)
                .where(CustomAddress_Table.carLocationId.eq(carLocation.getId()))
                .querySingle();
        return address;
    }

    private void deleteCustomAddress(CarLocation carLocation) {
        CustomAddress address = getCustomAddress(carLocation);
        if (address != null) {
            address.delete();
        }
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, List<CarLocation> carLocations) {
        post(type, carLocations, null);
    }

    private void post(int type, List<CarLocation> carLocations, String error) {
        LocationsListEvent event = new LocationsListEvent();
        event.setType(type);
        event.setCarLocations(carLocations);
        event.setError(error);
        eventBus.post(event);
    }
}
