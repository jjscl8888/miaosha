package com.jjs.miaosha.util;

import com.alibaba.fastjson.JSON;
import com.jjs.miaosha.result.CodeMsg;
import com.jjs.miaosha.result.Result;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jjs
 * @Version 1.0 2020/5/3
 */
public class WebUtils {

    public static void render(HttpServletResponse response, CodeMsg serverError) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(JSON.toJSONBytes(Result.error(serverError)));
        out.flush();
        out.close();
    }
}
