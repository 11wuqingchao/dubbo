package com.alibaba.dubbo.tracing;

import java.io.Serializable;

/**
 * Created by woodle on 17/2/16.
 * 标注
 */
public class Annotation implements Serializable {
    public static final String CLIENT_SEND      = "cs";

    public static final String CLIENT_RECEIVE   = "cr";

    public static final String SERVER_SEND      = "ss";

    public static final String SERVER_RECEIVE   = "sr";

    private Long timestamp;

    private String value;

    private Integer duration;

    private Endpoint host;

    public Annotation(){

    }
    public Annotation(Long timestamp, String value, Endpoint host) {
        this.timestamp = timestamp;
        this.value = value;
        this.host = host;
    }

    public Long getTimestamp() {
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Endpoint getHost() {
        return host;
    }

    public void setHost(Endpoint host) {
        this.host = host;
    }
}
