package com.jjs.miaosha.util;

import java.util.UUID;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
