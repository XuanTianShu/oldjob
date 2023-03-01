package com.yuepei.system.service;

import com.yuepei.common.core.domain.AjaxResult;
import com.yuepei.common.core.domain.entity.SysUser;
import com.yuepei.system.domain.Discount;

import java.util.List;

/**
 * 优惠券Service接口
 *
 * @author ohy
 * @date 2023-02-21
 */
public interface IDiscountService
{
    /**
     * 查询优惠券
     *
     * @param id 优惠券主键
     * @return 优惠券
     */
    public Discount selectDiscountById(Long id);

    /**
     * 查询优惠券列表
     *
     * @param discount 优惠券
     * @return 优惠券集合
     */
    public List<Discount> selectDiscountList(Discount discount);

    /**
     * 新增优惠券
     *
     * @param discount 优惠券
     * @return 结果
     */
    public int insertDiscount(Discount discount);

    /**
     * 修改优惠券
     *
     * @param discount 优惠券
     * @return 结果
     */
    public int updateDiscount(Discount discount);

    /**
     * 批量删除优惠券
     *
     * @param ids 需要删除的优惠券主键集合
     * @return 结果
     */
    public int deleteDiscountByIds(Long[] ids);

    /**
     * 删除优惠券信息
     *
     * @param id 优惠券主键
     * @return 结果
     */
    public int deleteDiscountById(Long id);

    /**
     * 用户兑换优惠券
     * @param discountId 优惠券编号
     * @param user 用户
     * @return
     */
    AjaxResult updateUserIntegral(Long discountId, SysUser user);
}
