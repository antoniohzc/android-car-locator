package hzc.antonio.carlocator.locationslist;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Arrays;
import java.util.List;

import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CarLocation_Table;
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
                .orderBy(CarLocation_Table.timestamp, false)
                .queryList();

        if (list.isEmpty()) {
            post(LocationsListEvent.ON_EMPTY_LIST);
        }
        else {
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
            }
        }

        carLocation.setTimestamp(Util.generateTimestamp());
        carLocation.setCurrent(true);
        carLocation.update();
        post(LocationsListEvent.ON_CAR_LOCATION_UPDATED, Arrays.asList(carLocation));
    }

    @Override
    public void removeCarLocation(CarLocation carLocation) {
        carLocation.delete();
        post(LocationsListEvent.ON_CAR_LOCATION_REMOVED, Arrays.asList(carLocation));
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
    }
}
