package com.jjs.miaosha.access;

import com.jjs.miaosha.context.UserContext;
import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.redis.AccessKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.result.CodeMsg;
import com.jjs.miaosha.service.MiaoShaUserService;
import com.jjs.miaosha.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author jjs
 * @Version 1.0 2020/5/3
 */

@Service
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoShaUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {

            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            int maxCount = accessLimit.maxCount();
            boolean required = accessLimit.required();
            int seconds = accessLimit.seconds();

            String uri = request.getRequestURI();
            String key = uri;
            if (required) {
                if (user == null) {
                    WebUtils.render(response, CodeMsg.SERVER_ERROR);
                    return false;
                }
                key += ","+user.getId();
            }
            Integer count = redisService.get(AccessKey.accessKey(seconds), key, Integer.class);
            if (count == null) {
                redisService.set(AccessKey.accessKey(seconds), key ,1);
            } else if (count <= maxCount) {
                redisService.incr(AccessKey.accessKey(seconds), key);
            } else {
                WebUtils.render(response, CodeMsg.ACCESS_LIMIT);
                return false;
            }
        }
        return true;
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(MiaoShaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoShaUserService.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(cookieNameToken)) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
