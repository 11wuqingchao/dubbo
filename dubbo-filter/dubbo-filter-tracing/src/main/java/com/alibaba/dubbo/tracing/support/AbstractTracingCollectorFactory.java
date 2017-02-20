package com.alibaba.dubbo.tracing.support;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.support.AbstractRegistryFactory;
import com.alibaba.dubbo.tracing.TracingCollector;
import com.alibaba.dubbo.tracing.TracingCollectorFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by woodle on 17/2/17.
 *
 */
public abstract class AbstractTracingCollectorFactory implements TracingCollectorFactory {

    @Override
    public TracingCollector getTracingCollector() {
        Collection<Registry> registries =  AbstractRegistryFactory.getRegistries();
        List<URL> urls = new ArrayList<URL>();
        for (Registry registry:registries) {
            URL url = registry.getUrl();
            String protocolName = url.getProtocol();
            url = url.setProtocol(Constants.REGISTRY_PROTOCOL);
            url = url.addParameter(Constants.REGISTRY_KEY, protocolName);
            url = url.setPath(TracingCollector.class.getName());
            url = url.addParameter(Constants.INTERFACE_KEY, TracingCollector.class.getName());
            url = url.addParameter(Constants.REFERENCE_FILTER_KEY, "-tracing");
            urls.add(url);
        }
        return createTracingCollector(urls);
    }

    /**
     *
     * @param urls registry://ip:port/
     */
    protected abstract TracingCollector createTracingCollector(List<URL> urls);
}
