package com.alibaba.dubbo.tracing.support;


import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.tracing.Span;
import com.alibaba.dubbo.tracing.TracingCollector;
import com.alibaba.dubbo.tracing.TracingConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

import java.util.List;

/**
 * Created by woodle on 17/2/20.
 *
 */
public class RocketMqTracingCollector implements TracingCollector {

    private DefaultMQProducer defaultMQProducer;

    private Logger logger = LoggerFactory.getLogger(RocketMqTracingCollector.class);

    public RocketMqTracingCollector() {
        defaultMQProducer = new DefaultMQProducer(TracingConstants.ROCKET_MQ_PRODUCER);
        defaultMQProducer.setNamesrvAddr(ConfigUtils.getProperty(TracingConstants.ROCKET_MQ_NAME_SRV_ADD));
        try {
            defaultMQProducer.start();
        } catch (MQClientException e) {
            throw new IllegalArgumentException("fail to start rocket mq producer.",e);
        }
    }

    @Override
    public void push(List<Span> spanList) {
        byte[] bytes = JSON.toJSONBytes(spanList);
        Message message = new Message(TracingConstants.ROCKET_MQ_TOPIC, bytes);
        try {
            SendResult sendResult = defaultMQProducer.send(message);
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                logger.error("send mq message return [" + sendResult.getSendStatus() + "]");
            }
        } catch (Exception e) {
            logger.error("fail to send message.",e);
        }
    }
}
