package hzc.antonio.carlocator.login.di;

import javax.inject.Singleton;

import dagger.Component;
import hzc.antonio.carlocator.CarLocatorAppModule;
import hzc.antonio.carlocator.domain.di.DomainModule;
import hzc.antonio.carlocator.libs.di.LibsModule;
import hzc.antonio.carlocator.login.ui.LoginActivity;

@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, CarLocatorAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
