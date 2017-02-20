package com.alibaba.dubbo.tracing;

import java.io.Serializable;

/**
 * Created by woodle on 17/2/16.
 * 标注 文本Annotation
 */
public class Annotation implements Serializable {
    public static final String CLIENT_SEND      = "cs";

    public static final String CLIENT_RECEIVE   = "cr";

    public static final String SERVER_SEND      = "ss";

    public static final String SERVER_RECEIVE   = "sr";

    private long timestamp;

    private String value;

    private int duration;

    private Endpoint host;

    public Annotation(){

    }
    public Annotation(long timestamp, String value, Endpoint host) {
        this.timestamp = timestamp;
        this.value = value;
        this.host = host;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Endpoint getHost() {
        return host;
    }

    public void setHost(Endpoint host) {
        this.host = host;
    }
}
