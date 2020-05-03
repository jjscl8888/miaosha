package com.jjs.miaosha.controller;

import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.domain.OrderInfo;
import com.jjs.miaosha.exception.GlobalException;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.result.CodeMsg;
import com.jjs.miaosha.result.Result;
import com.jjs.miaosha.service.GoodsService;
import com.jjs.miaosha.service.OrderService;
import com.jjs.miaosha.vo.GoodsVo;
import com.jjs.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jjs
 * @Version 1.0 2020/5/1
 */
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("detail")
    public Result<OrderDetailVo> detail(MiaoshaUser user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        Long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoods(goodsVo);
        orderDetailVo.setOrder(orderInfo);
        return Result.success(orderDetailVo);
    }
}
