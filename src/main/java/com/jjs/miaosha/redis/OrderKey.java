package com.jjs.miaosha.redis;

/**
 * @author jjs
 * @Version 1.0 2020/4/13
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
