package com.alibaba.dubbo.tracing;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * Created by woodle on 17/2/16.
 *
 */

@SPI("default")
public interface SyncTransfer {

    void start();

    void cancel();

    void syncSend(Span span);

}
