package com.jjs.miaosha.qmq.entity;

import com.jjs.miaosha.domain.MiaoshaUser;

/**
 * @author jjs
 * @Version 1.0 2020/5/3
 */
public class MiaoShaEntity {
    private MiaoshaUser user;
    private Long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
