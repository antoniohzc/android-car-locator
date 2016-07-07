package hzc.antonio.carlocator.main;

import org.greenrobot.eventbus.Subscribe;

import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.main.events.MainEvent;
import hzc.antonio.carlocator.main.ui.MainView;

public class MainPresenterImpl implements MainPresenter {
    private MainView view;
    private EventBus eventBus;
    private SessionInteractor sessionInteractor;

    public MainPresenterImpl(MainView view, EventBus eventBus, SessionInteractor sessionInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void logout() {
        sessionInteractor.logout();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {

    }
}
