package hzc.antonio.carlocator.locationslist.di;

import javax.inject.Singleton;

import dagger.Component;
import hzc.antonio.carlocator.CarLocatorAppModule;
import hzc.antonio.carlocator.domain.di.DomainModule;
import hzc.antonio.carlocator.libs.di.LibsModule;
import hzc.antonio.carlocator.locationslist.ui.LocationsListFragment;

@Singleton
@Component(modules = {LocationsListModule.class, DomainModule.class, LibsModule.class, CarLocatorAppModule.class})
public interface LocationsListComponent {
    void inject(LocationsListFragment target);
}
