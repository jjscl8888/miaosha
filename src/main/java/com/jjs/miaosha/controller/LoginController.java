package com.jjs.miaosha.controller;

import com.jjs.miaosha.vo.LoginVo;
import com.jjs.miaosha.exception.GlobalException;
import com.jjs.miaosha.result.CodeMsg;
import com.jjs.miaosha.result.Result;
import com.jjs.miaosha.service.MiaoShaUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
@Controller
@RequestMapping("login")
public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);

    @Autowired
    private MiaoShaUserService miaoShaUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/do_login", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        LOGGER.info(loginVo.toString());

        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        return Result.success(miaoShaUserService.login(response, loginVo));
    }
}
