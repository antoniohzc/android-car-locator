package hzc.antonio.carlocator.login;

import org.greenrobot.eventbus.Subscribe;

import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.login.events.LoginEvent;
import hzc.antonio.carlocator.login.ui.LoginView;

public class LoginPresenterImpl implements LoginPresenter{
    private EventBus eventBus;
    private LoginView view;
    private LoginInteractor interactor;

    public LoginPresenterImpl(EventBus eventBus, LoginView view, LoginInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
        this.interactor = interactor;
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
    public void validateLogin(String email, String password) {
        if (view != null) {
            view.disableInputs();
            view.showProgress();
        }
        interactor.doSignIn(email, password);
    }

    @Override
    public void registerNewUser(String email, String password) {
        if (view != null) {
            view.enableInputs();
            view.hideProgress();
        }
        interactor.doSignUp(email, password);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()) {
            case LoginEvent.ON_SIGN_IN_SUCCESS:
                onSignInSuccess(event.getCurrentUserEmail());
                break;
            case LoginEvent.ON_SIGN_UP_SUCCESS:
                onSignUpSuccess();
                break;
            case LoginEvent.ON_SIGN_IN_ERROR:
                onSignInError(event.getErrorMessage());
                break;
            case LoginEvent.ON_SIGN_UP_ERROR:
                onSignUpError(event.getErrorMessage());
                break;
            case LoginEvent.ON_FAILED_TO_RECOVER_SESSION:
                onFailedToRecoverSession();
                break;
        }
    }


    private void onSignInSuccess(String email) {
        if (view != null) {
            view.setUserEmail(email);
            view.navigateToMainScreen();
        }
    }

    private void onSignUpSuccess() {
        if (view != null) {
            view.newUserSuccess();
        }
    }

    private void onSignInError(String error) {
        if (view != null) {
            view.hideProgress();
            view.enableInputs();
            view.loginError(error);
        }
    }

    private void onSignUpError(String error) {
        if (view != null) {
            view.hideProgress();
            view.enableInputs();
            view.newUserError(error);
        }
    }

    private void onFailedToRecoverSession() {
        if (view != null) {
            view.hideProgress();
            view.enableInputs();
        }
    }
}
