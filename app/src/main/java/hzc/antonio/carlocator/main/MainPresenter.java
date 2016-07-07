package hzc.antonio.carlocator.main;

import hzc.antonio.carlocator.main.events.MainEvent;

public interface MainPresenter {
    void onCreate();
    void onDestroy();

    void logout();
    void onEventMainThread(MainEvent event);
}
