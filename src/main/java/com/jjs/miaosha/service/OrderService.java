package com.jjs.miaosha.service;

import com.jjs.miaosha.dao.OrderDao;
import com.jjs.miaosha.domain.MiaoshaOrder;
import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.domain.OrderInfo;
import com.jjs.miaosha.redis.OrderKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author jjs
 * @Version 1.0 2020/4/19
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
        return redisService.get(OrderKey.orderKey, "" + userId + "_"+ goodsId, MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());

        long insert = orderDao.insert(orderInfo);
        if (insert > 0) {

        }

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());

        int res = orderDao.insertMiaoshaOrder(miaoshaOrder);
        if (res > 0) {

        }

        redisService.set(OrderKey.orderKey, "" + user.getId() +"_"+ goods.getId(), miaoshaOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
