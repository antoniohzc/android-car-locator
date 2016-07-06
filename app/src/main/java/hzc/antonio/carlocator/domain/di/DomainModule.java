package hzc.antonio.carlocator.domain.di;

import android.content.Context;
import android.location.Geocoder;

import com.firebase.client.Firebase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.domain.FirebaseApi;
import hzc.antonio.carlocator.domain.Util;

@Module
public class DomainModule {
    String firebaseUrl;

    public DomainModule(String firebaseUrl) {
        this.firebaseUrl = firebaseUrl;
    }

    @Provides
    @Singleton
    FirebaseApi providesFirebaseApi(Firebase firebase) {
        return new FirebaseApi(firebase);
    }

    @Provides
    @Singleton
    Firebase providesFirebase(String firebaseUrl) {
        return new Firebase(firebaseUrl);
    }

    @Provides
    @Singleton
    String providesFirebaseUrl() {
        return this.firebaseUrl;
    }

    @Provides
    @Singleton
    Util providesUtil(Geocoder geocoder) {
        return new Util(geocoder);
    }

    @Provides
    @Singleton
    Geocoder providesGeocoder(Context context) {
        return new Geocoder(context);
    }
}
