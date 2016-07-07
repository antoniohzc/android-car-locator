package hzc.antonio.carlocator.main;

import hzc.antonio.carlocator.domain.FirebaseApi;
import hzc.antonio.carlocator.libs.base.EventBus;

public class MainRepositoryImpl implements MainRepository {
    private EventBus eventBus;
    private FirebaseApi firebaseApi;

    public MainRepositoryImpl(EventBus eventBus, FirebaseApi firebaseApi) {
        this.eventBus = eventBus;
        this.firebaseApi = firebaseApi;
    }

    @Override
    public void logout() {
        firebaseApi.logout();
    }
}
