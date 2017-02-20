package com.alibaba.dubbo.tracing;

import java.util.List;

/**
 * Created by woodle on 17/2/16.
 * 追踪日志收集
 */
public interface TracingCollector {

    void push(List<Span> spanList);

}
