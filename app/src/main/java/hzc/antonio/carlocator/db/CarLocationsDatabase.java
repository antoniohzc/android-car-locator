package hzc.antonio.carlocator.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = CarLocationsDatabase.NAME, version = CarLocationsDatabase.VERSION)
public class CarLocationsDatabase {
    public static final int VERSION = 1;
    public static final String NAME = "CarLocations";
}
