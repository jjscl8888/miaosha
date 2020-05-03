package com.jjs.miaosha.controller;

import com.jjs.miaosha.access.AccessLimit;
import com.jjs.miaosha.domain.MiaoshaOrder;
import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.exception.GlobalException;
import com.jjs.miaosha.qmq.QmqSend;
import com.jjs.miaosha.qmq.entity.MiaoShaEntity;
import com.jjs.miaosha.redis.GoodsKey;
import com.jjs.miaosha.redis.MiaoShaUserKey;
import com.jjs.miaosha.redis.OrderKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.result.CodeMsg;
import com.jjs.miaosha.result.Result;
import com.jjs.miaosha.service.GoodsService;
import com.jjs.miaosha.service.MiaoshaService;
import com.jjs.miaosha.service.OrderService;
import com.jjs.miaosha.util.MD5Util;
import com.jjs.miaosha.util.UUIDUtil;
import com.jjs.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jjs
 * @Version 1.0 2020/4/19
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    private static final Map<Long, Boolean> stockOverMap = new HashMap<>();

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QmqSend qmqSend;

    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, MiaoshaUser user,
                                @RequestParam("goodsId") long goodsId, @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        String res = redisService.get(MiaoShaUserKey.path, "" + user.getId() + "_" + goodsId, String.class);
        if (!path.equalsIgnoreCase(res)) {
            throw new GlobalException(CodeMsg.MIAO_PATH_INVALIDATE);
        }

        Boolean flag = stockOverMap.get(goodsId);
        if (flag) {
            throw new GlobalException(CodeMsg.MIAO_SHA_OVER);
        }

        // 减少redis中的库存
        Long stock = redisService.decr(GoodsKey.getGoodsByID, "" + goodsId);
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        Integer stock = goods.getStockCount();
        if (stock < 0) {
            stockOverMap.put(goodsId, true);
            throw new GlobalException(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断该用户是否已经秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            throw new GlobalException(CodeMsg.REAPETE_MIAO_SHA);
        }

        // 请求入队
        MiaoShaEntity miaoShaEntity = new MiaoShaEntity();
        miaoShaEntity.setUser(user);
        miaoShaEntity.setGoodsId(goodsId);
        qmqSend.send(miaoShaEntity);

//        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);
//        model.addAttribute("orderInfo",orderInfo);
//        model.addAttribute("goods",goods);

        return Result.success(0);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goods = goodsService.getAllGoods();
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }
        for (GoodsVo good : goods) {
            redisService.set(GoodsKey.getGoodsByID,"" + good.getId(), good.getStockCount());
            stockOverMap.put(good.getId(), false);
        }
    }

    @RequestMapping("result")
    @ResponseBody
    public Result<Long> result( MiaoshaUser user,
                                @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        Long res = redisService.get(OrderKey.orderCreateKey, "" + user.getId() + "_" + goodsId, Long.class);
        if (res == null) {
            return Result.success(0L);
        } else {
            return Result.success(res);
        }
    }

    @AccessLimit(seconds = 5, maxCount = 5)
    @RequestMapping("path")
    @ResponseBody
    public Result<String> path( MiaoshaUser user,
                                @RequestParam("goodsId") long goodsId,
                                @RequestParam(value = "verifyCode", defaultValue = "0") long code) {
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        Integer resCode = redisService.get(MiaoShaUserKey.code, user.getId() + "," + goodsId, Integer.class);
        if (resCode == null || code != resCode) {
            throw new GlobalException(CodeMsg.CODE_ERROR);
        }
        redisService.delete(MiaoShaUserKey.code, user.getId() + "," + goodsId);

        String res = MD5Util.md5(UUIDUtil.uuid());
        redisService.set(MiaoShaUserKey.path,""+user.getId()+ "_"+goodsId, res);
        return Result.success(res);
    }

    @RequestMapping("verifyCode")
    @ResponseBody
    public Result<String> verifyCode(HttpServletResponse response, MiaoshaUser user,
                                     @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        try {
            BufferedImage image = miaoshaService.getImage(user, goodsId);
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "JPG", outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {

        }
        return null;
    }
}
