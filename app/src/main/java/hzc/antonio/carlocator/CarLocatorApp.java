package hzc.antonio.carlocator;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.firebase.client.Firebase;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import hzc.antonio.carlocator.domain.di.DomainModule;
import hzc.antonio.carlocator.libs.di.LibsModule;
import hzc.antonio.carlocator.locationmap.di.DaggerLocationMapComponent;
import hzc.antonio.carlocator.locationmap.di.LocationMapComponent;
import hzc.antonio.carlocator.locationmap.di.LocationMapModule;
import hzc.antonio.carlocator.locationmap.ui.LocationMapView;
import hzc.antonio.carlocator.locationslist.di.DaggerLocationsListComponent;
import hzc.antonio.carlocator.locationslist.di.LocationsListComponent;
import hzc.antonio.carlocator.locationslist.di.LocationsListModule;
import hzc.antonio.carlocator.locationslist.ui.LocationsListView;
import hzc.antonio.carlocator.locationslist.ui.adapters.OnItemClickListener;
import hzc.antonio.carlocator.login.di.DaggerLoginComponent;
import hzc.antonio.carlocator.login.di.LoginComponent;
import hzc.antonio.carlocator.login.di.LoginModule;
import hzc.antonio.carlocator.login.ui.LoginView;
import hzc.antonio.carlocator.main.di.DaggerMainComponent;
import hzc.antonio.carlocator.main.di.MainComponent;
import hzc.antonio.carlocator.main.di.MainModule;
import hzc.antonio.carlocator.main.ui.MainView;

public class CarLocatorApp extends Application {
    private static final String SP_NAME = "UserPrefs";
    private static final String SP_EMAIL_KEY = "email";
    private static final String FIREBASE_URL = "https://android-car-locator-hzc.firebaseio.com/" ;

    private CarLocatorAppModule carLocatorAppModule;
    private DomainModule domainModule;
    private LibsModule libsModule;

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
        libsModule = new LibsModule(this);
    }

    @Override
    public void onTerminate() {
        DBTearDown();
        super.onTerminate();
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    public String getShPrefName() {
        return SP_NAME;
    }

    public String getShPrefEmailKey() {
        return SP_EMAIL_KEY;
    }

    public LoginComponent getLoginComponent(LoginView view) {
        return DaggerLoginComponent.builder()
                .carLocatorAppModule(carLocatorAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .loginModule(new LoginModule(view))
                .build();
    }

    public MainComponent getMainComponent(MainView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        return DaggerMainComponent.builder()
                .carLocatorAppModule(carLocatorAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .mainModule(new MainModule(view, titles, fragments, fragmentManager))
                .build();
    }

    public LocationMapComponent getLocationMapComponent(LocationMapView view) {
        return DaggerLocationMapComponent.builder()
                .carLocatorAppModule(carLocatorAppModule)
                .libsModule(libsModule)
                .locationMapModule(new LocationMapModule(view))
                .build();
    }

    public LocationsListComponent getLocationsListComponent(LocationsListView view, OnItemClickListener onItemClickListener) {
        return DaggerLocationsListComponent.builder()
                .carLocatorAppModule(carLocatorAppModule)
                .domainModule(domainModule)
                .libsModule(libsModule)
                .locationsListModule(new LocationsListModule(view, onItemClickListener))
                .build();
    }
}
