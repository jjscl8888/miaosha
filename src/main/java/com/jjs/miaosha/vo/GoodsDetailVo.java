package com.jjs.miaosha.vo;

import com.jjs.miaosha.domain.MiaoshaUser;

/**
 * @author jjs
 * @Version 1.0 2020/5/1
 */
public class GoodsDetailVo {
    private GoodsVo goods;
    private MiaoshaUser user;
    private Integer miaoshaStatus;
    private Integer remainSeconds;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public Integer isMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(Integer miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public Integer getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(Integer remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
}
