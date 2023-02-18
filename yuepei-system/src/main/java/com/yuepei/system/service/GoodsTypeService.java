package com.yuepei.system.service;

import com.yuepei.system.domain.GoodsType;

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
public interface GoodsTypeService {
    /**
     * 查询商品类型
     *
     * @param goodsTypeId 商品类型主键
     * @return 商品类型
     */
    public GoodsType selectGoodsTypeByGoodsTypeId(Long goodsTypeId);

    /**
     * 查询商品类型列表
     *
     * @param goodsType 商品类型
     * @return 商品类型集合
     */
    public List<GoodsType> selectGoodsTypeList(GoodsType goodsType);

    /**
     * 新增商品类型
     *
     * @param goodsType 商品类型
     * @return 结果
     */
    public int insertGoodsType(GoodsType goodsType);

    /**
     * 修改商品类型
     *
     * @param goodsType 商品类型
     * @return 结果
     */
    public int updateGoodsType(GoodsType goodsType);

    /**
     * 批量删除商品类型
     *
     * @param goodsTypeIds 需要删除的商品类型主键集合
     * @return 结果
     */
    public int deleteGoodsTypeByGoodsTypeIds(Long[] goodsTypeIds);

    /**
     * 删除商品类型信息
     *
     * @param goodsTypeId 商品类型主键
     * @return 结果
     */
    public int deleteGoodsTypeByGoodsTypeId(Long goodsTypeId);
}
