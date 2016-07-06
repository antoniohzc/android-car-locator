package hzc.antonio.carlocator.login;

import hzc.antonio.carlocator.login.events.LoginEvent;

public interface LoginPresenter {
    void onCreate();
    void onDestroy();

    void validateLogin(String email, String password);
    void registerNewUser(String email, String password);

    void onEventMainThread(LoginEvent event);
}
