package hzc.antonio.carlocator;

import android.app.Application;

import com.firebase.client.Firebase;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import hzc.antonio.carlocator.domain.di.DomainModule;

public class CarLocatorApp extends Application {
    private static final String SHARED_PREFERENCES_NAME = "UserPrefs";
    private static final String SHARED_PREFERENCES_EMAIL_KEY = "email";
    private static final String FIREBASE_URL = "https://android-car-locator-hzc.firebaseio.com/" ;

    private CarLocatorAppModule carLocatorAppModule;
    private DomainModule domainModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
        initFirebase();
        initModules();
    }

    private void initDB() {
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this);
    }

    private void initModules() {
        carLocatorAppModule = new CarLocatorAppModule(this);
        domainModule = new DomainModule(FIREBASE_URL);
    }

    @Override
    public void onTerminate() {
        DBTearDown();
        super.onTerminate();
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    public String getSharedPrefName() {
        return SHARED_PREFERENCES_NAME;
    }

    public String getSharedPrefEmailKey() {
        return SHARED_PREFERENCES_EMAIL_KEY;
    }

    public String getFirebaseUrl() {
        return FIREBASE_URL;
    }
}
