package hzc.antonio.carlocator.login.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.domain.FirebaseApi;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.login.LoginInteractor;
import hzc.antonio.carlocator.login.LoginInteractorImpl;
import hzc.antonio.carlocator.login.LoginPresenter;
import hzc.antonio.carlocator.login.LoginPresenterImpl;
import hzc.antonio.carlocator.login.LoginRepository;
import hzc.antonio.carlocator.login.LoginRepositoryImpl;
import hzc.antonio.carlocator.login.ui.LoginView;

@Module
public class LoginModule {
    LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides @Singleton
    LoginView providesLoginView() {
        return this.view;
    }

    @Provides @Singleton
    LoginPresenter providesLoginPresenter(EventBus eventBus, LoginView loginView, LoginInteractor loginInteractor) {
        return new LoginPresenterImpl(eventBus, loginView, loginInteractor);
    }

    @Provides @Singleton
    LoginInteractor providesLoginInteractor(LoginRepository repository) {
        return new LoginInteractorImpl(repository);
    }

    @Provides @Singleton
    LoginRepository providesLoginRepository(EventBus eventBus, FirebaseApi firebaseApi) {
        return new LoginRepositoryImpl(eventBus, firebaseApi);
    }
}
