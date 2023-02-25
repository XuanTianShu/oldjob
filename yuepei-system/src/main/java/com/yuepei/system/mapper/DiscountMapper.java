package com.yuepei.system.mapper;

import com.yuepei.system.domain.Discount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券Mapper接口
 *
 * @author ohy
 * @date 2023-02-21
 */
public interface DiscountMapper
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
     * 删除优惠券
     *
     * @param id 优惠券主键
     * @return 结果
     */
    public int deleteDiscountById(Long id);

    /**
     * 批量删除优惠券
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDiscountByIds(Long[] ids);

    /**
     * 查询该优惠券库存
     * @param discountId
     * @return
     */
    int checkDiscountSum(@Param("discountId") Long discountId);
}
