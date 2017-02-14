package com.alibaba.dubbo.remoting.transport.netty4;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by woodle on 17/2/12.
 *
 */
public class NettyCodecAdapter {

    private final ChannelHandler encoder = new InternalEncoder();

    private final ChannelHandler decoder = new InternalDecoder();

    private final Codec codec;

    private final URL url;

    private final com.alibaba.dubbo.remoting.ChannelHandler handler;

    public NettyCodecAdapter(Codec codec, URL url, com.alibaba.dubbo.remoting.ChannelHandler handler) {
        this.codec = codec;
        this.url = url;
        this.handler = handler;
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    @ChannelHandler.Sharable
    private class InternalEncoder extends MessageToByteEncoder<Object> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            try {
                codec.encode(channel, new ByteBufOutputStream(out), msg);
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            try {
                Object msg = codec.decode(channel, new ByteBufInputStream(input));
                if (msg == Codec.NEED_MORE_INPUT) {
                    return;
                }
                if (msg != null) {
                    out.add(msg);
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }
    }
}
