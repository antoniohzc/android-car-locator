package hzc.antonio.carlocator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CarLocatorAppModule {
    CarLocatorApp app;

    public CarLocatorAppModule(CarLocatorApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return this.app;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(app.getShPrefName(), Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return app.getApplicationContext();
    }
}
