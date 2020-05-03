package com.jjs.miaosha.exception;

import com.jjs.miaosha.result.CodeMsg;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
public class GlobalException extends RuntimeException {

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.getMsg());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
