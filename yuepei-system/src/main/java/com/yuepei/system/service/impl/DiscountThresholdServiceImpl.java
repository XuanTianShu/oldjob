package com.yuepei.system.service.impl;

import com.yuepei.common.utils.DateUtils;
import com.yuepei.system.domain.DiscountThreshold;
import com.yuepei.system.mapper.DiscountThresholdMapper;
import com.yuepei.system.service.IDiscountThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门槛类型Service业务层处理
 *
 * @author ohy
 * @date 2023-02-24
 */
@Service
public class DiscountThresholdServiceImpl implements IDiscountThresholdService
{
    @Autowired
    private DiscountThresholdMapper discountThresholdMapper;

    /**
     * 查询门槛类型
     *
     * @param id 门槛类型主键
     * @return 门槛类型
     */
    @Override
    public DiscountThreshold selectDiscountThresholdById(Long id)
    {
        return discountThresholdMapper.selectDiscountThresholdById(id);
    }

    /**
     * 查询门槛类型列表
     *
     * @param discountThreshold 门槛类型
     * @return 门槛类型
     */
    @Override
    public List<DiscountThreshold> selectDiscountThresholdList(DiscountThreshold discountThreshold)
    {
        return discountThresholdMapper.selectDiscountThresholdList(discountThreshold);
    }

    /**
     * 新增门槛类型
     *
     * @param discountThreshold 门槛类型
     * @return 结果
     */
    @Override
    public int insertDiscountThreshold(DiscountThreshold discountThreshold)
    {
        discountThreshold.setCreateTime(DateUtils.getNowDate());
        return discountThresholdMapper.insertDiscountThreshold(discountThreshold);
    }

    /**
     * 修改门槛类型
     *
     * @param discountThreshold 门槛类型
     * @return 结果
     */
    @Override
    public int updateDiscountThreshold(DiscountThreshold discountThreshold)
    {
        discountThreshold.setUpdateTime(DateUtils.getNowDate());
        return discountThresholdMapper.updateDiscountThreshold(discountThreshold);
    }

    /**
     * 批量删除门槛类型
     *
     * @param ids 需要删除的门槛类型主键
     * @return 结果
     */
    @Override
    public int deleteDiscountThresholdByIds(Long[] ids)
    {
        return discountThresholdMapper.deleteDiscountThresholdByIds(ids);
    }

    /**
     * 删除门槛类型信息
     *
     * @param id 门槛类型主键
     * @return 结果
     */
    @Override
    public int deleteDiscountThresholdById(Long id)
    {
        return discountThresholdMapper.deleteDiscountThresholdById(id);
    }
}
