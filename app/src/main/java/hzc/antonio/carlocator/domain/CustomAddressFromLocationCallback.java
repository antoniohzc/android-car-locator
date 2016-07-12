package hzc.antonio.carlocator.domain;

import hzc.antonio.carlocator.entities.CustomAddress;

public interface CustomAddressFromLocationCallback {
    void onCompleted(CustomAddress address);
}
