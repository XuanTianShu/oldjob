package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.Discount;
import com.yuepei.system.mapper.DiscountMapper;
import com.yuepei.system.service.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券Service业务层处理
 *
 * @author ohy
 * @date 2023-02-21
 */
@Service
public class DiscountServiceImpl implements IDiscountService
{
    @Autowired
    private DiscountMapper discountMapper;

    /**
     * 查询优惠券
     *
     * @param id 优惠券主键
     * @return 优惠券
     */
    @Override
    public Discount selectDiscountById(Long id)
    {
        return discountMapper.selectDiscountById(id);
    }

    /**
     * 查询优惠券列表
     *
     * @param discount 优惠券
     * @return 优惠券
     */
    @Override
    public List<Discount> selectDiscountList(Discount discount)
    {
        return discountMapper.selectDiscountList(discount);
    }

    /**
     * 新增优惠券
     *
     * @param discount 优惠券
     * @return 结果
     */
    @Override
    public int insertDiscount(Discount discount)
    {
        discount.setCreateTime(DateUtils.getNowDate());
        return discountMapper.insertDiscount(discount);
    }

    /**
     * 修改优惠券
     *
     * @param discount 优惠券
     * @return 结果
     */
    @Override
    public int updateDiscount(Discount discount)
    {
        return discountMapper.updateDiscount(discount);
    }

    /**
     * 批量删除优惠券
     *
     * @param ids 需要删除的优惠券主键
     * @return 结果
     */
    @Override
    public int deleteDiscountByIds(Long[] ids)
    {
        return discountMapper.deleteDiscountByIds(ids);
    }

    /**
     * 删除优惠券信息
     *
     * @param id 优惠券主键
     * @return 结果
     */
    @Override
    public int deleteDiscountById(Long id)
    {
        return discountMapper.deleteDiscountById(id);
    }

    /**
     * 查询该优惠券库存
     * @param discountId
     * @return
     */
    @Override
    public int checkDiscountSum(Long discountId) {
        return discountMapper.checkDiscountSum(discountId);
    }
}
