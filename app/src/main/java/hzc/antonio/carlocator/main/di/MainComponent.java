package hzc.antonio.carlocator.main.di;

import javax.inject.Singleton;

import dagger.Component;
import hzc.antonio.carlocator.CarLocatorAppModule;
import hzc.antonio.carlocator.domain.di.DomainModule;
import hzc.antonio.carlocator.libs.di.LibsModule;
import hzc.antonio.carlocator.main.ui.MainActivity;

@Singleton
@Component(modules = {MainModule.class, DomainModule.class, LibsModule.class, CarLocatorAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
