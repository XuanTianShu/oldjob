package com.yuepei.system.service.impl;

import com.yuepei.system.domain.GoodsType;
import com.yuepei.system.mapper.GoodsTypeMapper;
import com.yuepei.system.service.GoodsTypeService;
import org.apache.ibatis.annotations.Select;
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
 * @create ：2022/11/19 14:56
 **/
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    /**
     * 查询商品类型
     *
     * @param goodsTypeId 商品类型主键
     * @return 商品类型
     */
    @Override
    public GoodsType selectGoodsTypeByGoodsTypeId(Long goodsTypeId)
    {
        return goodsTypeMapper.selectGoodsTypeByGoodsTypeId(goodsTypeId);
    }

    /**
     * 查询商品类型列表
     *
     * @param goodsType 商品类型
     * @return 商品类型
     */
    @Override
    public List<GoodsType> selectGoodsTypeList(GoodsType goodsType)
    {
        return goodsTypeMapper.selectGoodsTypeList(goodsType);
    }

    /**
     * 新增商品类型
     *
     * @param goodsType 商品类型
     * @return 结果
     */
    @Override
    public int insertGoodsType(GoodsType goodsType)
    {
        return goodsTypeMapper.insertGoodsType(goodsType);
    }

    /**
     * 修改商品类型
     *
     * @param goodsType 商品类型
     * @return 结果
     */
    @Override
    public int updateGoodsType(GoodsType goodsType)
    {
        return goodsTypeMapper.updateGoodsType(goodsType);
    }

    /**
     * 批量删除商品类型
     *
     * @param goodsTypeIds 需要删除的商品类型主键
     * @return 结果
     */
    @Override
    public int deleteGoodsTypeByGoodsTypeIds(Long[] goodsTypeIds)
    {
        return goodsTypeMapper.deleteGoodsTypeByGoodsTypeIds(goodsTypeIds);
    }

    /**
     * 删除商品类型信息
     *
     * @param goodsTypeId 商品类型主键
     * @return 结果
     */
    @Override
    public int deleteGoodsTypeByGoodsTypeId(Long goodsTypeId)
    {
        return goodsTypeMapper.deleteGoodsTypeByGoodsTypeId(goodsTypeId);
    }
}
