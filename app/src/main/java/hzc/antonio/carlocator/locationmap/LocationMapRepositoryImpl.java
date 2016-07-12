package hzc.antonio.carlocator.locationmap;

import com.google.android.gms.maps.model.LatLng;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import hzc.antonio.carlocator.domain.CustomAddressFromLocationCallback;
import hzc.antonio.carlocator.domain.Util;
import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CarLocation_Table;
import hzc.antonio.carlocator.entities.CustomAddress;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.locationmap.events.LocationMapEvent;

public class LocationMapRepositoryImpl implements LocationMapRepository {
    private EventBus eventBus;
    private Util util;

    private static final double MIN_DISTANCE = 25d;

    public LocationMapRepositoryImpl(EventBus eventBus, Util util) {
        this.eventBus = eventBus;
        this.util = util;
    }

    @Override
    public void getCarLocation() {
        CarLocation carLocation = SQLite.select()
                .from(CarLocation.class)
                .where(CarLocation_Table.current.eq(true))
                .querySingle();

        if (carLocation == null) {
            post(LocationMapEvent.ON_NOT_SAVED_CAR_LOCATION);
        }
        else {
            post(LocationMapEvent.ON_SET_CAR_LOCATION, carLocation);
        }
    }

    @Override
    public void updateCarLocation(CarLocation carLocation) {
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
        carLocation.setFavourite(false);
        carLocation.insert();

        post(LocationMapEvent.ON_SET_CAR_LOCATION, carLocation);
    }

    @Override
    public void addToList(final CarLocation carLocation) {
        List<CarLocation> storedLocations = SQLite.select()
                .from(CarLocation.class)
                .where(CarLocation_Table.favourite.eq(true))
                .queryList();

        for (CarLocation item : storedLocations) {
            if (areTooNear(item.getLatLng(), carLocation.getLatLng())) {
                post(LocationMapEvent.ON_NOT_ADDED_TO_LIST, item);
                return;
            }
        }

        util.getCustomAddressFromLocation(carLocation.getLatitude(), carLocation.getLongitude(), new CustomAddressFromLocationCallback() {
            @Override
            public void onFinished(CustomAddress address) {
                address.setCarLocationId(carLocation.getId());
                address.save();

                carLocation.setAddress(address);
                carLocation.setFavourite(true);
                carLocation.update();
                post(LocationMapEvent.ON_ADDED_TO_LIST);
            }
        });
    }


    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, CarLocation carLocation) {
        post(type, carLocation, null);
    }

    private void post(int type, CarLocation carLocation, String error) {
        LocationMapEvent event = new LocationMapEvent();
        event.setType(type);
        event.setCarLocation(carLocation);
        event.setError(error);
        eventBus.post(event);
    }

    private boolean areTooNear(LatLng a, LatLng b) {
        double deltaX = Math.abs(a.longitude - b.longitude) * 110 * 1000;
        double deltaY = Math.abs(a.latitude - b.latitude) * 110 * 1000;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) ;

        return distance < MIN_DISTANCE;
    }
}
