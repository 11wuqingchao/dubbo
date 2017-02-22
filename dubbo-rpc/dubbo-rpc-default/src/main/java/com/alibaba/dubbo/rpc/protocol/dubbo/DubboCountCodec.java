/*
 * Copyright 1999-2011 Alibaba Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alibaba.dubbo.rpc.protocol.dubbo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.PositionableInputStream;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.Codec;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.remoting.exchange.support.MultiMessage;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public final class DubboCountCodec implements Codec {

    private DubboCodec codec = new DubboCodec();

    public void encode(Channel channel, OutputStream os, Object msg) throws IOException {
        codec.encode(channel, os, msg);
    }

    public Object decode(Channel channel, InputStream is) throws IOException {
        PositionableInputStream pis = (PositionableInputStream) is;
        int beginIdx = pis.position();
        MultiMessage result = MultiMessage.create();
        do {
            Object obj = codec.decode(channel, pis);
            if (NEED_MORE_INPUT == obj) {
                pis.position(beginIdx);
                break;
            } else {
                result.addMessage(obj);
                logMessageLength(obj, pis.position() - beginIdx);
                beginIdx = pis.position();
            }
        } while (true);
        if (result.isEmpty()) {
            return NEED_MORE_INPUT;
        }
        if (result.size() == 1) {
            return result.get(0);
        }
        return result;
    }

    private void logMessageLength(Object result, int bytes) {
        if (bytes <= 0) {
            return;
        }
        if (result instanceof Request) {
            try {
                /**
                 * 将请求参数的大小在RpcResult里放了一份，以用来监控
                 * add by woodle
                 */
                ((RpcInvocation) ((Request) result).getData()).setAttachment(
                    Constants.INPUT_KEY, String.valueOf(bytes));
            } catch (Throwable e) {
                // do nothing
            }
        }
        if (result instanceof Response) {
            try {
                /**
                 * 将接口的返回结果的大小在RpcResult里放了一份，以用来监控
                 * add by woodle
                 */
                ((RpcResult) ((Response) result).getResult()).setAttachment(
                        Constants.OUTPUT_KEY, String.valueOf(bytes));
            } catch (Throwable e) {
                // do nothing
            }
        }
    }

}
