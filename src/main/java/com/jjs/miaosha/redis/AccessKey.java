package com.jjs.miaosha.redis;

/**
 * @author jjs
 * @Version 1.0 2020/4/13
 */
public class AccessKey extends BasePrefix {

    private AccessKey(Integer expiration, String prefix) {
        super(expiration,prefix);
    }

    public static KeyPrefix accessKey(Integer expiration) {
       return new AccessKey(expiration,"access");
    }

}
