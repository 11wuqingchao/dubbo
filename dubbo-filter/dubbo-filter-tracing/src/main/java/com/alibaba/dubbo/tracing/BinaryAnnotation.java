package com.alibaba.dubbo.tracing;

import java.io.Serializable;

/**
 * Created by woodle on 17/2/16.
 * key-value映射的标注
 */
public class BinaryAnnotation implements Serializable {

    private static final long serialVersionUID = 6344383711155625186L;

    private String key;

    private String value;

    private String type;

    private Integer duration;

    private Endpoint host;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "BinaryAnnotation{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", host=" + host +
                '}';
    }
}
