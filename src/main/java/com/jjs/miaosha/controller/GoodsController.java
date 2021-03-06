package com.jjs.miaosha.controller;

import com.jjs.miaosha.domain.MiaoshaUser;
import com.jjs.miaosha.redis.GoodsKey;
import com.jjs.miaosha.redis.RedisService;
import com.jjs.miaosha.result.Result;
import com.jjs.miaosha.service.GoodsService;
import com.jjs.miaosha.vo.GoodsDetailVo;
import com.jjs.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String to(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user) {
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", user);
        // 查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
//        return "goods_list";

        // 手动渲染
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", webContext);

        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList,"", html);
        }
        return html;
        // 返回
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> to(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                    @PathVariable("goodsId")Long goodsId) {
        // 取缓存
//        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }

        model.addAttribute("user", user);
        // 查询商品列表
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int remainSeconds = 0;
        int miaoshaStatus = 0;
        if (now <startAt) {
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt - now)/1000;
        } else if(now >endAt) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
       // return "goods_detail";

        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);

        return Result.success(goodsDetailVo);
//        // 手动解析
//        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
//
//        if (!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsDetail,"" + goodsId, html);
//        }
//        // 返回
//        return html;
    }
}
