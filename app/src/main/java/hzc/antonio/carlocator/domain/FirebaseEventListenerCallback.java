package hzc.antonio.carlocator.domain;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public interface FirebaseEventListenerCallback {
    void onChildAdded(DataSnapshot snapshot);
    void onChildChanged(DataSnapshot snapshot);
    void onCancelled(FirebaseError error);
}
