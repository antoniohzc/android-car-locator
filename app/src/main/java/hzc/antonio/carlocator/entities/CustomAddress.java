package hzc.antonio.carlocator.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import hzc.antonio.carlocator.db.CarLocationsDatabase;

@Table(database = CarLocationsDatabase.class)
public class CustomAddress extends BaseModel{

    @PrimaryKey
    private int carLocationId;
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private String postalCode;
    @Column
    private String province;
    @Column
    private String countryName;
    @Column
    private String countryCode;
    @Column
    private String featuredName;

    public int getCarLocationId() {
        return carLocationId;
    }

    public void setCarLocationId(int carLocationId) {
        this.carLocationId = carLocationId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFeaturedName() {
        return featuredName;
    }

    public void setFeaturedName(String featuredName) {
        this.featuredName = featuredName;
    }
}
