package hzc.antonio.carlocator.main.di;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hzc.antonio.carlocator.domain.FirebaseApi;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.main.MainPresenter;
import hzc.antonio.carlocator.main.MainPresenterImpl;
import hzc.antonio.carlocator.main.MainRepository;
import hzc.antonio.carlocator.main.MainRepositoryImpl;
import hzc.antonio.carlocator.main.SessionInteractor;
import hzc.antonio.carlocator.main.SessionInteractorImpl;
import hzc.antonio.carlocator.main.ui.MainView;
import hzc.antonio.carlocator.main.ui.adapters.MainSectionsPagerAdapter;

@Module
public class MainModule {
    private MainView view;
    private String[] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public MainModule(MainView view, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        this.view = view;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Provides @Singleton
    MainView providesMainView() {
        return this.view;
    }

    @Provides @Singleton
    MainPresenter providesMainPresenter(MainView view, EventBus eventBus, SessionInteractor sessionInteractor) {
        return new MainPresenterImpl(view, eventBus, sessionInteractor);
    }

    @Provides @Singleton
    SessionInteractor providesSessionInteractor(MainRepository repository) {
        return new SessionInteractorImpl(repository);
    }

    @Provides @Singleton
    MainRepository providesMainRepository(EventBus eventBus, FirebaseApi firebaseApi) {
        return new MainRepositoryImpl(eventBus, firebaseApi);
    }


    @Provides @Singleton
    MainSectionsPagerAdapter providesAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
        return new MainSectionsPagerAdapter(fm, titles, fragments);
    }

    @Provides @Singleton
    FragmentManager providesFragmentManager() {
        return this.fragmentManager;
    }

    @Provides @Singleton
    String[] providesStringArrayForAdapter() {
        return this.titles;
    }


    @Provides @Singleton
    Fragment[] providesFragmentArrayForAdapter() {
        return this.fragments;
    }
}
