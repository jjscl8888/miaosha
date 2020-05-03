package com.jjs.miaosha.service;

import com.jjs.miaosha.dao.GoodsDao;
import com.jjs.miaosha.domain.Goods;
import com.jjs.miaosha.domain.MiaoshaGoods;
import com.jjs.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jjs
 * @Version 1.0 2020/4/19
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        return goodsDao.reduceStock(goods.getId()) > 0;
    }

    public List<GoodsVo> getAllGoods() {
        return goodsDao.listGoodsVo();
    }
}
