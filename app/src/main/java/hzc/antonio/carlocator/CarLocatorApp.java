package hzc.antonio.carlocator;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class CarLocatorApp extends Application {

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

    }

    private void initModules() {

    }

    @Override
    public void onTerminate() {
        DBTearDown();
        super.onTerminate();
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }
}
