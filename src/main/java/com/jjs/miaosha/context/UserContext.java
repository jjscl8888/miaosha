package com.jjs.miaosha.context;

import com.jjs.miaosha.domain.MiaoshaUser;

/**
 * @author jjs
 * @Version 1.0 2020/5/3
 */
public class UserContext {

    private static ThreadLocal<MiaoshaUser> threadLocal = new ThreadLocal<>();

    public static void setUser(MiaoshaUser user) {
        threadLocal.set(user);
    }

    public static MiaoshaUser getUser() {
        return threadLocal.get();
    }
}
