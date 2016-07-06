package hzc.antonio.carlocator.libs.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LibsModule.class, CarLocatorAppModule.class})
public interface LibsComponent {
}
