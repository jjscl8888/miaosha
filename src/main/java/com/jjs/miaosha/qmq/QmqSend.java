package com.jjs.miaosha.qmq;

import com.jjs.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jjs
 * @Version 1.0 2020/5/2
 */
@Component
public class QmqSend {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(QmqSend.class);

    public void send(Object msg) {
        String res = RedisService.beanToString(msg);
        logger.info("send start :" + res);
        rabbitTemplate.convertAndSend(QmqConfig.TOPIC, res);
        logger.info("send end!");
    }
}
