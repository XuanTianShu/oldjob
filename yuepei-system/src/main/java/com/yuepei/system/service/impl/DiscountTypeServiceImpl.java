package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.DiscountType;
import com.yuepei.system.mapper.DiscountTypeMapper;
import com.yuepei.system.service.IDiscountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券类型Service业务层处理
 *
 * @author ohy
 * @date 2023-02-22
 */
@Service
public class DiscountTypeServiceImpl implements IDiscountTypeService
{
    @Autowired
    private DiscountTypeMapper discountTypeMapper;

    /**
     * 查询优惠券类型
     *
     * @param id 优惠券类型主键
     * @return 优惠券类型
     */
    @Override
    public DiscountType selectDiscountTypeById(Long id)
    {
        return discountTypeMapper.selectDiscountTypeById(id);
    }

    /**
     * 查询优惠券类型列表
     *
     * @param discountType 优惠券类型
     * @return 优惠券类型
     */
    @Override
    public List<DiscountType> selectDiscountTypeList(DiscountType discountType)
    {
        return discountTypeMapper.selectDiscountTypeList(discountType);
    }

    /**
     * 新增优惠券类型
     *
     * @param discountType 优惠券类型
     * @return 结果
     */
    @Override
    public int insertDiscountType(DiscountType discountType)
    {
        discountType.setCreateTime(DateUtils.getNowDate());
        return discountTypeMapper.insertDiscountType(discountType);
    }

    /**
     * 修改优惠券类型
     *
     * @param discountType 优惠券类型
     * @return 结果
     */
    @Override
    public int updateDiscountType(DiscountType discountType)
    {
        discountType.setUpdateTime(DateUtils.getNowDate());
        return discountTypeMapper.updateDiscountType(discountType);
    }

    /**
     * 批量删除优惠券类型
     *
     * @param ids 需要删除的优惠券类型主键
     * @return 结果
     */
    @Override
    public int deleteDiscountTypeByIds(Long[] ids)
    {
        return discountTypeMapper.deleteDiscountTypeByIds(ids);
    }

    /**
     * 删除优惠券类型信息
     *
     * @param id 优惠券类型主键
     * @return 结果
     */
    @Override
    public int deleteDiscountTypeById(Long id)
    {
        return discountTypeMapper.deleteDiscountTypeById(id);
    }
}
