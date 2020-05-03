package com.jjs.miaosha.controller;

import com.jjs.miaosha.domain.User;
import com.jjs.miaosha.qmq.QmqSend;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.redis.UserKey;
import com.jjs.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jjs
 * @Version 1.0 2020/4/12
 */
@Controller
@RequestMapping("/demo")
public class SimpleController {

    @Autowired
    private QmqSend qmqSend;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "joshua");
        return "hello";
    }

    @RequestMapping("/db/get/{id}")
    @ResponseBody
    public User getDb(@PathVariable(value = "id")Integer id) {
        return userService.getUserByid(id);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public User redisGet() {
        User user = new User();
        user.setId(1);
        redisService.set(UserKey.getById,"key1", user);

        return redisService.get(UserKey.getById,"key1", User.class);
    }

    @RequestMapping("/mq")
    @ResponseBody
    public String mq(Model model) {

        qmqSend.send("hello world");

        return "hello";
    }
}
