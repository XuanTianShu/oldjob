package com.yuepei.system.service;

import com.yuepei.system.domain.DiscountType;

import java.util.List;

/**
 * 优惠券类型Service接口
 *
 * @author ohy
 * @date 2023-02-22
 */
public interface IDiscountTypeService
{
    /**
     * 查询优惠券类型
     *
     * @param id 优惠券类型主键
     * @return 优惠券类型
     */
    public DiscountType selectDiscountTypeById(Long id);

    /**
     * 查询优惠券类型列表
     *
     * @param discountType 优惠券类型
     * @return 优惠券类型集合
     */
    public List<DiscountType> selectDiscountTypeList(DiscountType discountType);

    /**
     * 新增优惠券类型
     *
     * @param discountType 优惠券类型
     * @return 结果
     */
    public int insertDiscountType(DiscountType discountType);

    /**
     * 修改优惠券类型
     *
     * @param discountType 优惠券类型
     * @return 结果
     */
    public int updateDiscountType(DiscountType discountType);

    /**
     * 批量删除优惠券类型
     *
     * @param ids 需要删除的优惠券类型主键集合
     * @return 结果
     */
    public int deleteDiscountTypeByIds(Long[] ids);

    /**
     * 删除优惠券类型信息
     *
     * @param id 优惠券类型主键
     * @return 结果
     */
    public int deleteDiscountTypeById(Long id);
}
