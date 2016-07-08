package hzc.antonio.carlocator.libs.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.libs.GreenRobotEventBus;
import hzc.antonio.carlocator.libs.base.EventBus;

@Module
public class LibsModule {
    private Context context;

    public LibsModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus) {
        return new GreenRobotEventBus(eventBus);
    }

    @Provides @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus() {
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

//    @Provides @Singleton
//    Context providesContext() {
//        return this.context;
//    }
}
