package hzc.antonio.carlocator.domain.di;

import javax.inject.Singleton;

import dagger.Component;
import hzc.antonio.carlocator.CarLocatorAppModule;

@Singleton
@Component(modules = {DomainModule.class, CarLocatorAppModule.class})
public interface DomainComponent {
}
