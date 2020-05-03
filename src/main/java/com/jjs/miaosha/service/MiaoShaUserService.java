package com.jjs.miaosha.service;

import com.jjs.miaosha.vo.LoginVo;
import com.jjs.miaosha.dao.MiaoShaUserDao;
import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.exception.GlobalException;
import com.jjs.miaosha.redis.MiaoShaUserKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.result.CodeMsg;
import com.jjs.miaosha.util.MD5Util;
import com.jjs.miaosha.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
@Service
public class MiaoShaUserService {

    @Autowired
    private MiaoShaUserDao miaoShaUserDao;

    @Autowired
    private RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public MiaoshaUser getById(long id) {
        // 取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoShaUserKey.GetUserById, "" + id, MiaoshaUser.class);
        if (miaoshaUser != null) {
            return miaoshaUser;
        }
        //数据库去数据库
        miaoshaUser = miaoShaUserDao.getById(id);
        if (miaoshaUser != null) {
            redisService.set(MiaoShaUserKey.GetUserById, "" + id, miaoshaUser);
        }
        return miaoshaUser;
    }

    public boolean updatePassword(String token, long id, String passwordNew) {
        // 数据库取数据
        MiaoshaUser user = miaoShaUserDao.getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 更新数据库
        MiaoshaUser miaoshaUser = new MiaoshaUser();
        miaoshaUser.setId(id);
        miaoshaUser.setPassword(MD5Util.fromPassToDBPass(passwordNew, user.getSalt()));
        miaoShaUserDao.update(miaoshaUser);

        // 更新缓存
        redisService.delete(MiaoShaUserKey.GetUserById, ""+id);
        user.setPassword(miaoshaUser.getPassword());
        redisService.set(MiaoShaUserKey.token, token,user);
        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 验证密码
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        String calcPass = MD5Util.fromPassToDBPass(password, salt);
        if (!calcPass.equalsIgnoreCase(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, user, token);
        return token;
    }

    private void addCookie(HttpServletResponse response, MiaoshaUser user, String token) {
        // 生成token
        redisService.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {

        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoShaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            addCookie(response, user, token);
        }
        return user;
    }
}
