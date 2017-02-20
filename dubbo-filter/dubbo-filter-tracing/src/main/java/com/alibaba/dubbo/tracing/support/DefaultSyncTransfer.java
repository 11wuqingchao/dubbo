package com.alibaba.dubbo.tracing.support;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.tracing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by woodle on 17/2/19.
 *
 */
public class DefaultSyncTransfer implements SyncTransfer {
    private static final Logger log = LoggerFactory.getLogger(DefaultSyncTransfer.class);

    private Protocol protocol;

    private volatile TracingCollector collector;

    private volatile BlockingQueue<Span> queue;

    private volatile TransferTask transferTask;

    private volatile boolean initialized = false;

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    private class TransferTask extends Thread {
        private List<Span> cacheList;
        private int flushSizeInner;

        private TransferTask(int flushSize) {
            cacheList = new ArrayList<Span>();
            flushSizeInner = flushSize;
            setName("Tracing-span-transfer-task-thread");
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    Span first = queue.take();
                    cacheList.add(first);
                    queue.drainTo(cacheList, flushSizeInner);
                    if (cacheList.size() <=0 ){
                        continue;
                    }
                    if (!initialized && collector == null) {
                        String collectorName = ConfigUtils.getProperty(TracingConstants.TRACING_COLLECTOR, TracingConstants.DEFAULT_COLLECTOR_TYPE);
                        TracingCollectorFactory tracingCollectorFactory = ExtensionLoader.getExtensionLoader(TracingCollectorFactory.class)
                                .getExtension(collectorName);
                        collector = tracingCollectorFactory.getTracingCollector();
                        initialized = true;
                    }
                    collector.push(cacheList);
                    cacheList.clear();
                } catch (InterruptedException e) {
                    log.error("Dst-span-transfer-task-thread occur an error", e);
                }
            }
        }
    }

    public DefaultSyncTransfer() {
        queue = new ArrayBlockingQueue<Span>(Integer.parseInt(ConfigUtils.getProperty(TracingConstants.FLUSH_SIZE_KEY,
                TracingConstants.DEFAULT_FLUSH_SIZE)));
        transferTask = new TransferTask(Integer.parseInt(ConfigUtils.getProperty(TracingConstants.QUEUE_SIZE_KEY,
                TracingConstants.DEFAULT_BUFFER_QUEUE_SIZE)));
    }


    public void start() {
        transferTask.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                cancel();
            }
        });
    }

    public void cancel() {
        transferTask.interrupt();
    }

    public void syncSend(Span span) {
        try {
            queue.add(span);
        } catch (Exception e) {
            log.error("span : ignore ..", e);
        }
    }
}
