package com.jjs.miaosha.qmq;

import com.jjs.miaosha.domain.MiaoshaOrder;
import com.jjs.miaosha.domain.OrderInfo;
import com.jjs.miaosha.qmq.entity.MiaoShaEntity;
import com.jjs.miaosha.redis.OrderKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.service.GoodsService;
import com.jjs.miaosha.service.MiaoshaService;
import com.jjs.miaosha.service.OrderService;
import com.jjs.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jjs
 * @Version 1.0 2020/5/2
 */
@Component
public class QmqReciever {

    private static final Logger logger = LoggerFactory.getLogger(QmqReciever.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;


    @RabbitListener(queues = QmqConfig.TOPIC)
    public void recieve(String msg) {
        logger.info("recieve : " + msg);

            MiaoShaEntity miaoShaEntity = RedisService.stringToBean(msg, MiaoShaEntity.class);
            if (miaoShaEntity == null ||
                    miaoShaEntity.getUser() == null ||
                    miaoShaEntity.getGoodsId() == null ||
                    miaoShaEntity.getUser().getId() == null
                    ) {
                return;
            }
            GoodsVo goods = goodsService.getGoodsVoByGoodsId(miaoShaEntity.getGoodsId());
            Integer stock = goods.getStockCount();
            if (stock <= 0) {
                return;
            }
            MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(miaoShaEntity.getUser().getId(), miaoShaEntity.getGoodsId());
            if (order != null) {
                return;
            }
        try {
            OrderInfo orderInfo = miaoshaService.miaosha(miaoShaEntity.getUser(), goods);
            if (orderInfo != null) {
                redisService.set(OrderKey.orderCreateKey, "" + miaoShaEntity.getUser().getId() + "_" + miaoShaEntity.getGoodsId(), orderInfo.getId());
            } else {
                redisService.set(OrderKey.orderCreateKey, "" + miaoShaEntity.getUser().getId() + "_" + miaoShaEntity.getGoodsId(), -1);
            }
        } catch (Exception ex) {
            redisService.set(OrderKey.orderCreateKey, "" + miaoShaEntity.getUser().getId() + "_" + miaoShaEntity.getGoodsId(), -1);
        }
    }
}
