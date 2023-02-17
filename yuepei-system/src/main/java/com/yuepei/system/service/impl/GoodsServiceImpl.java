package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.Goods;
import com.yuepei.system.mapper.GoodsMapper;
import com.yuepei.system.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2022/11/21 15:57
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 查询商品
     *
     * @param goodsId 商品主键
     * @return 商品
     */
    @Override
    public Goods selectGoodsByGoodsId(Long goodsId)
    {
        return goodsMapper.selectGoodsByGoodsId(goodsId);
    }

    /**
     * 查询商品列表
     *
     * @param goods 商品
     * @return 商品
     */
    @Override
    public List<Goods> selectGoodsList(Goods goods)
    {
        return goodsMapper.selectGoodsList(goods);
    }

    /**
     * 新增商品
     *
     * @param goods 商品
     * @return 结果
     */
    @Override
    public int insertGoods(Goods goods)
    {
        goods.setCreateTime(DateUtils.getNowDate());
        return goodsMapper.insertGoods(goods);
    }

    /**
     * 修改商品
     *
     * @param goods 商品
     * @return 结果
     */
    @Override
    public int updateGoods(Goods goods)
    {
        return goodsMapper.updateGoods(goods);
    }

    /**
     * 批量删除商品
     *
     * @param goodsIds 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteGoodsByGoodsIds(Long[] goodsIds)
    {
        return goodsMapper.deleteGoodsByGoodsIds(goodsIds);
    }

    /**
     * 删除商品信息
     *
     * @param goodsId 商品主键
     * @return 结果
     */
    @Override
    public int deleteGoodsByGoodsId(Long goodsId)
    {
        return goodsMapper.deleteGoodsByGoodsId(goodsId);
    }
}
