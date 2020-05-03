package com.jjs.miaosha.redis;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
public class MiaoShaUserKey extends BasePrefix{
    public static final int TOKEN_EXPIRE = 3600*24*2;

    private MiaoShaUserKey(String prefix, int expireSeconds) {
        super(expireSeconds, prefix);
    }

    public static MiaoShaUserKey token = new MiaoShaUserKey("tk", TOKEN_EXPIRE);
    public static MiaoShaUserKey GetUserById = new MiaoShaUserKey("id", 0);

    public static MiaoShaUserKey path = new MiaoShaUserKey("path", 60);

    public static MiaoShaUserKey code = new MiaoShaUserKey("code", 60*10);
}
