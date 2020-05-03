package com.jjs.miaosha.vo;

import com.jjs.miaosha.domain.OrderInfo;

/**
 * @author jjs
 * @Version 1.0 2020/5/1
 */
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
