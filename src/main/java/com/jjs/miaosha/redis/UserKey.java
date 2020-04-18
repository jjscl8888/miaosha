package com.jjs.miaosha.redis;

/**
 * @author jjs
 * @Version 1.0 2020/4/13
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static KeyPrefix getById = new UserKey("id");
    public static KeyPrefix getByName = new UserKey("name");

}
