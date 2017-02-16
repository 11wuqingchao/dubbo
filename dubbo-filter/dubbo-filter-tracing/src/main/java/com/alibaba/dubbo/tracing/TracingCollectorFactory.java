package com.alibaba.dubbo.tracing;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * Created by woodle on 17/2/16.
 *
 */
@SPI(TracingConstants.DEFAULT_COLLECTOR_TYPE)
public interface TracingCollectorFactory {

    /**
     * 监控链路的数据同步器
     *
     */
    public TracingCollector getTracingCollector();

}
