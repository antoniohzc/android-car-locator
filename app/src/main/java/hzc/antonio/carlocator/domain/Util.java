package hzc.antonio.carlocator.domain;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CustomAddress;

public class Util {
    private Geocoder geocoder;

    public Util(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public CustomAddress getCustomAddressFromLocation(double lat, double lng) {
        List<Address> addresses = null;
        CustomAddress mAddress = new CustomAddress();

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses == null || addresses.size() == 0) {
                mAddress = getCustomAddressDefaults();
            }
            else {
                Address address = addresses.get(0);

//                ArrayList<String> addressLines = new ArrayList<String>();
//                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                    addressLines.add(address.getAddressLine(i));
//                }
//                mAddress.setStreet(TextUtils.join(", ", addressLines));

                mAddress.setStreet(address.getMaxAddressLineIndex() == 0 ? "Street" : address.getAddressLine(0));
                mAddress.setCity(address.getMaxAddressLineIndex() == 1 ? "City" : address.getAddressLine(1));
                mAddress.setCountryCode(address.getCountryCode());
                mAddress.setCountryName(address.getCountryName());
                mAddress.setPostalCode(address.getPostalCode());
                mAddress.setProvince(address.getAdminArea());
                mAddress.setFeaturedName(address.getFeatureName() == null ? "" : address.getFeatureName());
            }
        }
        catch (IOException e) {
            mAddress = getCustomAddressDefaults();
        }

        return mAddress;
    }

    private CustomAddress getCustomAddressDefaults() {
        CustomAddress mAddress = new CustomAddress();

        mAddress.setStreet("Street");
        mAddress.setCity("City");
        mAddress.setPostalCode("ZIP");
        mAddress.setProvince("Province");
        mAddress.setCountryCode("__");
        mAddress.setCountryName("Country");
        mAddress.setFeaturedName("");

        return mAddress;
    }

    public String getImageMapUrl(CarLocation carLocation) {
        return "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=" + carLocation.getLatitude() + "," + carLocation.getLongitude() +
                "&zoom=16" +
                "&size=180x180" +
                "&scale=2" +
                "&markers=color:green%7Csize:normal%7C" + carLocation.getLatitude() + "," + carLocation.getLongitude() +
                "&style=feature:all%7Celement:labels%7Clightness:25";
    }


    public static String generateTimestamp() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date());
    }

}
