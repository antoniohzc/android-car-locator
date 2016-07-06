package hzc.antonio.carlocator.domain;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import hzc.antonio.carlocator.entities.CarLocation;

public class FirebaseApi {
    private Firebase firebase;
    private ChildEventListener childEventListener;

    public FirebaseApi(Firebase firebase) {
        this.firebase = firebase;
    }

    // leer datos desde el repositorio
    // y / o
    // por el progressBar
    /*
    public void checkForData(String uid, final FirebaseActionListenerCallback listenerCallback) {
        firebase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listenerCallback.onSuccess();
                }
                else {
                    listenerCallback.onError(null);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }
    */

    public void subscribe(String uid, final FirebaseEventListenerCallback listenerCallback) {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listenerCallback.onChildChanged(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    listenerCallback.onCancelled(firebaseError);
                }
            };

            firebase.child(uid).addChildEventListener(childEventListener);
        }
    }

    public void unsubscribe(String uid) {
        if (childEventListener != null) {
            firebase.removeEventListener(childEventListener);
        }
    }



    public void update(final CarLocation carLocation, final FirebaseActionListenerCallback listenerCallback) {
        String key = carLocation.getTimestamp();
        // String key = carLocation.getUid() // getEmail().replace(".", "_");
        firebase.child(key).setValue(carLocation);
        listenerCallback.onSuccess();

/*        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CarLocation location = dataSnapshot.getValue(CarLocation.class);

                if (location == null) {

                }
                else {
                    userReference.setValue(carLocation);
                    listenerCallback.onSuccess();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });*/
    }

    // Session
    public void checkForSession(FirebaseActionListenerCallback listenerCallback) {
        if (firebase.getAuth() != null) {
            listenerCallback.onSuccess();
        }
        else {
            listenerCallback.onError(null);
        }
    }

    public String getAuthEmail(){
        String email = null;
        if (firebase.getAuth() != null) {
            Map<String, Object> providerData = firebase.getAuth().getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public String getUid() {
        String uid = null;
        if (firebase.getAuth() != null) {
            uid = firebase.getAuth().getUid();
        }
        return uid;
    }

    public void login(String email, String password, final FirebaseActionListenerCallback listenerCallback) {
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                listenerCallback.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void signup(String email, String password, final FirebaseActionListenerCallback listenerCallback) {
        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                listenerCallback.onSuccess();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listenerCallback.onError(firebaseError);
            }
        });
    }

    public void logout() {
        firebase.unauth();
    }

}
