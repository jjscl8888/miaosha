package com.jjs.miaosha.result;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
public class CodeMsg {

    private int code;
    private String msg;

    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常: %s");

    //登陆模块
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"密码不能为空");
    public static CodeMsg MOBILE_FORMAT_ERROR = new CodeMsg(500212,"手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500213,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500214,"密码错误");

    //商品模块

    //秒杀模块
    public static CodeMsg MIAO_PATH_INVALIDATE = new CodeMsg(500502,"秒杀路径错误");
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500,"商品已经秒杀完");
    public static CodeMsg REAPETE_MIAO_SHA = new CodeMsg(500501,"不允许重复秒杀");
    public static CodeMsg CODE_ERROR = new CodeMsg(500503,"验证码错误");

    //接口
    public static CodeMsg ACCESS_LIMIT = new CodeMsg(500601,"超过调用限制次数");

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArg(Object... args) {
        int code = this.code;
        String msg = String.format(this.msg, args);
        return new CodeMsg(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
