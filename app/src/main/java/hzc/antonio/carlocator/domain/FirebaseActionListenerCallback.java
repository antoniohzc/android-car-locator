package hzc.antonio.carlocator.domain;

import com.firebase.client.FirebaseError;

public interface FirebaseActionListenerCallback {
    void onSuccess();
    void onError(FirebaseError error);
}
