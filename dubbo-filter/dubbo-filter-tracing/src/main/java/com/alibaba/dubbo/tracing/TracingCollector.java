package com.alibaba.dubbo.tracing;

import java.util.List;

/**
 * Created by woodle on 17/2/16.
 *
 */
public interface TracingCollector {

    public void push(List<Span> spanList);

}
