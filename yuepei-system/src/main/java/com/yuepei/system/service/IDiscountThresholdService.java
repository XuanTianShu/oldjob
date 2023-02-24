package com.yuepei.system.service;

import com.yuepei.system.domain.DiscountThreshold;

import java.util.List;

/**
 * 门槛类型Service接口
 *
 * @author ohy
 * @date 2023-02-24
 */
public interface IDiscountThresholdService
{
    /**
     * 查询门槛类型
     *
     * @param id 门槛类型主键
     * @return 门槛类型
     */
    public DiscountThreshold selectDiscountThresholdById(Long id);

    /**
     * 查询门槛类型列表
     *
     * @param discountThreshold 门槛类型
     * @return 门槛类型集合
     */
    public List<DiscountThreshold> selectDiscountThresholdList(DiscountThreshold discountThreshold);

    /**
     * 新增门槛类型
     *
     * @param discountThreshold 门槛类型
     * @return 结果
     */
    public int insertDiscountThreshold(DiscountThreshold discountThreshold);

    /**
     * 修改门槛类型
     *
     * @param discountThreshold 门槛类型
     * @return 结果
     */
    public int updateDiscountThreshold(DiscountThreshold discountThreshold);

    /**
     * 批量删除门槛类型
     *
     * @param ids 需要删除的门槛类型主键集合
     * @return 结果
     */
    public int deleteDiscountThresholdByIds(Long[] ids);

    /**
     * 删除门槛类型信息
     *
     * @param id 门槛类型主键
     * @return 结果
     */
    public int deleteDiscountThresholdById(Long id);
}
