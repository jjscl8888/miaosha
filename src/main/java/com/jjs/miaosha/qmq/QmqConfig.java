package com.jjs.miaosha.qmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

;


/**
 * @author jjs
 * @Version 1.0 2020/5/2
 */
@Configuration
public class QmqConfig {
    public static final String TOPIC = "queue";

    @Bean
    public Queue getQueue() {
        return new Queue(TOPIC, true);
    }

}
