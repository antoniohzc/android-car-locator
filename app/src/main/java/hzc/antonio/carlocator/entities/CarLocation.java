package hzc.antonio.carlocator.entities;

import com.google.android.gms.maps.model.LatLng;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import hzc.antonio.carlocator.db.CarLocationsDatabase;

@Table(database = CarLocationsDatabase.class)
public class CarLocation extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private int id;
    @Column
    private double latitude;
    @Column
    private double longitude;
    @Column
    private String timestamp;
    @Column
    private boolean current;
    @Column
    private boolean favourite;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public LatLng getLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }
    
    public void setLatLng(LatLng location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o instanceof CarLocation) {
            CarLocation location = (CarLocation) o;
            equal = this.id == location.getId();
        }
        return equal;
    }
}
