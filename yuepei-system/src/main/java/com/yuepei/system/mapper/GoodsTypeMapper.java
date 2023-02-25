package com.yuepei.system.mapper;

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
 * @create ：2022/11/19 14:54
 **/
public interface GoodsTypeMapper {
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
     * 删除商品类型
     *
     * @param goodsTypeId 商品类型主键
     * @return 结果
     */
    public int deleteGoodsTypeByGoodsTypeId(Long goodsTypeId);

    /**
     * 批量删除商品类型
     *
     * @param goodsTypeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsTypeByGoodsTypeIds(Long[] goodsTypeIds);

    /**
     * 查询该类型名称是否存在
     * @param goodsType
     * @return
     */
    int checkGoodsType(GoodsType goodsType);

    /**
     * 是否绑定该类型
     * @param goodsTypeIds
     * @return
     */
    int checkGoodsTypeByIds(Long[] goodsTypeIds);
}
