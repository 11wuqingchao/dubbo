package com.alibaba.dubbo.remoting.exchange.support;

import java.util.concurrent.Future;

/**
 * Created by woodle on 17/2/12.
 *
 */
public abstract class ListenableFuture<V> implements Future<V> {
    private volatile Runnable runnable;

    public void then(Runnable runnable) {
        this.runnable = runnable;
        trigger();
    }

    protected final void trigger() {
        if (isDone() && runnable != null) {
            runnable.run();
        }
    }
}
