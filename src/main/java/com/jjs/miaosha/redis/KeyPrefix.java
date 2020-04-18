package com.jjs.miaosha.redis;

public interface KeyPrefix {
    int expireSeconds();

    String getPrefix();
}
