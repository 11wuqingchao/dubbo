package com.alibaba.dubbo.tracing.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.tracing.ContextHolder;
import com.alibaba.dubbo.tracing.Tracer;
import com.alibaba.dubbo.tracing.TracingCollector;


/**
 * Created by woodle on 17/2/16.
 *
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class TracingFilter implements Filter {

    private Tracer tracer = new Tracer();

    public TracingFilter(){
        tracer.init();
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (invoker.getInterface() == TracingCollector.class) {
            return invoker.invoke(invocation);
        }
        boolean isConsumerSide = isConsumerSide();
        try {
            tracer.beforeInvoke(isConsumerSide);
            Result result = invoker.invoke(invocation);
            if (result.hasException()) {
                tracer.addException(result.getException());
            }
            return result;
        } catch (RpcException e) {
            tracer.addException(e);
            throw e;
        } finally {
            tracer.afterInvoke(isConsumerSide);
            ContextHolder.removeAll();
        }
    }

    private boolean isConsumerSide(){
        URL url =  RpcContext.getContext().getUrl();
        return Constants.CONSUMER_SIDE.equals(url.getParameter(Constants.SIDE_KEY));
    }

}
