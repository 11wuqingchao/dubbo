package com.alibaba.dubbo.tracing.support;

import com.alibaba.dubbo.tracing.TracingCollector;
import com.alibaba.dubbo.tracing.TracingCollectorFactory;

/**
 * Created by woodle on 17/2/20.
 *
 */
public class RocketMqTracingCollectorFactory implements TracingCollectorFactory {

    @Override
    public TracingCollector getTracingCollector() {
        return new RocketMqTracingCollector();
    }
}
