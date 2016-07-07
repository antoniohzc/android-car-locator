package hzc.antonio.carlocator.libs;

import hzc.antonio.carlocator.libs.base.EventBus;

public class GreenRobotEventBusSingleton implements EventBus {
    org.greenrobot.eventbus.EventBus eventBus;

    private GreenRobotEventBusSingleton() {
        this.eventBus = org.greenrobot.eventbus.EventBus.getDefault();
    }

    private static class SingletonHolder {
        private static final GreenRobotEventBusSingleton INSTANCE = new GreenRobotEventBusSingleton();
    }

    public static GreenRobotEventBusSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }


    @Override
    public void register(Object suscriber) {
        eventBus.register(suscriber);
    }

    @Override
    public void unregister(Object suscriber) {
        eventBus.unregister(suscriber);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }
}
