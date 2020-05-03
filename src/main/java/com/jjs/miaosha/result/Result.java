package com.jjs.miaosha.result;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result(T data) {
        this.data = data;
        this.code = 0;
        this.msg = "success";
    }

    public Result(CodeMsg codeMsg) {
        if (codeMsg == null) {
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public static  Result error(CodeMsg codeMsg) {
        return new Result<>(codeMsg);
    }

    public static <T> Result<T> success(T date) {
        return new Result<>(date);

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
