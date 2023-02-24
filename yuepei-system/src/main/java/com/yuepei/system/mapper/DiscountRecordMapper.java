package com.yuepei.system.mapper;

import com.yuepei.system.domain.DiscountRecord;

import java.util.List;

/**
 * 优惠券发放记录Mapper接口
 *
 * @author ohy
 * @date 2023-02-24
 */
public interface DiscountRecordMapper
{
    /**
     * 查询优惠券发放记录
     *
     * @param id 优惠券发放记录主键
     * @return 优惠券发放记录
     */
    public DiscountRecord selectDiscountRecordById(Long id);

    /**
     * 查询优惠券发放记录列表
     *
     * @param discountRecord 优惠券发放记录
     * @return 优惠券发放记录集合
     */
    public List<DiscountRecord> selectDiscountRecordList(DiscountRecord discountRecord);

    /**
     * 新增优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    public int insertDiscountRecord(DiscountRecord discountRecord);

    /**
     * 修改优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    public int updateDiscountRecord(DiscountRecord discountRecord);

    /**
     * 删除优惠券发放记录
     *
     * @param id 优惠券发放记录主键
     * @return 结果
     */
    public int deleteDiscountRecordById(Long id);

    /**
     * 批量删除优惠券发放记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDiscountRecordByIds(Long[] ids);
}