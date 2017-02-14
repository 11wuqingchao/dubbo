package com.alibaba.dubbo.remoting.transport.netty4;

import com.alibaba.dubbo.common.PositionableInputStream;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by woodle on 17/2/12.
 *
 */
public class ByteBufInputStream extends PositionableInputStream {

    private final ByteBuf buffer;

    private int mark;

    public ByteBufInputStream(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public int position() {
        return buffer.readerIndex();
    }

    @Override
    public void position(int newPosition) {
        buffer.readerIndex(newPosition);
    }

    @Override
    public int read() throws IOException {
        return buffer.readInt();
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (b == null)
            throw new NullPointerException();

        if (off < 0 || len < 0 || len > b.length - off)
            throw new IndexOutOfBoundsException();

        int readerIndex = buffer.readerIndex();
        int writerIndex = buffer.writerIndex();
        if (readerIndex >= writerIndex)
            return -1;

        if (readerIndex + len > writerIndex)
            len = writerIndex - readerIndex;

        if (len <= 0)
            return 0;

        buffer.readBytes(b, off, len);
        return len;
    }

    @Override
    public int available() {
        return buffer.readableBytes();
    }

    @Override
    public long skip(long skip) {
        int readerIndex = buffer.readerIndex();
        int writerIndex = buffer.writerIndex();
        if (readerIndex + skip > writerIndex)
            skip = writerIndex - readerIndex;

        if (skip <= 0)
            return 0;
        buffer.skipBytes((int) skip);
        return skip;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) {
        mark = readAheadLimit;
    }

    @Override
    public void reset() {
        buffer.readerIndex(mark);
    }

}
