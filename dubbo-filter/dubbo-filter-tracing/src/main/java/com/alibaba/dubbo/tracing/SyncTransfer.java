package com.alibaba.dubbo.tracing;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * Created by woodle on 17/2/16.
 *
 */

@SPI("default")
public interface SyncTransfer {

    public void start();
    public void cancel();
    public void syncSend(Span span);
}
