package com.jjs.miaosha.redis;

/**
 * @author jjs
 * @Version 1.0 2020/4/13
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(Integer expiration, String prefix) {
        super(expiration,prefix);
    }

    public static KeyPrefix getGoodsList = new GoodsKey(60,"gl");
    public static KeyPrefix getGoodsDetail = new GoodsKey(60, "gd");
    public static KeyPrefix getGoodsByID = new GoodsKey(0, "getGoodsByID");

}
