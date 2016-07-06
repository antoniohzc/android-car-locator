package hzc.antonio.carlocator.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.maps.model.LatLng;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import hzc.antonio.carlocator.db.CarLocationsDatabase;

@Table(database = CarLocationsDatabase.class)
public class CarLocation extends BaseModel {
    @JsonIgnore
    private String uid;

    @PrimaryKey
    private String timestamp;

    @Column
    private double latitude;
    
    @Column
    private double longitude;

    @JsonIgnore
    @Column
    private boolean currentCarLocation;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public boolean isCurrentCarLocation() {
        return currentCarLocation;
    }

    public void setCurrentCarLocation(boolean currentCarLocation) {
        this.currentCarLocation = currentCarLocation;
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
            equal = this.timestamp.equals(location.getTimestamp());
        }
        return equal;
    }
}
