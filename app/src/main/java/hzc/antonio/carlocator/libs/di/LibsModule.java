package hzc.antonio.carlocator.libs.di;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.libs.GlideImageLoader;
import hzc.antonio.carlocator.libs.GreenRobotEventBus;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.libs.base.ImageLoader;

@Module
public class LibsModule {
    private Context context;

    public LibsModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager) {
        return new GlideImageLoader(requestManager);
    }

    @Provides @Singleton
    RequestManager providesRequestManager(Context context) {
        return Glide.with(context);
    }

    @Provides @Singleton
    EventBus providesEventBus(org.greenrobot.eventbus.EventBus eventBus) {
        return new GreenRobotEventBus(eventBus);
    }

    @Provides @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus() {
        return org.greenrobot.eventbus.EventBus.getDefault();
    }
}
