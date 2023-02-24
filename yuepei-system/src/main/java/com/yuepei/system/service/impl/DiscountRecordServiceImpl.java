package com.yuepei.system.service.impl;

import com.yuepei.system.domain.DiscountRecord;
import com.yuepei.system.mapper.DiscountRecordMapper;
import com.yuepei.system.service.IDiscountRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券发放记录Service业务层处理
 *
 * @author ohy
 * @date 2023-02-24
 */
@Service
public class DiscountRecordServiceImpl implements IDiscountRecordService
{
    @Autowired
    private DiscountRecordMapper discountRecordMapper;

    /**
     * 查询优惠券发放记录
     *
     * @param id 优惠券发放记录主键
     * @return 优惠券发放记录
     */
    @Override
    public DiscountRecord selectDiscountRecordById(Long id)
    {
        return discountRecordMapper.selectDiscountRecordById(id);
    }

    /**
     * 查询优惠券发放记录列表
     *
     * @param discountRecord 优惠券发放记录
     * @return 优惠券发放记录
     */
    @Override
    public List<DiscountRecord> selectDiscountRecordList(DiscountRecord discountRecord)
    {
        return discountRecordMapper.selectDiscountRecordList(discountRecord);
    }

    /**
     * 新增优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    @Override
    public int insertDiscountRecord(DiscountRecord discountRecord)
    {
        return discountRecordMapper.insertDiscountRecord(discountRecord);
    }

    /**
     * 修改优惠券发放记录
     *
     * @param discountRecord 优惠券发放记录
     * @return 结果
     */
    @Override
    public int updateDiscountRecord(DiscountRecord discountRecord)
    {
        return discountRecordMapper.updateDiscountRecord(discountRecord);
    }

    /**
     * 批量删除优惠券发放记录
     *
     * @param ids 需要删除的优惠券发放记录主键
     * @return 结果
     */
    @Override
    public int deleteDiscountRecordByIds(Long[] ids)
    {
        return discountRecordMapper.deleteDiscountRecordByIds(ids);
    }

    /**
     * 删除优惠券发放记录信息
     *
     * @param id 优惠券发放记录主键
     * @return 结果
     */
    @Override
    public int deleteDiscountRecordById(Long id)
    {
        return discountRecordMapper.deleteDiscountRecordById(id);
    }
}
