package com.alibaba.dubbo.tracing;

/**
 * Created by woodle on 17/2/16.
 *
 */
public class Configuration {

    /**
     * 一批发送多少条消息到收集端
     */
    private int flushSize;

    /**
     * 缓冲队列大小
     */
    private int queueSize;

    public Integer getFlushSize() {
        return flushSize;
    }

    public void setFlushSize(int flushSize) {
        this.flushSize = flushSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

}
