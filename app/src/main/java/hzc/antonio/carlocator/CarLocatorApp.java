package hzc.antonio.carlocator;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.firebase.client.Firebase;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import hzc.antonio.carlocator.domain.di.DomainModule;
import hzc.antonio.carlocator.libs.di.LibsModule;
import hzc.antonio.carlocator.login.di.DaggerLoginComponent;
import hzc.antonio.carlocator.login.di.LoginComponent;
import hzc.antonio.carlocator.login.di.LoginModule;
import hzc.antonio.carlocator.login.ui.LoginView;
import hzc.antonio.carlocator.main.di.DaggerMainComponent;
import hzc.antonio.carlocator.main.di.MainComponent;
import hzc.antonio.carlocator.main.di.MainModule;
import hzc.antonio.carlocator.main.ui.MainView;

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

    public LoginComponent getLoginComponent(LoginView view) {
        return DaggerLoginComponent.builder()
                .carLocatorAppModule(carLocatorAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(this))
                .loginModule(new LoginModule(view))
                .build();
    }

    public MainComponent getMainComponent(MainView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        return DaggerMainComponent.builder()
                .carLocatorAppModule(carLocatorAppModule)
                .domainModule(domainModule)
                .libsModule(new LibsModule(this))
                .mainModule(new MainModule(view, titles, fragments, fragmentManager))
                .build();
    }
}
