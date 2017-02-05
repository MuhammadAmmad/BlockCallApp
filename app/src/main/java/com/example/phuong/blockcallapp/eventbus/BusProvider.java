package com.example.phuong.blockcallapp.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by phuong on 05/02/2017.
 */

public class BusProvider {
    private static Bus mBus;

    private BusProvider() {
    }

    public static synchronized Bus getInstance() {
        if (mBus == null) {
            mBus = new Bus();
        }
        return mBus;
    }
}
