package com.alibaba.dubbo.common;

import java.io.InputStream;

/**
 * Created by woodle on 17/2/12.
 *
 */
public abstract class PositionableInputStream extends InputStream {
    public abstract int position();

    public abstract void position(int newPosition);
}
