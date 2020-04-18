package com.jjs.miaosha.redis;

/**
 * @author jjs
 * @Version 1.0 2020/4/13
 */
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        // 默认0为永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String name = getClass().getSimpleName();
        return name + ":" + prefix;
    }
}
