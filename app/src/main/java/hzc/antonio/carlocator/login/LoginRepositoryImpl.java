package hzc.antonio.carlocator.login;

import com.firebase.client.FirebaseError;

import hzc.antonio.carlocator.domain.FirebaseActionListenerCallback;
import hzc.antonio.carlocator.domain.FirebaseApi;
import hzc.antonio.carlocator.libs.base.EventBus;
import hzc.antonio.carlocator.login.events.LoginEvent;

public class LoginRepositoryImpl implements LoginRepository {
    private EventBus eventBus;
    private FirebaseApi firebaseApi;

    public LoginRepositoryImpl(EventBus eventBus, FirebaseApi firebaseApi) {
        this.eventBus = eventBus;
        this.firebaseApi = firebaseApi;
    }

    @Override
    public void signIn(String email, String password) {
        // Validate login
        if (email != null && password != null) {
            firebaseApi.login(email, password, new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseApi.getAuthEmail();
                    post(LoginEvent.ON_SIGN_IN_SUCCESS, email);
                }

                @Override
                public void onError(FirebaseError error) {
                    post(LoginEvent.ON_SIGN_IN_ERROR, error.getMessage(), null);
                }
            });
        }
        // Check for session
        else {
            firebaseApi.checkForSession(new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    String email = firebaseApi.getAuthEmail();
                    post(LoginEvent.ON_SIGN_IN_SUCCESS, email);
                }

                @Override
                public void onError(FirebaseError error) {
                    post(LoginEvent.ON_FAILED_TO_RECOVER_SESSION);
                }
            });
        }
    }

    @Override
    public void signUp(final String email, final String password) {
        firebaseApi.signup(email, password, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(LoginEvent.ON_SIGN_UP_SUCCESS);
                signIn(email, password);
            }

            @Override
            public void onError(FirebaseError error) {
                post(LoginEvent.ON_SIGN_UP_ERROR, error.getMessage(), null);
            }
        });
    }


    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String currentUserEmail) {
        post(type, null, currentUserEmail);
    }

    private void post(int type, String errorMessage, String currentUserEmail) {
        LoginEvent event = new LoginEvent();
        event.setEventType(type);
        event.setCurrentUserEmail(currentUserEmail);
        event.setErrorMessage(errorMessage);

        eventBus.post(event);
    }
}
