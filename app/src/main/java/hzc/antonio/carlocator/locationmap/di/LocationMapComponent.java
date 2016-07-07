package hzc.antonio.carlocator.locationmap.di;

import javax.inject.Singleton;

import dagger.Component;
import hzc.antonio.carlocator.CarLocatorAppModule;
import hzc.antonio.carlocator.libs.di.LibsModule;
import hzc.antonio.carlocator.locationmap.ui.LocationMapFragment;

@Singleton
@Component(modules = {LocationMapModule.class, LibsModule.class, CarLocatorAppModule.class})
public interface LocationMapComponent {
    void inject(LocationMapFragment target);
}
