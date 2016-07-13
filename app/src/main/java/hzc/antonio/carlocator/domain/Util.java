package hzc.antonio.carlocator.domain;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hzc.antonio.carlocator.entities.CarLocation;
import hzc.antonio.carlocator.entities.CustomAddress;

public class Util {
    private Geocoder geocoder;

    private static final String STATIC_MAP_ZOOM_SMALL = "16";
    private static final String STATIC_MAP_ZOOM_LARGE = "17";
    private static final String STATIC_MAP_SIZE_SMALL = "180";
    private static final String STATIC_MAP_SIZE_LARGE = "300";

    public Util(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void getCustomAddressFromLocation(final double lat, final double lng, final CustomAddressFromLocationCallback listener) {
        new AsyncTask<Void, Void, CustomAddress>() {
            @Override
            protected CustomAddress doInBackground(Void... params) {
                try {
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(lat, lng, 1);

                    if (addresses == null || addresses.size() == 0) {
                        return getCustomAddressDefaults();
                    }
                    else {
                        Address address = addresses.get(0);
                        CustomAddress mAddress = new CustomAddress();

//                        ArrayList<String> addressLines = new ArrayList<String>();
//                        for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                            addressLines.add(address.getAddressLine(i));
//                        }
//                        mAddress.setStreet(TextUtils.join(", ", addressLines));

                        mAddress.setStreet(
                                (address.getThoroughfare() == null ? "Street" : address.getThoroughfare()) +
                                ", " +
                                (address.getSubThoroughfare() == null ? "0" : address.getSubThoroughfare()));
                        mAddress.setCity(address.getLocality() == null ? "City" : address.getLocality());
                        mAddress.setPostalCode(address.getPostalCode()== null ? "00000" : address.getPostalCode());
                        mAddress.setProvince(address.getSubAdminArea()== null ? "Province" : address.getSubAdminArea());
                        mAddress.setCountryCode(address.getCountryCode()== null ? "__" : address.getCountryCode());
                        mAddress.setCountryName(address.getCountryName()== null ? "Country" : address.getCountryName());
                        mAddress.setFeaturedName(address.getFeatureName() == null ? "" : address.getFeatureName());

                        return mAddress;
                    }
                }
                catch (IOException e) {
                    return getCustomAddressDefaults();
                }
            }

            @Override
            protected void onPostExecute(CustomAddress address) {
                listener.onFinished(address);
            }
        }.execute();


    }

    public static CustomAddress getCustomAddressDefaults() {
        CustomAddress mAddress = new CustomAddress();

        mAddress.setStreet("Street");
        mAddress.setCity("City");
        mAddress.setPostalCode("00000");
        mAddress.setProvince("Province");
        mAddress.setCountryCode("__");
        mAddress.setCountryName("Country");
        mAddress.setFeaturedName("");

        return mAddress;
    }


    public static String generateTimestamp() {
        return new SimpleDateFormat("yyyy.MM.dd - HH:mm").format(new Date());
    }

    public static String getImageMapUrl(CarLocation carLocation, boolean isLarge) {
        String zoom = isLarge ? STATIC_MAP_ZOOM_LARGE : STATIC_MAP_ZOOM_SMALL;
        String size = isLarge ? STATIC_MAP_SIZE_LARGE : STATIC_MAP_SIZE_SMALL;

        return "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=" + carLocation.getLatitude() + "," + carLocation.getLongitude() +
                "&zoom=" + zoom +
                "&size=" + size + "x" + size +
                "&scale=2" +
                "&markers=color:green%7Csize:normal%7C" + carLocation.getLatitude() + "," + carLocation.getLongitude() +
                "&style=feature:all%7Celement:labels%7Clightness:25";
    }

    public static Intent getShareIntent(CarLocation carLocation, String subject) {
        String uri = "http://maps.google.com/maps?q=" + carLocation.getLatitude() + "," + carLocation.getLongitude();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        return intent;
    }

    public static Intent getStreetViewIntent(CarLocation carLocation) {
        String uri = "google.streetview:cbll=" + carLocation.getLatitude() + "," + carLocation.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }


    public static Intent getNavigateToIntent(CarLocation carLocation) {
        String uri = "google.navigation:q=" + carLocation.getLatitude() + "," + carLocation.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        return intent;
    }

}
