package com.jjs.miaosha.service;

import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.domain.OrderInfo;
import com.jjs.miaosha.redis.MiaoShaUserKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author jjs
 * @Version 1.0 2020/4/19
 */
@Service
public class MiaoshaService {
    private static char[] ops = new char[]{'+','-','*'};

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        // 减库存
        boolean stock = goodsService.reduceStock(goods);
        if (stock) {
            return orderService.createOrder(user, goods);
        }
        return null;
    }


    public BufferedImage getImage(MiaoshaUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0XDCDCDC));
        g.fillRect(0,0,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(0,0,width -1, height -1);
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x,y,0,0);
        }
        String code = createVerifyCode(random);
        g.setColor(new Color(0,100,0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(code, 8, 24);
        g.dispose();

        int md = calc(code);
        redisService.set(MiaoShaUserKey.code, user.getId()+","+goodsId,md);
        return image;
    }

    private int calc(String code) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine javaScript = manager.getEngineByName("JavaScript");
            return (Integer)javaScript.eval(code);
        } catch (Exception e) {
            return 0;
        }
    }

    private String createVerifyCode(Random random) {
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        int c = random.nextInt(10);
        char op1 = ops[random.nextInt(3)];
        char op2 = ops[random.nextInt(3)];
        return "" + a + op1 + b + op2 + c;
    }
}
